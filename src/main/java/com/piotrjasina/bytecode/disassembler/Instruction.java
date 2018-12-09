package com.piotrjasina.bytecode.disassembler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Instruction {

    private Opcode opcode;
    private String hexArgument;

    public Instruction(Opcode opcode, String hexArgument) {
        this.opcode = opcode;
        this.hexArgument = hexArgument;
    }

    public boolean hasMnemonic(String mnemonic) {
        return opcode.name().equals(mnemonic);
    }

}
