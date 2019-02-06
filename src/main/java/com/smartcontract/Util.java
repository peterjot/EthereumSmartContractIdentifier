package com.smartcontract;

import org.web3j.crypto.Hash;

public class Util {

    private Util() {
        throw new UnsupportedOperationException();
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) throw new NullPointerException(message);
        return value;
    }

    public static String sha3Hash(String value) {
        return Hash.sha3String(value);
    }
}
