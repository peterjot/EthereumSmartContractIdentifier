package com.piotrjasina.solidity;

import com.piotrjasina.solidity.function.Function;
import com.piotrjasina.solidity.function.FunctionRepository;
import com.piotrjasina.solidity.solidityfile.SolidityFileRepository;
import org.junit.Before;
import org.junit.Ignore;
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

    private SolidityService solidityService;

    @MockBean
    SolidityFileRepository solidityFileRepository;
    @MockBean
    FunctionRepository functionRepository;

    @Before
    public void setUp() {
        solidityService = new SolidityService(solidityFileRepository, functionRepository);
    }


    @Test
    @Ignore
    public void shouldGetFunctionsFromFile() throws Exception {
        //given
        Set<Function> expectedFunctions;

        //when
        Set<Function> actualFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            expectedFunctions = new HashSet<>(Arrays.asList(
                    new Function("10fdf92a", "commentFromAccount(uint256)"),
                    new Function("8ebb4c15", "comments(uint256)"),
                    new Function("cd65eabe", "commentsFromPost(uint256,uint256)"),
                    new Function("5a9cfac8", "hasPosts()"),
                    new Function("09787a2c", "newComment(uint256,string)"),
                    new Function("23bcaae9", "newPost(string)")
                    ,
                    new Function("0b1e7f83", "posts(uint256)"),
                    new Function("acdc2cde", "postsFromAccount(address,uint256)")
            ));

            actualFunctions = solidityService.getFunctionsFromFile(inputStream);
        }

        //then
        assertThat(actualFunctions, equalTo(expectedFunctions));
    }
}