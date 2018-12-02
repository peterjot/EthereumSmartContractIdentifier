package com.piotrjasina.bytecode;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class InstructionDto {
    private Opcode opcode;
    private String argument;
}
