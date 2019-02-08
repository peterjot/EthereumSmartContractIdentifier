package com.smartcontract.disassembler;

import java.util.Objects;

import static com.smartcontract.Util.checkNotNull;


public class Instruction {

    private final Opcode opcode;
    private final String hexParameters;

    public boolean hasMnemonic(String mnemonic) {
        checkNotNull(mnemonic, "Expected not-null mnemonic");
        return opcode.hasMnemonic(mnemonic);
    }

    Instruction(Opcode opcode, String hexParameters) {
        checkNotNull(opcode, "Expected not-null opcode");
        checkNotNull(hexParameters, "Expected not-null hexParameters");
        this.opcode = opcode;
        this.hexParameters = hexParameters;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "opcode=" + opcode +
                ", hexParameters='" + hexParameters + '\'' +
                '}';
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public String getHexParameters() {
        return hexParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return opcode == that.opcode &&
                Objects.equals(hexParameters, that.hexParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, hexParameters);
    }
}