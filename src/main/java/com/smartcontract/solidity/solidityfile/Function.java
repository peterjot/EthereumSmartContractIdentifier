package com.smartcontract.solidity.solidityfile;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Function {

    @NonNull
    private String selector;

    @NonNull
    private String signature;
}