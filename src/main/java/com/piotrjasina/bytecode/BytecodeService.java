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

    public Map<SolidityFile, Double> findSolidityFileWithCountByBytecode(String bytecode) {
        List<Instruction> instructionsOfBytecode = findPush4Instructions(bytecode);

        return findSolidityFilesWithCountByInstructions(instructionsOfBytecode);
    }

    private List<Instruction> findPush4Instructions(String bytecode) {
        List<Instruction> instructions = solidityDisassembler.disassembly(bytecode);
        return instructions
                .stream()
                .filter(instruction -> instruction.hasMnemonic(PUSH_4_MNEMONIC))
                .collect(toList());
    }

    private Map<SolidityFile, Double> findSolidityFilesWithCountByInstructions(List<Instruction> instructions) {

        Map<SolidityFile, Long> unsortedSolidityFileWithCount = instructions
                .stream()
                .map(instruction -> functionRepository.findBySelector(instruction.getHexArgument()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::findFilesByFunction)
                .flatMap(Collection::stream)
                .collect(groupingBy(identity(), counting()));

        double totalMatches = unsortedSolidityFileWithCount.values().stream().mapToDouble(Long::doubleValue).sum();

        return unsortedSolidityFileWithCount
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(reverseOrder()))
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), getMatchPercentage(totalMatches, e.getValue().doubleValue())), Map::putAll);
    }

    private double getMatchPercentage(double totalMatches, double e) {
        return (e / totalMatches);
    }

    private List<SolidityFile> findFilesByFunction(Function function) {
        return solidityFileRepository.findByFunctionsIsContaining(function);
    }
}
