package com.piotrjasina.solidity;

import com.piotrjasina.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.piotrjasina.Utils.stringHash;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolidityFileRepositoryTest {

    @Autowired
    private SolidityFileRepository solidityFileRepository;
    @Autowired
    private FunctionRepository functionRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final Function MOCK_FUNCTION_1 = new Function("dsap", "dsa");
    private static final Function MOCK_FUNCTION_2 = new Function("dsa", "dsddda");

    @Before
    public void setUp() throws Exception {
        String testSourceCode = getTestSourceCode();
        String testSourceCodeHash = stringHash(testSourceCode);

        String testSourceCode2 = testSourceCode.replaceAll("a","b");
        String testSourceCodeHash2 = stringHash(testSourceCode2);

        functionRepository.save(MOCK_FUNCTION_1);
        functionRepository.save(MOCK_FUNCTION_2);

        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash,
                testSourceCode,
                new HashSet<>(asList(MOCK_FUNCTION_1, MOCK_FUNCTION_2))));

        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash2,
                testSourceCode2,
                new HashSet<>(asList(MOCK_FUNCTION_1, MOCK_FUNCTION_2))));


    }

    @After
    public void tearDown() {
        solidityFileRepository.deleteAll();
        functionRepository.deleteAll();

    }

    @Test
    public void shouldSaveSourceCode() throws Exception {
        //given
        String expectedSourceCode = "dsadsadsadadklujsadoisajfsdkljgdfkl";
        String expectedSourceCodeHash = stringHash(expectedSourceCode);

        Function mockFunction1 = new Function("dsap", "dsa");
        Function mockFunction2 = new Function("dsa", "dssa");

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
        Function function = new Function("dsap", "dsa");
        int expectedSolidityFilesSize = 2;

        //when
        List<SolidityFile> actualSolidityFiles = solidityFileRepository.findByFunctionsIsContaining(function);

        //then
        assertThat(actualSolidityFiles.size(), equalTo(expectedSolidityFilesSize));
    }

    private String getTestSourceCode() throws Exception {
        try (InputStream inputStream = new FileInputStream(new File("src/test/resources/Test.sol"))) {
            return Utils.convertToString(inputStream);
        }

    }
}