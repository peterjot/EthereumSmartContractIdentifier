package com.piotrjasina.bytecode;

import com.piotrjasina.bytecode.disassembler.Instruction;
import com.piotrjasina.bytecode.disassembler.SolidityDisassembler;
import com.piotrjasina.solidity.function.Function;
import com.piotrjasina.solidity.function.FunctionRepository;
import com.piotrjasina.solidity.solidityfile.SolidityFile;
import com.piotrjasina.solidity.solidityfile.SolidityFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class BytecodeService {

    public static final String FOUR_BYTES_MASK = "ffffffff";
    private static final String PUSH_4_MNEMONIC = "PUSH4";
    private final SolidityDisassembler solidityDisassembler;
    private final SolidityFileRepository solidityFileRepository;
    private final FunctionRepository functionRepository;

    @Autowired
    public BytecodeService(SolidityDisassembler solidityDisassembler, SolidityFileRepository solidityFileRepository, FunctionRepository functionRepository) {
        this.solidityDisassembler = solidityDisassembler;
        this.solidityFileRepository = solidityFileRepository;
        this.functionRepository = functionRepository;
    }

    Map<SolidityFile, Double> findSolidityFileWithCountByBytecode(String bytecode) {
        List<Instruction> instructionsOfBytecode = findPush4Instructions(bytecode);

        return findSolidityFilesWithCountByInstructions(instructionsOfBytecode);
    }

    private List<Instruction> findPush4Instructions(String bytecode) {
        List<Instruction> instructions = solidityDisassembler.disassembly(bytecode);
        return instructions
                .stream()
                .filter(instruction -> instruction.hasMnemonic(PUSH_4_MNEMONIC) && !instruction.getHexArgument().equals(FOUR_BYTES_MASK))
                .collect(toList());
    }

    private Map<SolidityFile, Double> findSolidityFilesWithCountByInstructions(List<Instruction> instructions) {

        final int functionsSelectorCount = instructions.size();
        log.info("Push4 instructions in this bytecode: {}", functionsSelectorCount);


        Map<SolidityFile, Long> unsortedSolidityFileWithCount = instructions
                .stream()
                .map(instruction -> functionRepository.findBySelector(instruction.getHexArgument()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::findFilesByFunction)
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

    private List<SolidityFile> findFilesByFunction(Function function) {
        return solidityFileRepository.findByFunctionsIsContaining(function);
    }
}
