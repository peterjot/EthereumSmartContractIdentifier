package com.smartcontract.solidity.dto;

import lombok.Data;

@Data
public class SolidityFunctionDto {
    private final String selector;
    private final String signature;
}