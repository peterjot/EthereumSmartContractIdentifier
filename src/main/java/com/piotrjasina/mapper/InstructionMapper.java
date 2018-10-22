package com.piotrjasina.mapper;

import com.piotrjasina.disassembler.Instruction;
import com.piotrjasina.dto.InstructionDto;

import javax.xml.bind.DatatypeConverter;

public class InstructionMapper {
    public static InstructionDto convertToDto(Instruction instruction) {
        return new InstructionDto(instruction.getOpcode(), DatatypeConverter.printHexBinary(instruction.getArgument()).toLowerCase());
    }
}
