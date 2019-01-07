package com.smartcontract.solidity;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SolidityParserTest {

    private final SolidityParser solidityParser = new SolidityParser();

    @Test
    public void findMappingGetter() {
        //given
        String sourceCodeLine = "mapping(int=>mapping(int=>string)) public test;";
        String selector = "24d45ec3";

        //when
        Optional<SolidityFunction> actualFunction = solidityParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
        System.out.println(actualFunction);
    }

    @Test
    public void findArray1Getter() {
        //given
        String sourceCodeLine = "uint256[][][] public test;";
        String selector = "61805cc3";

        //when
        Optional<SolidityFunction> actualFunction = solidityParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
        System.out.println(actualFunction);
    }

    @Test
    public void findArray2Getter() {
        //given
        String sourceCodeLine = "uint256[ ][31][] public test;";
        String selector = "61805cc3";

        //when
        Optional<SolidityFunction> actualFunction = solidityParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
        System.out.println(actualFunction);
    }

    @Test
    public void findArray3Getter() {
        //given
        String sourceCodeLine = "uint256[1][][1] public test;";
        String selector = "61805cc3";

        //when
        Optional<SolidityFunction> actualFunction = solidityParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
        System.out.println(actualFunction);
    }
}