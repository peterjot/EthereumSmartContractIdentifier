package com.piotrjasina.service;

import com.piotrjasina.solidity.Function;
import com.piotrjasina.solidity.SolidityFileRepository;
import com.piotrjasina.solidity.SolidityFileService;
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
public class SolidityFileServiceTest {

    private SolidityFileService solidityFileService;

    @MockBean
    SolidityFileRepository solidityFileRepository;

    @Before
    public void setUp() {
        solidityFileService = new SolidityFileService(solidityFileRepository);
    }


    @Test
    public void shouldGetFunctionsFromFile() throws Exception {
        //given
        Set<Function> expectedFunctions;

        //when
        Set<Function> actualFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            expectedFunctions = new HashSet<>(Arrays.asList(
                    new Function("hasPosts()", "5a9cfac8"),
                    new Function("newPost(string)", "23bcaae9"),
                    new Function("newComment(uint256,string)", "09787a2c")
            ));

            actualFunctions = solidityFileService.getFunctionsFromFile(inputStream);
        }

        //then
        assertThat(actualFunctions, equalTo(expectedFunctions));
    }
}