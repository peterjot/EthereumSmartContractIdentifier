package com.piotrjasina.solidity.function;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Function {

    @Id
    @NonNull
    private String selector;

    @Indexed(unique = true)
    @NonNull
    private String signature;
}
