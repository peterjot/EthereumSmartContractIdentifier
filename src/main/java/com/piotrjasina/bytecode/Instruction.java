package com.piotrjasina.bytecode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Instruction {

    private Opcode opcode;
    private byte[] argument;

    public Instruction(Opcode opcode, byte[] argument) {
        this.opcode = opcode;
        this.argument = argument;
    }

}
