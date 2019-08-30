package io.github.peterjot.solidity;

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
    private final String sourceCodeHash;

    @NonNull
    private final String sourceCode;

    @NonNull
    private final Set<SolidityFunction> solidityFunctions;
}
