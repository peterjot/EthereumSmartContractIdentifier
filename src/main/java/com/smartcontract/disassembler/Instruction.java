package com.smartcontract.disassembler;

import lombok.*;

import static lombok.Lombok.checkNotNull;


@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Instruction {

    @NonNull
    private Opcode opcode;

    @NonNull
    private String hexParameters;

    public boolean hasMnemonic(String mnemonic) {
        checkNotNull(mnemonic, "Expected not-null mnemonic");
        return opcode.hasMnemonic(mnemonic);
    }
}
