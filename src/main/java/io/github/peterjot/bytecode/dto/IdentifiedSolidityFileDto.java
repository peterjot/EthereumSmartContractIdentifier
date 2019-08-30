package io.github.peterjot.bytecode.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class IdentifiedSolidityFileDto {

    @NonNull
    private final String fileHash;

    private final double valueOfMatch;


    public IdentifiedSolidityFileDto(@NonNull String fileHash) {
        this.fileHash = fileHash;
        this.valueOfMatch = 0D;
    }

    public String getFileHash() {
        return fileHash;
    }

    public Double getValueOfMatch() {
        return valueOfMatch;
    }
}
