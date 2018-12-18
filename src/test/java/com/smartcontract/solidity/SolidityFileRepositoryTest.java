package com.smartcontract.solidity;

import com.smartcontract.solidity.solidityfile.Function;
import com.smartcontract.solidity.solidityfile.SolidityFile;
import com.smartcontract.solidity.solidityfile.SolidityFileRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.smartcontract.Utils.stringHash;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolidityFileRepositoryTest {

    private static final Function MOCK_FUNCTION_1 = new Function("dsap", "dsappp");
    private static final Function MOCK_FUNCTION_2 = new Function("dsap", "321appp");
    private static final Function MOCK_FUNCTION_3 = new Function("ffffff", "tttt");

    private static final Charset CHARSET = Charset.defaultCharset();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private SolidityFileRepository solidityFileRepository;

    @Before
    public void setUp() throws Exception {
        String testSourceCode = getTestSourceCode();
        String testSourceCodeHash = stringHash(testSourceCode);

        String testSourceCode2 = testSourceCode + "FFFFFFFFFFF";
        String testSourceCodeHash2 = stringHash(testSourceCode2);

        String testSourceCode3 = testSourceCode + "GGGGGGGGGGGG";
        String testSourceCodeHash3 = stringHash(testSourceCode3);


        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash,
                testSourceCode,
                new HashSet<>(asList(MOCK_FUNCTION_1, MOCK_FUNCTION_2))));

        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash2,
                testSourceCode2,
                new HashSet<>(asList(MOCK_FUNCTION_2, MOCK_FUNCTION_2))));

        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash3,
                testSourceCode3,
                new HashSet<>(singletonList(MOCK_FUNCTION_3))));


    }

    @After
    public void tearDown() {
        solidityFileRepository.deleteAll();

    }

    @Test
    public void shouldSaveSourceCode() {
        //given
        String expectedSourceCode = "dsadsadsadadklujsadoisajfsdkljgdfkl";
        String expectedSourceCodeHash = stringHash(expectedSourceCode);

        Function mockFunction1 = new Function("dsap", "dsappp");
        Function mockFunction2 = new Function("dsa", "321appp");

        HashSet<Function> expectedFunctions = new HashSet<>(asList(mockFunction1, mockFunction2));

        SolidityFile solidityFile = new SolidityFile(
                expectedSourceCodeHash,
                expectedSourceCode,
                expectedFunctions);

        //when
        SolidityFile actualSolidityFile = solidityFileRepository.save(solidityFile);


        //then
        assertThat(actualSolidityFile.getSourceCode(), equalTo(expectedSourceCode));
        assertThat(actualSolidityFile.getSourceCodeHash(), equalTo(expectedSourceCodeHash));
        assertThat(actualSolidityFile.getFunctions(), equalTo(expectedFunctions));
    }


    @Test
    public void shouldFindAllSourceCodesContainingFunction() {
        //given
        String functionSelector = "dsap";
        int expectedSolidityFilesSize = 2;

        //when
        List<SolidityFile> actualSolidityFiles = solidityFileRepository.findByFunctionSelectorsIsContaining(functionSelector);

        //then
        assertThat(actualSolidityFiles.size(), equalTo(expectedSolidityFilesSize));
    }

    private String getTestSourceCode() throws Exception {
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            return convertToString(inputStream);
        }

    }

    public static String convertToString(InputStream inputStream) throws IOException {
        checkNotNull(inputStream, "Expected non-null inputStream");

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CHARSET))) {
            return bufferedReader
                    .lines()
                    .collect(joining(lineSeparator()));
        }
    }
}