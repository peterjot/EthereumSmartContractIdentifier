package com.smartcontract.bytecode;

import com.smartcontract.disassembler.Disassembler;
import com.smartcontract.disassembler.Instruction;
import com.smartcontract.solidity.SolidityFile;
import com.smartcontract.solidity.SolidityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class BytecodeService {

    private final SolidityService solidityService;
    private final Disassembler disassembler;
    private static final String FOUR_BYTES_MASK = "ffffffff";
    private static final String PUSH_4_MNEMONIC = "PUSH4";

    @Autowired
    public BytecodeService(SolidityService solidityService, Disassembler disassembler) {
        checkNotNull(solidityService, "Expected not-null solidityService");
        checkNotNull(disassembler, "Expected not-null disassembler");
        this.solidityService = solidityService;
        this.disassembler = disassembler;
    }

    Map<SolidityFile, Double> findSolidityFileWithCountByBytecode(String bytecode) {
        List<Instruction> instructionsOfBytecode = findPush4Instructions(bytecode);
        return findSolidityFilesWithCountByInstructions(instructionsOfBytecode);
    }

    private List<Instruction> findPush4Instructions(String bytecode) {
        List<Instruction> instructions = disassembler.disassembly(bytecode);
        return instructions
                .stream()
                .filter(instruction -> instruction.hasMnemonic(PUSH_4_MNEMONIC) && !instruction.getHexParameters().equals(FOUR_BYTES_MASK))
                .collect(toList());
    }

    private Map<SolidityFile, Double> findSolidityFilesWithCountByInstructions(List<Instruction> instructions) {

        final int functionsSelectorCount = instructions.size();
        log.info("Push4 instructions in this bytecode: {}", functionsSelectorCount);


        Map<SolidityFile, Long> unsortedSolidityFileWithCount = instructions
                .stream()
                .map(Instruction::getHexParameters)
                .map(solidityService::findSolidityFilesByFunctionSelector)
                .flatMap(Collection::stream)
                .collect(groupingBy(identity(), counting()));

        return unsortedSolidityFileWithCount
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(reverseOrder()))
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), getMatchPercentage(functionsSelectorCount, e.getValue().doubleValue())), Map::putAll);
    }

    private double getMatchPercentage(double functionsSelectorCount, double value) {
        return (value / functionsSelectorCount);
    }
}
