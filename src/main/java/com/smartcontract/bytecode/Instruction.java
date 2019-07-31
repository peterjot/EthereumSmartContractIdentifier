package com.smartcontract.bytecode;

import lombok.NonNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


class Instruction {

    static final String PUSH4_MASK = "ffffffff";

    private final Opcode opcode;
    private final String hexParameter;

    Instruction(@NonNull Opcode opcode, @NonNull String hexParameter) {
        this.opcode = opcode;
        this.hexParameter = hexParameter.toLowerCase();
    }

    boolean hasOpcode(@NonNull Opcode opcode) {
        requireNonNull(opcode, "Expected not-null opcode");
        return this.opcode.equals(opcode);
    }

    boolean hasHexParameter(@NonNull String hexParameter) {
        requireNonNull(hexParameter, "Expceted not-null hexParameter");
        return this.hexParameter.equals(hexParameter);
    }

    String getHexParameter() {
        return hexParameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, hexParameter);
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
    public String toString() {
        return "Instruction{" +
                "opcode=" + opcode +
                ", hexParameter='" + hexParameter + '\'' +
                '}';
    }
}
