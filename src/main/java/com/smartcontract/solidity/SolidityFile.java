package com.smartcontract.solidity;

import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.Objects;
import java.util.Set;

public class SolidityFile {

    @Id
    private final String sourceCodeHash;
    private final String sourceCode;
    private final Set<SolidityFunction> solidityFunctions;

    SolidityFile(@NonNull String sourceCodeHash,
                 @NonNull String sourceCode,
                 @NonNull Set<SolidityFunction> solidityFunctions) {
        this.sourceCodeHash = sourceCodeHash;
        this.sourceCode = sourceCode;
        this.solidityFunctions = solidityFunctions;
    }

    public String getSourceCodeHash() {
        return sourceCodeHash;
    }

    public Set<SolidityFunction> getSolidityFunctions() {
        return solidityFunctions;
    }

    @Override
    public String toString() {
        return "SolidityFile{" +
                "sourceCodeHash='" + sourceCodeHash + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", solidityFunctions=" + solidityFunctions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolidityFile)) return false;
        SolidityFile that = (SolidityFile) o;
        return Objects.equals(sourceCodeHash, that.sourceCodeHash) &&
                Objects.equals(sourceCode, that.sourceCode) &&
                Objects.equals(solidityFunctions, that.solidityFunctions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCodeHash, sourceCode, solidityFunctions);
    }

    String getSourceCode() {
        return sourceCode;
    }
}
