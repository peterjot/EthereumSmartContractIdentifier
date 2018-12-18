package com.piotrjasina.solidity.solidityfile;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Function {

    @Indexed(unique = true)
    @NonNull
    private String selector;

    @Indexed(unique = true)
    @NonNull
    private String signature;
}