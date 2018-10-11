package com.piotrjasina.contractidentifier;

public class OpcodeNotFoundException extends RuntimeException{
    public OpcodeNotFoundException(String message) {
        super(message);
    }
}
