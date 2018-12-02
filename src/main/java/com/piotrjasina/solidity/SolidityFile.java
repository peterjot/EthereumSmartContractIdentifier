package com.piotrjasina.solidity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Id
    private String id;

    @NonNull
    private String sourceCode;

    @NonNull
    @Indexed(unique = true)
    private String sourceCodeHash;

    @NonNull
    private Set<Function> functions;

}
