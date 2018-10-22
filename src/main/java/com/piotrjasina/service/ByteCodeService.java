package com.piotrjasina.service;

import com.piotrjasina.disassembler.Instruction;
import com.piotrjasina.disassembler.SolidityDisassembler;
import com.piotrjasina.dto.InstructionDto;
import com.piotrjasina.mapper.InstructionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ByteCodeService {

    private final SolidityDisassembler solidityDisassembler;

    @Autowired
    public ByteCodeService(SolidityDisassembler solidityDisassembler) {
        this.solidityDisassembler = solidityDisassembler;
    }

    public List<InstructionDto> getOpcodeArgumentByMnemonic(String byteCode, String mnemonic) {
        List<Instruction> instructions = solidityDisassembler.disassembly(byteCode);

        return instructions
                .stream()
                .filter(instruction -> instruction.getOpcode().name().equals(mnemonic))
                .map(InstructionMapper::convertToDto)
                .collect(toList());
    }


}
