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
    SolidityFileRepository solidityFileRepository;
    private SolidityService solidityService;

    @Before
    public void setUp() {
        solidityService = new SolidityService(solidityFileRepository);
    }


    @Test
    public void shouldGetFunctionsFromFile() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("10fdf92a", "commentFromAccount(uint256)"),
                new SolidityFunction("8ebb4c15", "comments(uint256)"),
                new SolidityFunction("cd65eabe", "commentsFromPost(uint256,uint256)"),
                new SolidityFunction("5a9cfac8", "hasPosts()"),
                new SolidityFunction("09787a2c", "newComment(uint256,string)"),
                new SolidityFunction("23bcaae9", "newPost(string)")
                ,
                new SolidityFunction("0b1e7f83", "posts(uint256)"),
                new SolidityFunction("acdc2cde", "postsFromAccount(address,uint256)")));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    public void shouldGetFunctionsFromFileWhenGeneratingGetterForMappingAndArray() throws Exception {
        //given
        Set<SolidityFunction> expectedSolidityFunctions = new HashSet<>(Arrays.asList(
                new SolidityFunction("e5265c8a", "mymap1(uint256,uint232)"),
                new SolidityFunction("f55f218e", "mymap2(uint256,uint256,uint256)"),
                new SolidityFunction("238f6a0d", "mymap3(uint256,uint256,uint256,uint256)"),
                new SolidityFunction("5732e7a9", "mymap4(uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256)")));

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test4MappingAndArrays.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions, equalTo(expectedSolidityFunctions));
    }

    @Test
    public void diffrentArrays() throws Exception {

        //when
        Set<SolidityFunction> actualSolidityFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test5Arrays.sol"))) {
            actualSolidityFunctions = solidityService.findSolidityFunctionsFromSourceFile(inputStream);
        }

        //then
        assertThat(actualSolidityFunctions.size(), equalTo(8));
    }
}
