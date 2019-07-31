package com.smartcontract.solidity.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class SolidityFunctionDto {

    @NonNull
    private final String selector;

    @NonNull
    private final String signature;
}
