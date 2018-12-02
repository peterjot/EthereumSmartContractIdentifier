package com.piotrjasina.solidity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Document
public class Function {

    @NonNull
    @Id
    private String selector;

    @NonNull
    private String signature;

}
