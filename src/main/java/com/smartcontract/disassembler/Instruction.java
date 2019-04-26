package com.smartcontract.disassembler;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


public class Instruction {

    public static final String PUSH4_MASK = "ffffffff";

    private final Opcode opcode;
    private final String hexParameter;

    Instruction(Opcode opcode, String hexParameter) {
        requireNonNull(opcode, "Expected not-null opcode");
        requireNonNull(hexParameter, "Expected not-null hexParameter");
        this.opcode = opcode;
        this.hexParameter = hexParameter.toLowerCase();
    }

    public boolean hasOpcode(Opcode opcode) {
        requireNonNull(opcode, "Expected not-null opcode");
        return this.opcode.equals(opcode);
    }

    public boolean hasHexParameter(String hexParameter) {
        requireNonNull(hexParameter, "Expceted not-null hexParameter");
        return this.hexParameter.equals(hexParameter);
    }

    public String getHexParameter() {
        return hexParameter;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "opcode=" + opcode +
                ", hexParameter='" + hexParameter + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return opcode == that.opcode &&
                Objects.equals(hexParameter, that.hexParameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, hexParameter);
    }
}
