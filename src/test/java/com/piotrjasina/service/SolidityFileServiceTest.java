package com.piotrjasina.service;

import com.piotrjasina.dto.MethodDto;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


public class SolidityFileServiceTest {

    private SolidityFileService solidityFileService;

    @Before
    public void setUp() {
        solidityFileService = new SolidityFileService();
    }


    @Test
    public void getMethodsFromFile() throws Exception {
        //given
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File("src/test/resources/Test.sol")));
        List<MethodDto> expectedMethodDtos = Arrays.asList(
                new MethodDto("hasPosts()", "5a9cfac8"),
                new MethodDto("newPost(string)", "23bcaae9"),
                new MethodDto("newComment(uint256,string)", "09787a2c")
        );

        //when
        List<MethodDto> actualMethodDtosFromFile = solidityFileService.getMethodsFromFile(inputStreamReader);
        System.out.println(actualMethodDtosFromFile);

        //then
        assertThat(actualMethodDtosFromFile, equalTo(expectedMethodDtos));
    }
}