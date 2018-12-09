package com.piotrjasina.bytecode;

import com.piotrjasina.bytecode.disassembler.Instruction;
import com.piotrjasina.bytecode.disassembler.SolidityDisassembler;
import com.piotrjasina.solidity.Function;
import com.piotrjasina.solidity.FunctionRepository;
import com.piotrjasina.solidity.SolidityFile;
import com.piotrjasina.solidity.SolidityFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class ByteCodeService {

    private static final String PUSH_4_MNEMONIC = "PUSH4";

    private final SolidityDisassembler solidityDisassembler;
    private final SolidityFileRepository solidityFileRepository;
    private final FunctionRepository functionRepository;

    @Autowired
    public ByteCodeService(SolidityDisassembler solidityDisassembler, SolidityFileRepository solidityFileRepository, FunctionRepository functionRepository) {
        this.solidityDisassembler = solidityDisassembler;
        this.solidityFileRepository = solidityFileRepository;
        this.functionRepository = functionRepository;
    }

    public Map<SolidityFile, Double> findSolidityFileWithCountByByteCode(String byteCode) {
        List<Instruction> instructionsOfByteCode = findPush4Instructions(byteCode);

        return findSolidityFilesWithCountByInstructions(instructionsOfByteCode);
    }

    private List<Instruction> findPush4Instructions(String byteCode) {
        List<Instruction> instructions = solidityDisassembler.disassembly(byteCode);
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
