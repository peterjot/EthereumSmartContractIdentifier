package com.piotrjasina.api.service;

import com.piotrjasina.api.disassembler.Instruction;
import com.piotrjasina.api.disassembler.SolidityDisassembler;
import com.piotrjasina.api.dto.InstructionDto;
import com.piotrjasina.api.mapper.InstructionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DisassemblerService {

    private final SolidityDisassembler solidityDisassembler;

    @Autowired
    public DisassemblerService(SolidityDisassembler solidityDisassembler) {
        this.solidityDisassembler = solidityDisassembler;
    }


    public List<InstructionDto> dissassemblyByMnemonic(String byteCode, String mnemonic) {
        List<Instruction> instructions = solidityDisassembler.disassembly(byteCode);

        return instructions
                .stream()
                .filter(instruction -> instruction.getOpcode().name().equals(mnemonic))
                .map(InstructionMapper::toInstructionDto)
                .collect(toList());
    }

    public List<InstructionDto> dissassemblyWithoutArgument(String byteCode) {
        List<Instruction> instructions = solidityDisassembler.disassembly(byteCode);

        return instructions
                .stream()
                .map(InstructionMapper::toInstructionDto)
                .collect(toList());
    }
}
