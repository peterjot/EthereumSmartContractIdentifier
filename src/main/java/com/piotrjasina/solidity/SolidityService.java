package com.piotrjasina.solidity;

import com.piotrjasina.solidity.solidityfile.SolidityFile;
import com.piotrjasina.solidity.solidityfile.SolidityFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.piotrjasina.Utils.stringHash;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.web3j.crypto.Hash.sha3String;

@Service
@Slf4j
public class SolidityService {
    //TODO Add fuction selectors for getter solidityFileFunctionSelectors
    //TODO Poprawic dopasowanie wynikow dla listy selektorow funkcji
    //TODO czasami zdarza sie sytuacja
    //                                     -xxxxxxxxx-- function name        --xxx--fun args--xxx--xxx
    private static final String pattern = "(function\\s+)([a-zA-Z_][a-zA-Z0-9_]*)(\\s*\\(\\s*)([^(){}]*)(\\s*\\)\\s*)(.*)";

    private final SolidityFileRepository solidityFileRepository;

    @Autowired
    public SolidityService(SolidityFileRepository solidityFileRepository) {
        checkNotNull(solidityFileRepository, "Expected not-null solidityFileRepository");
        this.solidityFileRepository = solidityFileRepository;
    }

    public String getSourceCodeByHash(String fileHash) {
        if (!fileHash.startsWith("0x"))
            fileHash = "0x" + fileHash;

        Optional<SolidityFile> solidityFile = solidityFileRepository.findBySourceCodeHash(fileHash);

        if (solidityFile.isPresent()) {
            return solidityFile.get().getSourceCode();
        }
        throw new RuntimeException("No source code");
    }

    public List<SolidityFile> findAllFiles() {
        return solidityFileRepository.findAll();
    }

    public long getUniqueFunctionSelectorsCount(){
        return findAllUniqueFunctionSelectors().size();
    }

    public long getSolidityFilesCount(){
        return solidityFileRepository.count();
    }

    public List<String> findAllUniqueFunctionSelectors() {
        return solidityFileRepository
                .findAll()
                .stream()
                .map(SolidityFile::getFunctionSelectors)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

    }

    public SolidityFile save(String sourceCode) throws IOException {
        return save(sourceCode.getBytes());
    }

    SolidityFile save(byte[] sourceCodeBytes) throws IOException {

        String sourceCode = new String(sourceCodeBytes, StandardCharsets.UTF_8);
        String sourceCodeHash = stringHash(sourceCode);

        log.info("SourceCode hash: [{}]", sourceCodeHash);

        Set<String> functionsFromFile = getFunctionsFromFile(new ByteArrayInputStream(sourceCodeBytes));
        log.info("SourceCode functios count: {}", functionsFromFile.size());

        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }


    public Set<String> getFunctionsFromFile(InputStream inputStream) throws IOException {
        checkNotNull(inputStream, "Expected not-null inputStream");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        Pattern pattern = Pattern.compile(SolidityService.pattern);

        Set<String> functions = new HashSet<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {

                String functionName = matcher.group(2);
                String functionArgs = matcher.group(4);

                String normalizedFunctionSignature = normalizeFunctionSignature(functionName, functionArgs);

                String functionSelector = getFunctionSelector(normalizedFunctionSignature);
                log.info("Function selector: {}", functionSelector);

                functions.add(functionSelector);
            }
        }

        return functions;
    }

    private String normalizeFunctionSignature(String functionName, String functionArgumentsString) {
        String[] functionArguments = functionArgumentsString.trim().split("\\s*,\\s*");

        List<String> functionArgumentsList = asList(functionArguments);

        String normalizedArguments =
                functionArgumentsList
                        .stream()
                        .map(s -> toCanonicalType(getFirstWord(s)))
                        .collect(joining(","));

        String join = functionName.trim() + "(" + normalizedArguments + ")";
        log.info(("Function signature(normalized): [{}]"), join);

        return join;
    }

    private String getFirstWord(String s) {
        return s.replaceAll(" .*", "");
    }

    private String getFunctionSelector(String normalizedFunctionSignature) {
        return sha3String(normalizedFunctionSignature).substring(2, 10);
    }

    private String toCanonicalType(String from) {
        Map<String, String> canonicalTypes = new HashMap<>();
        canonicalTypes.put("uint", "uint256");
        canonicalTypes.put("int", "int256");
        canonicalTypes.put("byte", "bytes1");


        String s = canonicalTypes.get(from);
        if (s != null) {
            return s;
        }
        return from;
    }
}