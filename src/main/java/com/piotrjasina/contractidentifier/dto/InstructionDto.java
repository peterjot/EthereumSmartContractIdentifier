package com.piotrjasina.contractidentifier.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class InstructionDto {
    private String mnemonic;
    private String argument;
    private String unit8;
}
