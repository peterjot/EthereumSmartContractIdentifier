package com.smartcontract.solidity;

import com.smartcontract.solidity.solidityfile.Function;
import com.smartcontract.solidity.solidityfile.SolidityFile;
import com.smartcontract.solidity.solidityfile.SolidityFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.smartcontract.Utils.stringHash;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class SolidityService {

    private static final String FUNCTION_REGEX = "^\\s*function\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(\\s*([^(){}]*)\\s*\\)\\s*.*";
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION_REGEX);
    private static final int FUNCTION_NAME_GROUP_ID = 1;
    private static final int FUNCTION_ARGS_GROUP_ID = 2;

    private static final String MAPPING_VARIABLE_REGEX = "^\\s*mapping\\s*\\(\\s*([a-zA-Z][a-zA-Z]*)\\s*=>\\s*(.*)\\s*\\)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*$";
    private static final Pattern MAPPING_VARIABLE_PATTERN = Pattern.compile(MAPPING_VARIABLE_REGEX);
    private static final int MAPPING_VARIABLE_ARGUMENT_GROUP_ID = 1;
    private static final int MAPPING_VARIABLE_VALUE_GROUP_ID = 2;
    private static final int MAPPING_VARIABLE_NAME_GROUP_ID = 3;

    private static final String MAPPING_REGEX = "^\\s*mapping\\s*\\(\\s*([a-zA-Z0-9][a-zA-Z0-9]*)\\s*=>\\s*(.*)\\s*\\)\\s*";
    private static final Pattern MAPPING_PATTERN = Pattern.compile(MAPPING_REGEX);

//    private static final String ARRAY_REGEX = "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*(\\s*\\[\\s*[0-9]*\\s*]\\s*)+";// TODO ADD ARRAY_PATTERN SUPPORT IN MAPPING_PATTERN RETURN
//    private static final Pattern ARRAY_PATTERN = Pattern.compile(ARRAY_REGEX);// TODO ADD ARRAY_PATTERN SUPPORT IN MAPPING_PATTERN RETURN

    private static final Charset CHARSET = StandardCharsets.UTF_8;


    private final SolidityFileRepository solidityFileRepository;

    @Autowired
    public SolidityService(SolidityFileRepository solidityFileRepository) {
        checkNotNull(solidityFileRepository, "Expected not-null solidityFileRepository");
        this.solidityFileRepository = solidityFileRepository;
    }

    public String getSourceCodeByHash(String _fileHash) {
        checkNotNull(_fileHash, "Expected not-null _fileHash");
        String fileHash;
        if (!_fileHash.startsWith("0x")) {
            fileHash = "0x" + _fileHash;
        } else {
            fileHash = _fileHash;
        }

        Optional<SolidityFile> solidityFile = solidityFileRepository.findBySourceCodeHash(fileHash);

        if (solidityFile.isPresent()) {
            return solidityFile.get().getSourceCode();
        }
        throw new RuntimeException(format("No source code for fileHash: %s", fileHash));
    }

    public List<SolidityFile> findAllFiles() {
        return solidityFileRepository.findAll();
    }

    public long getSolidityFilesCount() {
        return solidityFileRepository.count();
    }

    public SolidityFile save(String sourceCode) throws IOException {
        checkNotNull(sourceCode, "Expected not-null sourceCode");
        return save(sourceCode.getBytes());
    }

    SolidityFile save(byte[] sourceCodeBytes) throws IOException {
        String sourceCode = new String(sourceCodeBytes, CHARSET);
        String sourceCodeHash = stringHash(sourceCode);

        Set<Function> functionsFromFile = findFunctionsFromFile(new ByteArrayInputStream(sourceCodeBytes));

        log.info("SourceCode hash: [{}]", sourceCodeHash);
        log.info("SourceCode functions count: {}", functionsFromFile.size());

        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }

    Set<Function> findFunctionsFromFile(InputStream inputStream) throws IOException {
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
                mappingGetter.ifPresent(functions::add);
            }
        }
        return functions;
    }

    List<Function> findAllUniqueFunctions() {
        return solidityFileRepository
                .findAll()
                .stream()
                .map(SolidityFile::getFunctions)
                .flatMap(Collection::stream)
                .distinct()
                .collect(toList());
    }

    private Optional<Function> findFunction(String line) {
        Matcher matcher = FUNCTION_PATTERN.matcher(line);
        if (matcher.find()) {

            String functionName = matcher.group(FUNCTION_NAME_GROUP_ID);
            String functionArgs = matcher.group(FUNCTION_ARGS_GROUP_ID);

            String normalizedFunctionSignature = normalizeFunctionSignature(functionName, functionArgs);

            String functionSelector = getFunctionSelector(normalizedFunctionSignature);
            log.info("Function selector: {}", functionSelector);

            return Optional.of(new Function(functionSelector, normalizedFunctionSignature));
        }
        return Optional.empty();
    }

    private Optional<Function> findMappingGetter(String line) {
        Matcher mappingVariableMatcher = MAPPING_VARIABLE_PATTERN.matcher(line);
        if (mappingVariableMatcher.find()) {
            log.info("MappingFound: {}", line);

            List<String> canonicalMappingArguments = new ArrayList<>();

            String mappingArgument = mappingVariableMatcher.group(MAPPING_VARIABLE_ARGUMENT_GROUP_ID);
            String canonicalMappingArgument = toCanonicalType(mappingArgument);

            canonicalMappingArguments.add(canonicalMappingArgument);

            String mappingValue = mappingVariableMatcher.group(MAPPING_VARIABLE_VALUE_GROUP_ID);
            while (true) {
                Matcher mappingMatcher = MAPPING_PATTERN.matcher(mappingValue);
                //Matcher arrayMatcher = ARRAY_PATTERN.matcher(mappingValue);// TODO ADD ARRAY_PATTERN SUPPORT IN MAPPING_PATTERN RETURN

                if (mappingMatcher.find()) {
                    String mappingNextArgument = mappingMatcher.group(MAPPING_VARIABLE_ARGUMENT_GROUP_ID);
                    String canonicalArgument = toCanonicalType(mappingNextArgument);
                    canonicalMappingArguments.add(canonicalArgument);
                    log.info("MappingArgs: {}", canonicalArgument);
                    mappingValue = mappingMatcher.group(MAPPING_VARIABLE_VALUE_GROUP_ID);
//                } else if(arrayMatcher.find()){
//                    // TODO ADD ARRAY_PATTERN SUPPORT IN MAPPING_PATTERN RETURN
                } else {
                    break;
                }
            }

            String mappingName = mappingVariableMatcher.group(MAPPING_VARIABLE_NAME_GROUP_ID);
            String normalizedMappingGetterSignature = mappingName + "(" + String.join(",", canonicalMappingArguments) + ")";
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
        return getEightBytesFromHashWithout0xPrefix(normalizedFunctionSignature);
    }

    private String getEightBytesFromHashWithout0xPrefix(String value) {
        return stringHash(value).substring(2, 10);
    }

    private String toCanonicalType(String baseType) {
        Map<String, String> canonicalTypes = new HashMap<>();
        canonicalTypes.put("uint", "uint256");
        canonicalTypes.put("int", "int256");
        canonicalTypes.put("byte", "bytes1");

        String canonicalType = canonicalTypes.get(baseType);
        if (nonNull(canonicalType)) {
            return canonicalType;
        }
        return baseType;
    }
}