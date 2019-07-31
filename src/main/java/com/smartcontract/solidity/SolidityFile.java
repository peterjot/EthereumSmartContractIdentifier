package com.smartcontract.solidity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class SolidityFile {

    @Id
    private final String sourceCodeHash;
    private final String sourceCode;
    private final Set<SolidityFunction> solidityFunctions;
}
