package io.github.peterjot.bytecode;

import io.github.peterjot.bytecode.dto.IdentifiedSolidityFileDto;
import io.github.peterjot.solidity.SolidityService;
import io.github.peterjot.solidity.dto.SolidityFileDto;
import io.github.peterjot.solidity.dto.SolidityFunctionDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

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
        List<Instruction> creationCodeInstructions =
                instructions.subList(getCreationCodeOffset(instructions), instructions.size());

        return creationCodeInstructions
                .stream()
                .dropWhile(instruction -> !instruction.hasOpcode(Opcode.CALLDATALOAD))
                .takeWhile(instruction -> !instruction.hasOpcode(Opcode.JUMPDEST))
                .filter(instruction -> instruction.hasOpcode(Opcode.PUSH4) && !(instruction.hasHexParameter(Instruction.PUSH4_MASK)))
                .map(Instruction::getHexParameter)
                .collect(toList());
    }

    private int getCreationCodeOffset(List<Instruction> instructions) {

        for (int i = 0; i < instructions.size() - 2; i++) {
            Instruction first = instructions.get(i);
            Instruction second = instructions.get(i + 1);
            Instruction third = instructions.get(i + 2);

            boolean isCreationCodeFound =
                    first.hasOpcode(Opcode.PUSH1) &&
                            first.hasHexParameter("00") &&
                            second.hasOpcode(Opcode.RETURN) &&
                            third.hasOpcode(Opcode.STOP);

            if (isCreationCodeFound) {
                return i + 3;
            }
        }

        throw new IllegalStateException("Creation code not found");
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
