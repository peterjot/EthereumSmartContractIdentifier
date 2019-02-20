package com.smartcontract.bytecode;

import com.smartcontract.disassembler.Disassembler;
import com.smartcontract.disassembler.Instruction;
import com.smartcontract.solidity.IdentifiedSolidityFileDto;
import com.smartcontract.solidity.SolidityFile;
import com.smartcontract.solidity.SolidityFunction;
import com.smartcontract.solidity.SolidityService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BytecodeService {

    private static final String PUSH_4_MNEMONIC = "PUSH4";
    private static final String EQ_MNEMONIC = "EQ";
    private static final String PUSH_2_MNEMONIC = "PUSH2";
    private static final Logger LOGGER = getLogger(BytecodeService.class);
    private static final int RESULT_LIMIT = 10;

    private final SolidityService solidityService;
    private final Disassembler disassembler;

    @Autowired
    public BytecodeService(SolidityService solidityService, Disassembler disassembler) {
        requireNonNull(solidityService, "Expected not-null solidityService");
        requireNonNull(disassembler, "Expected not-null disassembler");
        this.solidityService = solidityService;
        this.disassembler = disassembler;
    }

    public List<IdentifiedSolidityFileDto> findTop10FileHashesWithValueOfMatch(String bytecode) {
        requireNonNull(bytecode, "Expected not-null bytecode");

        List<String> functionSelectors = findFunctionSelectors(bytecode);
        LOGGER.info("Functions in bytecode: {}", functionSelectors.size());

        return solidityService
                .findSolidityFilesBySelectorIn(functionSelectors)
                .stream()
                .map(solidityFile -> getSelectorWithMatchValue(functionSelectors, solidityFile))
                .sorted((pair1, pair2) -> Double.compare(pair2.getValueOfMatch(), pair1.getValueOfMatch()))
                .limit(RESULT_LIMIT)
                .collect(toList());

    }

    public List<IdentifiedSolidityFileDto> findAllFileHashesWithValueOfMatch(String bytecode) {
        requireNonNull(bytecode, "Expected not-null bytecode");

        List<String> functionSelectors = findFunctionSelectors(bytecode);
        LOGGER.info("Functions in bytecode: {}", functionSelectors.size());

        return solidityService
                .findSolidityFilesBySelectorIn(functionSelectors)
                .stream()
                .map(solidityFile -> getSelectorWithMatchValue(functionSelectors, solidityFile))
                .sorted((pair1, pair2) -> Double.compare(pair2.getValueOfMatch(), pair1.getValueOfMatch()))
                .collect(toList());
    }

    private List<String> findFunctionSelectors(String bytecode) {
        List<Instruction> instructions = disassembler.disassembly(bytecode);

        List<String> functionSelector = new ArrayList<>();
        for (int i = 0; i < instructions.size() - 2; i++) {
            Instruction first = instructions.get(i);
            Instruction second = instructions.get(i + 1);
            Instruction third = instructions.get(i + 2);

            boolean isFunctionSchemeFound =
                    first.hasMnemonic(PUSH_4_MNEMONIC) && second.hasMnemonic(EQ_MNEMONIC) && third.hasMnemonic(PUSH_2_MNEMONIC);

            if (isFunctionSchemeFound) {
                functionSelector.add(first.getHexParameters());
            }
        }
        return functionSelector;
    }

    private IdentifiedSolidityFileDto getSelectorWithMatchValue(List<String> bytecodeSelectors, SolidityFile solidityFile) {
        String sourceCodeHash = solidityFile.getSourceCodeHash();
        Set<SolidityFunction> solidityFunctions = solidityFile.getSolidityFunctions();

        if (solidityFunctions.size() <= 0) {
            return new IdentifiedSolidityFileDto(sourceCodeHash, 0D);
        }

        long numberOfMatches = solidityFunctions
                .stream()
                .map(SolidityFunction::getSelector)
                .filter(bytecodeSelectors::contains).count();

        double percentOfMatch =
                2 * numberOfMatches / ((double) bytecodeSelectors.size() + solidityFunctions.size());

        LOGGER.info("Matched functions: {}", numberOfMatches);
        LOGGER.info("Functions in solidity file hash: {}, size: {}", solidityFile.getSourceCodeHash(), solidityFunctions.size());

        return new IdentifiedSolidityFileDto(sourceCodeHash, percentOfMatch);
    }
}
