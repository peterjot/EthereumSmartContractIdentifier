package com.piotrjasina.solidity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document
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
