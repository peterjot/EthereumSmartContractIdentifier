package com.piotrjasina.dto;

import com.piotrjasina.disassembler.Opcode;
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
