package com.smartcontract.bytecode;

import com.smartcontract.bytecode.dto.IdentifiedSolidityFileDto;
import com.smartcontract.solidity.SolidityService;
import com.smartcontract.solidity.dto.SolidityFileDto;
import com.smartcontract.solidity.dto.SolidityFunctionDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.smartcontract.bytecode.Instruction.PUSH4_MASK;
import static com.smartcontract.bytecode.Opcode.*;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;


@RequiredArgsConstructor
public class BytecodeService {

    private static final Logger LOGGER = getLogger(BytecodeService.class);
    private static final int RESULT_LIMIT = 10;

    @NonNull
    private final SolidityService solidityService;

    @NonNull
    private final Disassembler disassembler;


    public List<IdentifiedSolidityFileDto> findTop10FileHashesWithValueOfMatch(@NonNull String bytecode) {
        List<String> functionSelectors = findFunctionSelectors(bytecode);

        LOGGER.info("Found functions in solidity: {}", functionSelectors);
        LOGGER.info("Found function selectors in solidity: {}", functionSelectors);

        return solidityService
                .findSolidityFilesBySelectors(functionSelectors)
                .stream()
                .map(solidityFile -> getIdentifiedSolidityFileWithMatchValue(functionSelectors, solidityFile))
                .sorted((pair1, pair2) -> Double.compare(pair2.getValueOfMatch(), pair1.getValueOfMatch()))
                .limit(RESULT_LIMIT)
                .collect(toList());

    }

    private List<String> findFunctionSelectors(String bytecode) {
        List<Instruction> instructions = disassembler.disassembly(bytecode);
        return findFunctionSelectors(instructions);
    }

    private List<String> findFunctionSelectors(List<Instruction> instructions) {
        int offSet = getCreationCodeOffset(instructions);

        List<String> functionSelectors = new ArrayList<>();
        boolean isCalldataloadOpcodeFound = false;

        int i = offSet;
        while (i < instructions.size()) {
            Instruction instruction = instructions.get(i);

            if (instruction.hasOpcode(JUMPDEST)) {
                break;
            }

            if (instruction.hasOpcode(CALLDATALOAD)) {
                isCalldataloadOpcodeFound = true;
            }

            if (isCalldataloadOpcodeFound &&
                    instruction.hasOpcode(PUSH4) &&
                    !(instruction.hasHexParameter(PUSH4_MASK))) {
                functionSelectors.add(instruction.getHexParameter());
            }

            i++;
        }

        return functionSelectors;
    }

    private int getCreationCodeOffset(List<Instruction> instructions) {
        int i = 0;

        while (i < instructions.size() - 2) {
            Instruction first = instructions.get(i);
            Instruction second = instructions.get(i + 1);
            Instruction third = instructions.get(i + 2);

            if (isCreationCodeEndFound(first, second, third)) {
                i += 3;
                break;
            }

            i++;
        }

        return i;
    }

    private boolean isCreationCodeEndFound(Instruction first, Instruction second, Instruction third) {
        return first.hasOpcode(PUSH1) && first.hasHexParameter("00") &&
                second.hasOpcode(RETURN) &&
                third.hasOpcode(STOP);
    }

    private IdentifiedSolidityFileDto getIdentifiedSolidityFileWithMatchValue(List<String> bytecodeSelectors, SolidityFileDto solidityFile) {
        String sourceCodeHash = solidityFile.getSourceCodeHash();
        Set<SolidityFunctionDto> solidityFunctions = solidityFile.getSolidityFunctions();

        if (solidityFunctions.size() <= 0) {
            return new IdentifiedSolidityFileDto(sourceCodeHash);
        }

        long numberOfMatches = getNumberOfMatches(bytecodeSelectors, solidityFunctions);
        double percentOfMatch = calculatePercentOfMatch(bytecodeSelectors, solidityFunctions, numberOfMatches);

        return new IdentifiedSolidityFileDto(sourceCodeHash, percentOfMatch);
    }

    private long getNumberOfMatches(List<String> bytecodeSelectors, Set<SolidityFunctionDto> solidityFunctions) {
        return solidityFunctions
                .stream()
                .map(SolidityFunctionDto::getSelector)
                .filter(bytecodeSelectors::contains).count();
    }

    private double calculatePercentOfMatch(List<String> bytecodeSelectors, Set<SolidityFunctionDto> solidityFunctions, long numberOfMatches) {
        return numberOfMatches / ((double) bytecodeSelectors.size() + solidityFunctions.size() - numberOfMatches);
    }
}
