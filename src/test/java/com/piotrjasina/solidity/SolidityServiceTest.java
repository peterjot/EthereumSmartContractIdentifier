package com.piotrjasina.solidity;

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

    @Before
    public void setUp() {
        solidityService = new SolidityService(solidityFileRepository);
    }


    @Test
    @Ignore
    public void shouldGetFunctionsFromFile() throws Exception {
        //given
        Set<String> expectedFunctions = new HashSet<>(Arrays.asList(
                "10fdf92a", "8ebb4c15", "cd65eabe", "5a9cfac8", "09787a2c", "23bcaae9", "0b1e7f83", "acdc2cde"));

        //when
        Set<String> actualFunctions;
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            actualFunctions = solidityService.getFunctionsFromFile(inputStream);
        }

        //then
        assertThat(actualFunctions, equalTo(expectedFunctions));
    }
}