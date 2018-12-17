package com.piotrjasina.solidity.solidityfile;

import lombok.*;
import org.springframework.data.annotation.Id;

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
    private Set<String> functionSelectors;

}
