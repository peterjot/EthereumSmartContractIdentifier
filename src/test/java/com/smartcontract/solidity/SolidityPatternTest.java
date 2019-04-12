package com.smartcontract.solidity;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class SolidityPatternTest {


    @Test
    public void funSig() {
        final Matcher matcher = SolidityPattern.FUNCTION_PATTERN.matcher("    function aggregate(bytes memory data) public view returns (bytes memory) {");

        System.out.println(matcher.find());
    }
}