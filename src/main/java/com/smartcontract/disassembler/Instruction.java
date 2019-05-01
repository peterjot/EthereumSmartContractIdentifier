package com.smartcontract.disassembler;

import lombok.NonNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


public class Instruction {

    public static final String PUSH4_MASK = "ffffffff";

    private final Opcode opcode;
    private final String hexParameter;

    Instruction(@NonNull Opcode opcode, @NonNull String hexParameter) {
        this.opcode = opcode;
        this.hexParameter = hexParameter.toLowerCase();
    }

    public boolean hasOpcode(@NonNull Opcode opcode) {
        requireNonNull(opcode, "Expected not-null opcode");
        return this.opcode.equals(opcode);
    }

    public boolean hasHexParameter(@NonNull String hexParameter) {
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
