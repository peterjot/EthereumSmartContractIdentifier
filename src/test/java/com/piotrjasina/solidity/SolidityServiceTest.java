package com.piotrjasina.solidity;

import com.piotrjasina.solidity.solidityfile.Function;
import com.piotrjasina.solidity.solidityfile.SolidityFileRepository;
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
        Set<Function> expectedFunctions = new HashSet<>(Arrays.asList(
                new Function("10fdf92a", "commentFromAccount(uint256)"),
                new Function("8ebb4c15", "comments(uint256)"),
                new Function("cd65eabe", "commentsFromPost(uint256)"),
                new Function("5a9cfac8", "hasPosts()"),
                new Function("09787a2c", "newComment(uint256,string)"),
                new Function("23bcaae9", "newPost(string)")
                ,
                new Function("0b1e7f83", "posts(uint256)"),
                new Function("acdc2cde", "postsFromAccount(address)")));

        //when
        Set<Function> actualFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            actualFunctions = solidityService.getFunctionsFromFile(inputStream);
        }

        //then
        assertThat(actualFunctions, equalTo(expectedFunctions));
    }

    @Test
    public void shouldGetFunctionsFromFileWhenGeneratingGetterForMapping() throws Exception {
        //given
        Set<Function> expectedFunctions = new HashSet<>(Arrays.asList(
                new Function("e5265c8a", "mymap1(uint256,uint232)"),
                new Function("f55f218e", "mymap2(uint256,uint256,uint256)"),
                new Function("238f6a0d", "mymap3(uint256,uint256,uint256,uint256)")));

        //when
        Set<Function> actualFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test4SimpleMapping.sol"))) {
            actualFunctions = solidityService.getFunctionsFromFile(inputStream);
        }

        //then
        assertThat(actualFunctions, equalTo(expectedFunctions));
    }

    @Test
    public void shouldGetFunctionsFromFileWhenGeneratingGetterForArray() throws Exception {
        //given
        Set<Function> expectedFunctions = new HashSet<>(Arrays.asList(
                new Function("e5265c8a", "mymap1(uint256,uint232)"),
                new Function("f55f218e", "mymap2(uint256,uint256,uint256)"),
                new Function("238f6a0d", "mymap3(uint256,uint256,uint256,uint256)")));

        //when
        Set<Function> actualFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test4SimpleMapping.sol"))) {
            actualFunctions = solidityService.getFunctionsFromFile(inputStream);
        }

        //then
        assertThat(actualFunctions, equalTo(expectedFunctions));
    }


}