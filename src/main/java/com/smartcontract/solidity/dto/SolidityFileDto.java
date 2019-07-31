package com.smartcontract.solidity.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
public class SolidityFileDto {

    @Id
    private final String sourceCodeHash;
    private final String sourceCode;
    private final Set<com.smartcontract.solidity.dto.SolidityFunctionDto> solidityFunctions;


}
