package io.github.peterjot.solidity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class SolidityFunction {

    @NonNull
    private final String selector;

    @NonNull
    private final String signature;
}
