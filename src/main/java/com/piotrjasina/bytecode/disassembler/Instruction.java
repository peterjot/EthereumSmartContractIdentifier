package com.piotrjasina.bytecode.disassembler;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Instruction {

    @NonNull
    private Opcode opcode;
    @NonNull
    private String hexArgument;

    public boolean hasMnemonic(String mnemonic) {
        return opcode.name().equals(mnemonic);
    }

}
