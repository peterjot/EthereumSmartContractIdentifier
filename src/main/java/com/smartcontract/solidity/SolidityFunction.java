package com.smartcontract.solidity;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SolidityFunction {

    @NonNull
    private String selector;

    @NonNull
    private String signature;
}
