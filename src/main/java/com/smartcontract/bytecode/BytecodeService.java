package com.smartcontract.bytecode;

import com.smartcontract.disassembler.Disassembler;
import com.smartcontract.disassembler.Instruction;
import com.smartcontract.solidity.SolidityFile;
import com.smartcontract.solidity.SolidityFunction;
import com.smartcontract.solidity.SolidityService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lombok.Lombok.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BytecodeService {

    public static final String FOUR_BYTES_MASK = "ffffffff";
    public static final String PUSH_4_MNEMONIC = "PUSH4";

    private static final Logger LOGGER = getLogger(BytecodeService.class);

    private final SolidityService solidityService;
    private final Disassembler disassembler;


    @Autowired
    public BytecodeService(SolidityService solidityService, Disassembler disassembler) {
        checkNotNull(solidityService, "Expected not-null solidityService");
        checkNotNull(disassembler, "Expected not-null disassembler");
        this.solidityService = solidityService;
        this.disassembler = disassembler;
    }

    List<Pair<String, Double>> findFileHashWithPercentageOfMatch(String bytecode) {
        Set<Instruction> instructionsOfBytecode = findPush4Instructions(bytecode);
        return findFileHashWithPercentageOfMatch(instructionsOfBytecode);
    }

    private Set<Instruction> findPush4Instructions(String bytecode) {
        Set<Instruction> instructions = disassembler.disassembly(bytecode);
        return instructions
                .stream()
                .filter(instruction -> instruction.hasMnemonic(PUSH_4_MNEMONIC) && !instruction.getHexParameters().equals(FOUR_BYTES_MASK))
                .collect(toSet());
    }

    private List<Pair<String, Double>> findFileHashWithPercentageOfMatch(Set<Instruction> instructions) {
        List<String> bytecodeSelectors = mapInstructionsToSelectors(instructions);
        LOGGER.info("Functions in bytecode: {}", bytecodeSelectors.size());

        return solidityService
                .findSolidityFilesBySelectorIn(bytecodeSelectors)
                .stream()
                .map(solidityFile -> getSelectorWithMatchValue(bytecodeSelectors, solidityFile))
                .sorted((pair1, pair2) -> Double.compare(pair2.getValue(), pair1.getValue()))
                .collect(toList());

    }

    private List<String> mapInstructionsToSelectors(Set<Instruction> instructions) {
        return instructions
                .stream()
                .map(Instruction::getHexParameters)
                .collect(Collectors.toList());
    }

    private Pair<String, Double> getSelectorWithMatchValue(List<String> bytecodeSelectors, SolidityFile solidityFile) {
        final String sourceCodeHash = solidityFile.getSourceCodeHash();
        final Set<SolidityFunction> solidityFunctions = solidityFile.getSolidityFunctions();

        if (solidityFunctions.size() <= 0) {
            return new Pair<>(sourceCodeHash, 0D);
        }

        final long numberOfMatches = solidityFunctions
                .stream()
                .map(SolidityFunction::getSelector)
                .filter(bytecodeSelectors::contains).count();


        final double percentOfMatch =
                2 * numberOfMatches / ((double) bytecodeSelectors.size() + solidityFunctions.size());

        LOGGER.info("Matched functions: {}", numberOfMatches);
        LOGGER.info("Functions in solidity file hash: {}, size: {}", solidityFile.getSourceCodeHash(), solidityFunctions.size());

        return new Pair<>(sourceCodeHash, percentOfMatch);
    }
}
