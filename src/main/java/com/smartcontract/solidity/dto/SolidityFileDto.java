package com.smartcontract.solidity.dto;

import lombok.*;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class SolidityFileDto {

    @NonNull
    private final String sourceCodeHash;

    @NonNull
    private final String sourceCode;

    @NonNull
    private final Set<SolidityFunctionDto> solidityFunctions;
}
