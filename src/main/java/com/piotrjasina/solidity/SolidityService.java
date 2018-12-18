package com.piotrjasina.solidity;

import com.piotrjasina.solidity.solidityfile.Function;
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.piotrjasina.Utils.stringHash;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.web3j.crypto.Hash.sha3String;

@Service
@Slf4j
public class SolidityService {
    //TODO Add fuction selectors for getter solidityFileFunctionSelectors
    //TODO Poprawic dopasowanie wynikow dla listy selektorow funkcji
    //TODO czasami zdarza sie sytuacja
    //                                     -xxxxxxxxx-- function name        --xxx--fun args--xxx--xxx
    private static final String PATTERN_FUNCTION_VALUE = "(function\\s*)([a-zA-Z_][a-zA-Z0-9_]*)(\\s*\\(\\s*)([^(){}]*)(\\s*\\)\\s*)(.*)";
    private static final Pattern PATTERN_FUNCTION = Pattern.compile(PATTERN_FUNCTION_VALUE);

    private static final String PATTERN_MAPPING_VARIABLE_VALUE = "^\\s*mapping\\s*\\(\\s*([a-zA-Z][a-zA-Z]*)\\s*=>\\s*(.*)\\s*\\)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*$";
    private static final Pattern PATTERN_MAPPING_VARIABLE = Pattern.compile(PATTERN_MAPPING_VARIABLE_VALUE);

    private static final String PATTERN_MAPPING_VALUE = "^\\s*mapping\\s*\\(\\s*([a-zA-Z0-9][a-zA-Z0-9]*)\\s*=>\\s*(.*)\\s*\\)\\s*";
    private static final Pattern PATTERN_MAPPING = Pattern.compile(PATTERN_MAPPING_VALUE);

    private static final String PATTERN_ARRAY_VALUE = "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*(\\s*\\[\\s*[0-9]*\\s*]\\s*)+";
    private static final Pattern PATTERN_ARRAY = Pattern.compile(PATTERN_ARRAY_VALUE);


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


    public long getSolidityFilesCount() {
        return solidityFileRepository.count();
    }

    public List<Function> findAllUniqueFunctions() {
        return solidityFileRepository
                .findAll()
                .stream()
                .map(SolidityFile::getFunctions)
                .flatMap(Collection::stream)
                .distinct()
                .collect(toList());
    }

    public SolidityFile save(String sourceCode) throws IOException {
        return save(sourceCode.getBytes());
    }

    SolidityFile save(byte[] sourceCodeBytes) throws IOException {

        String sourceCode = new String(sourceCodeBytes, StandardCharsets.UTF_8);
        String sourceCodeHash = stringHash(sourceCode);

        log.info("SourceCode hash: [{}]", sourceCodeHash);

        Set<Function> functionsFromFile = getFunctionsFromFile(new ByteArrayInputStream(sourceCodeBytes));
        log.info("SourceCode functios count: {}", functionsFromFile.size());

        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }


    public Set<Function> getFunctionsFromFile(InputStream inputStream) throws IOException {
        checkNotNull(inputStream, "Expected not-null inputStream");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        Set<Function> functions = new HashSet<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Optional<Function> function = findFunction(line);
            if (function.isPresent()) {
                functions.add(function.get());
            } else {
                Optional<Function> mappingGetter = findMappingGetter(line);
                if (mappingGetter.isPresent()) {
                    functions.add(mappingGetter.get());
                }
            }
        }

        return functions;
    }

    private Optional<Function> findFunction(String line) {
        Matcher matcher = PATTERN_FUNCTION.matcher(line);
        if (matcher.find()) {

            String functionName = matcher.group(2);
            String functionArgs = matcher.group(4);

            String normalizedFunctionSignature = normalizeFunctionSignature(functionName, functionArgs);

            String functionSelector = getFunctionSelector(normalizedFunctionSignature);
            log.info("Function selector: {}", functionSelector);
            return Optional.of(new Function(functionSelector, normalizedFunctionSignature));
        }
        return Optional.empty();
    }

    private Optional<Function> findMappingGetter(String line) {
        Matcher mappingVariableMatcher = PATTERN_MAPPING_VARIABLE.matcher(line);
        if (mappingVariableMatcher.find()) {
            log.info("MappingFound: {}", line);
            List<String> mappingArguments = new ArrayList<>();
            String mappingName = mappingVariableMatcher.group(3);
            String canonicalMappingFirstArgument = toCanonicalType(mappingVariableMatcher.group(1));
            mappingArguments.add(canonicalMappingFirstArgument);
            String mappingReturn = mappingVariableMatcher.group(2);
            while (true) {
                Matcher mappingMatcher = PATTERN_MAPPING.matcher(mappingReturn);
                Matcher arrayMatcher = PATTERN_ARRAY.matcher(mappingName);
                if (mappingMatcher.find()) {
                    String mappingArgument = mappingMatcher.group(1);
                    String canonicalMappingArgument = toCanonicalType(mappingArgument);
                    mappingArguments.add(canonicalMappingArgument);
                    log.info("MappingArgs: {}", mappingArgument);
                    mappingReturn = mappingMatcher.group(2);
//                } else if(arrayMatcher.find()){
//                    // TODO ADD ARRAY SUPPORT IN MAPPING RETURN
                } else {
                    break;
                }
            }
            String normalizedMappingGetterSignature = mappingName + "(" + String.join(",", mappingArguments) + ")";
            String functionSelector = getFunctionSelector(normalizedMappingGetterSignature);
            return Optional.of(new Function(functionSelector, normalizedMappingGetterSignature));
        }
        return Optional.empty();
    }

    private String normalizeFunctionSignature(String functionName, String functionArgumentsString) {
        String[] functionArguments = functionArgumentsString.trim().split("\\s*,\\s*");

        List<String> functionArgumentsList = asList(functionArguments);

        String normalizedArguments =
                functionArgumentsList
                        .stream()
                        .map(s -> toCanonicalType(getFirstWord(s)))
                        .collect(joining(","));

        String join = functionName + "(" + normalizedArguments + ")";
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