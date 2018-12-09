package com.piotrjasina.bytecode;

import com.piotrjasina.solidity.Function;
import com.piotrjasina.solidity.FunctionRepository;
import com.piotrjasina.solidity.SolidityFile;
import com.piotrjasina.solidity.SolidityFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<InstructionDto> findImplementationWithCountByByteCode(String byteCode) {
        List<InstructionDto> opcodeArgumentByMnemonic = findPush4Instructions(byteCode);
        Map<SolidityFile, Long> filesByInstructions = findFilesByInstructions(opcodeArgumentByMnemonic);

        return new ArrayList<>();
    }

    private List<InstructionDto> findPush4Instructions(String byteCode) {
        List<Instruction> instructions = solidityDisassembler.disassembly(byteCode);

        return instructions
                .stream()
                .filter(instruction -> instruction.getOpcode().name().equals(PUSH_4_MNEMONIC))
                .map(InstructionMapper::convertToDto)
                .collect(toList());
    }

    private Map<SolidityFile, Long> findFilesByInstructions(List<InstructionDto> instructionDtos) {
        List<Function> functions = findAllFunctionsFromInstructions(instructionDtos);

        log.info("Found functions from bytecode in databse");
        log.info("{}", functions);


        List<SolidityFile> allFilesByFunction = functions
                .stream()
                .map(this::findFilesByFunction)
                .flatMap(Collection::stream)
                .collect(toList());

        return allFilesByFunction
                .stream()
                .collect(groupingBy(identity(), counting()));
    }

    private List<Function> findAllFunctionsFromInstructions(List<InstructionDto> instructionDtos) {
        return instructionDtos
                .stream()
                .map(instructionDto -> functionRepository
                        .findBySelector(instructionDto.getArgument()))
                .filter(Objects::nonNull)
                .collect(toList());
    }


    private List<SolidityFile> findFilesByFunction(Function function) {
        return solidityFileRepository.findByFunctionsIsContaining(function);
    }

}
