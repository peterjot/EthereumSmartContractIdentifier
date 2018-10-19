package com.piotrjasina.web.controller;

import org.junit.Test;
import org.web3j.crypto.Hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
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

    @Test
    public void dsa() {
        String test = "string _text, uint asd,dsadsa   dsad,   dsad sa";
        String[] split = test.split(",");

        List<String> collect = Stream.of(split).map(String::trim).collect(toList());

        List<String> collect1 = collect.stream().map(s -> maper(s.replaceAll(" .*", ""))).collect(Collectors.toList());

        System.out.println(collect1);
    }


    public String maper(String from) {
        Map<String, String> mapuj = new HashMap<>();
        mapuj.put("uint", "uint256");


        String s = mapuj.get(from);
        if (s != null) {
            return s;
        }
        return from;
    }

}