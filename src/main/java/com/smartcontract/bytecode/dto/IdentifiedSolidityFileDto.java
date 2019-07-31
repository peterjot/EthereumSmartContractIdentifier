package com.smartcontract.bytecode.dto;

import lombok.NonNull;

public class IdentifiedSolidityFileDto {

    private final String fileHash;
    private final Double valueOfMatch;


    public IdentifiedSolidityFileDto(@NonNull String fileHash, @NonNull Double valueOfMatch) {
        this.fileHash = fileHash;
        this.valueOfMatch = valueOfMatch;
    }

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
