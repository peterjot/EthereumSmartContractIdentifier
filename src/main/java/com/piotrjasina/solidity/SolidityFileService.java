package com.piotrjasina.solidity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.piotrjasina.Utils.convertToString;
import static com.piotrjasina.Utils.sourceCodeHash;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.web3j.crypto.Hash.sha3String;

@Service
@Slf4j
public class SolidityFileService {
    //                                     -xxxxxxxxx-- function name        --xxx--fun args--xxx--xxx
    private static final String pattern = "(function\\s+)([a-zA-Z_][a-zA-Z0-9_]*)(\\s*\\(\\s*)([^(){}]*)(\\s*\\)\\s*)(.*)";

    private final SolidityFileRepository solidityFileRepository;

    @Autowired
    public SolidityFileService(SolidityFileRepository solidityFileRepository) {
        this.solidityFileRepository = solidityFileRepository;
    }

    public SolidityFile save(InputStream inputStream) throws Exception {

        String sourceCode = convertToString(inputStream);
        String sourceCodeHash = sourceCodeHash(sourceCode);
        Set<Function> functionsFromFile = getFunctionsFromFile(inputStream);

        return solidityFileRepository.save(new SolidityFile(sourceCode, sourceCodeHash, functionsFromFile));
    }


    public Set<Function> getFunctionsFromFile(InputStream inputStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        log.info("Reading solidity file");

        Pattern pattern = Pattern.compile(SolidityFileService.pattern);

        Set<Function> functions = new HashSet<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {

                String functionName = matcher.group(2);
                log.info("Function: [{}]", functionName);
                String functionArgs = matcher.group(4);
                log.info("Function args: [{}]", functionArgs);

                String normalizedFunctionSignature = normalizeFunctionSignature(functionName, functionArgs);

                String functionSignatureHash = getFunctionSignatureHash(normalizedFunctionSignature);
                log.info("Function selector: {}", functionSignatureHash);

                Function function = new Function(normalizedFunctionSignature, functionSignatureHash);
                functions.add(function);
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

    private String getFunctionSignatureHash(String normalizedFunctionSignature) {
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