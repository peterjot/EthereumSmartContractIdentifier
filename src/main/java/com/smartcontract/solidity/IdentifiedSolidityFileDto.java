package com.smartcontract.solidity;

public class IdentifiedSolidityFileDto {

    private final String fileHash;
    private final Double valueOfMatch;


    public IdentifiedSolidityFileDto(String fileHash, Double valueOfMatch) {
        this.fileHash = fileHash;
        this.valueOfMatch = valueOfMatch;
    }

    public IdentifiedSolidityFileDto(String fileHash) {
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
