package com.piotrjasina.disassembler;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Instruction {

    private Opcode opcode;
    private byte[] argument;

    Instruction(Opcode opcode, byte[] argument) {
        this.opcode = opcode;
        this.argument = argument;
    }

}
