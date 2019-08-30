package io.github.peterjot.bytecode;

import lombok.*;

import static java.util.Objects.requireNonNull;


@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class Instruction {

    static final String PUSH4_MASK = "ffffffff";

    @NonNull
    private final Opcode opcode;

    @NonNull
    private final String hexParameter;


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
}
