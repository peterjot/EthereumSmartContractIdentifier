package com.piotrjasina.web.controller;

import org.junit.Test;
import org.web3j.crypto.Hash;

import static org.junit.Assert.assertEquals;

public class SolidityControllerTest {


//    @Test
//    public void name() {
//        boolean matches = Pattern.matches("function \\w*\\(.+\\).*", "function newComment(uint _postId, string _text) public {");
//        System.out.println(matches);
//    }


    @Test
    public void name() {
        String s = Hash.sha3String("hasPosts()");
        System.out.println(s);

        assertEquals("0x5a9cfac802684d92eab014033d01c96f7a3e5d2dd3b7b7a8869290c4f9adb673", s);
    }
}