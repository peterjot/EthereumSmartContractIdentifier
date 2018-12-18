package com.smartcontract;

import org.web3j.crypto.Hash;

public class Utils {



    private Utils() {
        throw new AssertionError();
    }

    public static String stringHash(String sourceCode) {
        return Hash.sha3String(sourceCode);
    }
}
