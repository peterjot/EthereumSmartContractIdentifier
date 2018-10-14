package com.piotrjasina.contractidentifier.disassembler;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Instruction {

    private Opcode opcode;

    private int uint8;
    private List<Byte> argumentBytes = new ArrayList<>();

    Instruction(Opcode opcode, int uint8) {
        this.opcode = opcode;
        this.uint8 = uint8 & 0xff;
    }

    void addArgumentByte(byte b) {
        argumentBytes.add(b);
    }
}
