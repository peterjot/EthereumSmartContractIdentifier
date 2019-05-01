package com.smartcontract.solidity;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SourceCodeParserTest {

    private final SourceCodeParser sourceCodeParser = new SourceCodeParser();


    @Test
    public void shouldFindMappingGetter() {
        //given
        String sourceCodeLine = "mapping(int=>mapping(int=>string)) public test;";
        String selector = "24d45ec3";

        //when
        Optional<SolidityFunction> actualFunction = sourceCodeParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
    }

    @Test
    public void shouldFindArray1Getter() {
        //given
        String sourceCodeLine = "uint256[][][] public test;";
        String selector = "61805cc3";

        //when
        Optional<SolidityFunction> actualFunction = sourceCodeParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
    }

    @Test
    public void shouldFindArray2Getter() {
        //given
        String sourceCodeLine = "uint256[ ][31][] public test;";
        String selector = "61805cc3";

        //when
        Optional<SolidityFunction> actualFunction = sourceCodeParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
    }

    @Test
    public void shouldFindArray3Getter() {
        //given
        String sourceCodeLine = "uint256[1][][1] public test;";
        String selector = "61805cc3";

        //when
        Optional<SolidityFunction> actualFunction = sourceCodeParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction.get().getSelector(), equalTo(selector));
    }

    @Test
    public void testArrayWithInitialization() {
        //given
        String sourceCodeLine = "\n" +
                "    uint16[M] public ODDS = [0, 600, 300, 200, 150, 120];";
        Optional<SolidityFunction> expectedFunction = Optional.of(new SolidityFunction("52382794", "ODDS(uint256)"));

        //when
        Optional<SolidityFunction> actualFunction = sourceCodeParser.findFunctionInLine(sourceCodeLine);

        //then
        assertTrue(actualFunction.isPresent());
        assertThat(actualFunction, equalTo(expectedFunction));
    }
}
