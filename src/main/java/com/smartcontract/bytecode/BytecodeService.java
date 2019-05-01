package com.smartcontract.bytecode;

import com.smartcontract.disassembler.Disassembler;
import com.smartcontract.disassembler.Instruction;
import com.smartcontract.solidity.IdentifiedSolidityFileDto;
import com.smartcontract.solidity.SolidityFile;
import com.smartcontract.solidity.SolidityFunction;
import com.smartcontract.solidity.SolidityService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.smartcontract.disassembler.Instruction.PUSH4_MASK;
import static com.smartcontract.disassembler.Opcode.*;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BytecodeService {

    private static final Logger LOGGER = getLogger(BytecodeService.class);
    private static final int RESULT_LIMIT = 10;

    private final SolidityService solidityService;
    private final Disassembler disassembler;


    public BytecodeService(@NonNull SolidityService solidityService, @NonNull Disassembler disassembler) {
        this.solidityService = solidityService;
        this.disassembler = disassembler;
    }

    public List<IdentifiedSolidityFileDto> findTop10FileHashesWithValueOfMatch(@NonNull String bytecode) {
        List<String> functionSelectors = findFunctionSelectors(bytecode);

        LOGGER.info("Found functions in bytecode: {}", functionSelectors);
        LOGGER.info("Found function selectors in bytecode: {}", functionSelectors);

        return solidityService
                .findSolidityFilesBySelectors(functionSelectors)
                .stream()
                .map(solidityFile -> getIdentifiedSolidityFileWithMatchValue(functionSelectors, solidityFile))
                .sorted((pair1, pair2) -> Double.compare(pair2.getValueOfMatch(), pair1.getValueOfMatch()))
                .limit(RESULT_LIMIT)
                .collect(toList());

    }

    private List<String> findFunctionSelectors(@NonNull String bytecode) {
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

    private IdentifiedSolidityFileDto getIdentifiedSolidityFileWithMatchValue(List<String> bytecodeSelectors, SolidityFile solidityFile) {
        String sourceCodeHash = solidityFile.getSourceCodeHash();
        Set<SolidityFunction> solidityFunctions = solidityFile.getSolidityFunctions();

        if (solidityFunctions.size() <= 0) {
            return new IdentifiedSolidityFileDto(sourceCodeHash);
        }

        long numberOfMatches = getNumberOfMatches(bytecodeSelectors, solidityFunctions);
        double percentOfMatch = calculatePercentOfMatch(bytecodeSelectors, solidityFunctions, numberOfMatches);

        return new IdentifiedSolidityFileDto(sourceCodeHash, percentOfMatch);
    }

    private long getNumberOfMatches(List<String> bytecodeSelectors, Set<SolidityFunction> solidityFunctions) {
        return solidityFunctions
                .stream()
                .map(SolidityFunction::getSelector)
                .filter(bytecodeSelectors::contains).count();
    }

    private double calculatePercentOfMatch(List<String> bytecodeSelectors, Set<SolidityFunction> solidityFunctions, long numberOfMatches) {
        return numberOfMatches / ((double) bytecodeSelectors.size() + solidityFunctions.size() - numberOfMatches);
    }
}
