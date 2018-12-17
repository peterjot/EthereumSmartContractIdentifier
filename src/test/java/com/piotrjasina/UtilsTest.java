package com.piotrjasina;

import org.junit.Test;
import org.web3j.crypto.Hash;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void stringHash() {
        System.out.println(Hash.sha3String("testtest"));
    }
}