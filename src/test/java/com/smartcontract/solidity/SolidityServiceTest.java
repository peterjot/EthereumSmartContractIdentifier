package com.smartcontract.solidity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
public class SolidityServiceTest {

    @MockBean
    private SolidityFileRepository solidityFileRepository;
    private SolidityService solidityService;
    private SourceCodeParser sourceCodeParser = new SourceCodeParser();


    @Before
    public void setUp() {
        solidityService = new SolidityService(sourceCodeParser, solidityFileRepository);
    }

    @Test
    public void shouldFindFunctionsFromFile() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<SolidityFunction>() {{
            add(new SolidityFunction("10fdf92a", "commentFromAccount(uint256)"));
            add(new SolidityFunction("8ebb4c15", "comments(uint256)"));
            add(new SolidityFunction("cd65eabe", "commentsFromPost(uint256,uint256)"));
            add(new SolidityFunction("5a9cfac8", "hasPosts()"));
            add(new SolidityFunction("09787a2c", "newComment(uint256,string)"));
            add(new SolidityFunction("23bcaae9", "newPost(string)"));
            add(new SolidityFunction("0b1e7f83", "posts(uint256)"));
            add(new SolidityFunction("acdc2cde", "postsFromAccount(address,uint256)"));
        }};

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    public void shouldFindFunctionsWithoutInternalAndPrivateFunctions() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<SolidityFunction>() {{
            add(new SolidityFunction("52382794", "ODDS(uint256)"));
            add(new SolidityFunction("e210c049", "AMOUNTS(uint256)"));
            add(new SolidityFunction("753feadd", "MASKS(uint256)"));
            add(new SolidityFunction("c9e525df", "N()"));
            add(new SolidityFunction("68be9822", "OWNER_AMOUNT()"));
            add(new SolidityFunction("833afd43", "OWNER_MIN()"));
            add(new SolidityFunction("2e1371bd", "OWNER_PERCENT()"));
            add(new SolidityFunction("22b80ff8", "_cash()"));
            add(new SolidityFunction("f851a440", "admin()"));
            add(new SolidityFunction("793cd71e", "cashOut()"));
            add(new SolidityFunction("502d0c30", "firstBN()"));
            add(new SolidityFunction("1ccf6955", "getBets(uint256)"));
            add(new SolidityFunction("a2f77bcc", "getGame(uint256)"));
            add(new SolidityFunction("41c0e1b5", "kill()"));
            add(new SolidityFunction("60b79784", "lockedIn()"));
            add(new SolidityFunction("7206a199", "open(uint256,bytes32,uint256)"));
            add(new SolidityFunction("f74797d1", "open2(uint256,bytes32,bytes32,uint256)"));
            add(new SolidityFunction("f20eaeb8", "output()"));
            add(new SolidityFunction("8da5cb5b", "owner()"));
            add(new SolidityFunction("03edf914", "placeBet(uint256,uint8)"));
            add(new SolidityFunction("715018a6", "renounceOwnership()"));
            add(new SolidityFunction("704b6c02", "setAdmin(address)"));
            add(new SolidityFunction("8afc2d35", "setN(uint8)"));
            add(new SolidityFunction("31af8eb0", "setOwnerMin(uint256)"));
            add(new SolidityFunction("ac7e64d5", "setOwnerPercent(uint256)"));
            add(new SolidityFunction("f2fde38b", "transferOwnership(address)"));
            add(new SolidityFunction("3ccfd60b", "withdraw()"));
        }};

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test3.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }


    @Test
    public void shouldGetFunctionsFromFileWhenGeneratingGetterForMappingAndArray() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<SolidityFunction>() {{
            add(new SolidityFunction("e5265c8a", "mymap1(uint256,uint232)"));
            add(new SolidityFunction("f55f218e", "mymap2(uint256,uint256,uint256)"));
            add(new SolidityFunction("238f6a0d", "mymap3(uint256,uint256,uint256,uint256)"));
            add(new SolidityFunction("5732e7a9", "mymap4(uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256)"));
        }};

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test4MappingAndArrays.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    public void shouldFindFunctionsWithDiffrentArrayParameters() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<SolidityFunction>() {{
            add(new SolidityFunction("07e6cb8a", "mymap5(uint256,uint256,uint256)"));
            add(new SolidityFunction("8cd6cadb", "mymap7(uint256,uint256,uint256,uint256,uint256)"));
            add(new SolidityFunction("8ef0c5ff", "mymap10(uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256)"));
            add(new SolidityFunction("6a985a77", "mymap2(uint256)"));
            add(new SolidityFunction("e9244aac", "mymap1(uint256,uint256)"));
            add(new SolidityFunction("1d7fb8a3", "mymap8(uint256,uint256,uint256,uint256,uint256,uint256)"));
            add(new SolidityFunction("0402a851", "mymap6(uint256,uint256,uint256,uint256)"));
            add(new SolidityFunction("bd58e491", "mymap9(uint256,uint256,uint256,uint256,uint256,uint256,uint256)"));
        }};

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test5Arrays.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions.size(), equalTo(8));
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    public void shouldFindFunctionWithDifferentModifiers() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("3e3ee859", "NewQuestion(string,bytes32)"),
                new SolidityFunction("f50ab247", "StopGame()"),
                new SolidityFunction("3853682c", "Try(string)"),
                new SolidityFunction("59988dce", "newQuestioner(address)"),
                new SolidityFunction("3fad9ae0", "question()"),
                new SolidityFunction("fd26c460", "set_game(string,string)")
        ));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test6.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }
}
