package com.smartcontract.bytecode;

import com.smartcontract.bytecode.disassembler.Instruction;
import com.smartcontract.solidity.solidityfile.SolidityFile;
import com.smartcontract.solidity.solidityfile.SolidityFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.smartcontract.bytecode.disassembler.SolidityDisassembler.disassembly;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class BytecodeService {

    private static final String FOUR_BYTES_MASK = "ffffffff";
    private static final String PUSH_4_MNEMONIC = "PUSH4";
    private final SolidityFileRepository solidityFileRepository;

    @Autowired
    public BytecodeService(SolidityFileRepository solidityFileRepository) {
        checkNotNull(solidityFileRepository, "Expected not-null solidityFileRepository");
        this.solidityFileRepository = solidityFileRepository;
    }

    Map<SolidityFile, Double> findSolidityFileWithCountByBytecode(String bytecode) {
        List<Instruction> instructionsOfBytecode = findPush4Instructions(bytecode);
        return findSolidityFilesWithCountByInstructions(instructionsOfBytecode);
    }

    private List<Instruction> findPush4Instructions(String bytecode) {
        List<Instruction> instructions = disassembly(bytecode);
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
                .map(this::findFilesByFunctionSelector)
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

    private List<SolidityFile> findFilesByFunctionSelector(String functionSelector) {
        return solidityFileRepository.findByFunctionSelectorsIsContaining(functionSelector);
    }
}
