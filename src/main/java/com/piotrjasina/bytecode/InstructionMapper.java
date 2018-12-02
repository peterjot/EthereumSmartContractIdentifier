package com.piotrjasina.bytecode;

import javax.xml.bind.DatatypeConverter;

public class InstructionMapper {
    public static InstructionDto convertToDto(Instruction instruction) {
        return new InstructionDto(instruction.getOpcode(), DatatypeConverter.printHexBinary(instruction.getArgument()).toLowerCase());
    }
}
