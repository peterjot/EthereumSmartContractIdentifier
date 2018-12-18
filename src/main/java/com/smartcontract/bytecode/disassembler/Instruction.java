package com.smartcontract.bytecode.disassembler;

import lombok.*;

import static com.google.common.base.Preconditions.checkNotNull;

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
