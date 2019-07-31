package com.smartcontract.solidity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class SolidityFile {

    @Id
    @NonNull
    private String sourceCodeHash;

    @NonNull
    private String sourceCode;

    @NonNull
    private Set<SolidityFunction> solidityFunctions;
}
