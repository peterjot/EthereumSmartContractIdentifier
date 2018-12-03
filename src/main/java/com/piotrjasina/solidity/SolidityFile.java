package com.piotrjasina.solidity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SolidityFile {

    @NonNull
    @Id
    private String sourceCodeHash;

    @NonNull
    private String sourceCode;

    @NonNull
    @DBRef
    @EqualsAndHashCode.Exclude
    private Set<Function> functions;

}
