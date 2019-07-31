package com.smartcontract.solidity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class SolidityFunction {

    @NonNull
    private String selector;

    @NonNull
    private String signature;
}
