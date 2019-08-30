package io.github.peterjot.solidity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.web3j.crypto.Hash.sha3String;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolidityFileRepositoryTest {

    private static final SolidityFunction MOCK_SOLIDITY_FUNCTION_1 = new SolidityFunction("dsap", "dsappp");
    private static final SolidityFunction MOCK_SOLIDITY_FUNCTION_2 = new SolidityFunction("dsap", "321appp");
    private static final SolidityFunction MOCK_SOLIDITY_FUNCTION_3 = new SolidityFunction("ffffff", "tttt");
    private static final Charset CHARSET = Charset.defaultCharset();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private SolidityFileRepository solidityFileRepository;


    @Before
    public void setUp() throws Exception {
        solidityFileRepository.deleteAll();

        String testSourceCode = getTestSourceCode();
        String testSourceCodeHash = sha3String(testSourceCode);

        String testSourceCode2 = testSourceCode + "FFFFFFFFFFF";
        String testSourceCodeHash2 = sha3String(testSourceCode2);

        String testSourceCode3 = testSourceCode + "GGGGGGGGGGGG";
        String testSourceCodeHash3 = sha3String(testSourceCode3);


        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash,
                testSourceCode,
                new HashSet<>(asList(MOCK_SOLIDITY_FUNCTION_1, MOCK_SOLIDITY_FUNCTION_2))));

        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash2,
                testSourceCode2,
                new HashSet<>(asList(MOCK_SOLIDITY_FUNCTION_2, MOCK_SOLIDITY_FUNCTION_2))));

        solidityFileRepository.save(new SolidityFile(
                testSourceCodeHash3,
                testSourceCode3,
                new HashSet<>(singletonList(MOCK_SOLIDITY_FUNCTION_3))));
    }

    @Test
    public void shouldSaveSourceCode() {
        //given
        String expectedSourceCode = "dsadsadsadadklujsadoisajfsdkljgdfkl";
        String expectedSourceCodeHash = sha3String(expectedSourceCode);

        SolidityFunction mockSolidityFunction1 = new SolidityFunction("dsap", "dsappp");
        SolidityFunction mockSolidityFunction2 = new SolidityFunction("dsa", "321appp");

        HashSet<SolidityFunction> expectedSolidityFunctions = new HashSet<>(asList(mockSolidityFunction1, mockSolidityFunction2));

        SolidityFile solidityFile = new SolidityFile(
                expectedSourceCodeHash,
                expectedSourceCode,
                expectedSolidityFunctions);

        //when
        SolidityFile actualSolidityFile = solidityFileRepository.save(solidityFile);

        //then
        assertThat(actualSolidityFile.getSourceCode(), equalTo(expectedSourceCode));
        assertThat(actualSolidityFile.getSourceCodeHash(), equalTo(expectedSourceCodeHash));
        assertThat(actualSolidityFile.getSolidityFunctions(), equalTo(expectedSolidityFunctions));
    }

    @Test
    public void shouldFinAllContains() {
        //given
        String expectedSourceCode = "dsadsadsadadklujsadoisajfsdkljgdfkl";
        String expectedSourceCodeHash = sha3String(expectedSourceCode);

        SolidityFunction mockSolidityFunction1 = new SolidityFunction("dsap", "dsappp");
        SolidityFunction mockSolidityFunction2 = new SolidityFunction("dsa", "321appp");

        HashSet<SolidityFunction> expectedSolidityFunctions = new HashSet<>(asList(mockSolidityFunction1, mockSolidityFunction2));

        SolidityFile solidityFile = new SolidityFile(
                expectedSourceCodeHash,
                expectedSourceCode,
                expectedSolidityFunctions
        );
        solidityFileRepository.save(solidityFile);
        List<String> functionSelector = new ArrayList<>() {{
            add("dsap");
        }};

        //when
        final List<SolidityFile> dsap = solidityFileRepository.findSolidityFilesBySelectorContains(
                functionSelector);

        //then
        assertThat(dsap.size(), Matchers.greaterThan(0));
    }

    private String getTestSourceCode() throws Exception {
        return Files.readString(Paths.get("src/test/resources/Test.sol"));
    }
}