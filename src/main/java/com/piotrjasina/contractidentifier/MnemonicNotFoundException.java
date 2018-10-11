package com.piotrjasina.contractidentifier;

public class MnemonicNotFoundException extends RuntimeException {
    public MnemonicNotFoundException(String message) {
        super(message);
    }
}
