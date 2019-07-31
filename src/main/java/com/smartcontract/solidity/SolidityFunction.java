package com.smartcontract.solidity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class SolidityFunction {

    private final String selector;
    private final String signature;
}

