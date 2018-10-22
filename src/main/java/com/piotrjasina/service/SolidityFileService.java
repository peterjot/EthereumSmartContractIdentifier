package com.piotrjasina.service;

import com.piotrjasina.dto.MethodDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.crypto.Hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class SolidityFileService {
    //                                     -xxxxxxxxx-- function name        --xxx--fun args--xxx--xxx
    private static final String pattern = "(function\\s+)([a-zA-Z_][a-zA-Z0-9_]*)(\\s*\\(\\s*)([^(){}]*)(\\s*\\)\\s*)(.*)";


    public List<MethodDto> getMethodsFromFile(@RequestParam("file") InputStreamReader inputStreamReader) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        log.info("Rozpoczynam czytanie pliku");

        Pattern p = Pattern.compile(pattern);

        List<MethodDto> methodDtos = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Matcher matcher = p.matcher(line);
            if (matcher.find()) {

                String functionName = matcher.group(2);
                log.info("FUNKCJA: [{}]", functionName);
                String functionArgs = matcher.group(4);
                log.info("FUNKCJA ARGS: [{}]", functionArgs);

                String normalizedFunctionSignature = normalizeFunctionSignature(functionName, functionArgs);

                String functionSignatureHash = getFunctionSignatureHash(normalizedFunctionSignature);
                log.info("HASH: {}", functionSignatureHash);

                MethodDto methodDto = new MethodDto(normalizedFunctionSignature, functionSignatureHash);
                methodDtos.add(methodDto);
            }
        }
        return methodDtos;
    }

    private String normalizeFunctionSignature(String functionName, String functionArguments) {
        String[] splitedFunctionArguments = functionArguments.trim().split("\\s*,\\s*");
        List<String> functionArgumentsList = Arrays.asList(splitedFunctionArguments);
        log.info("FUNKCJA ARGS(splited): [{}]", functionArgumentsList);

        List<String> normalizedArguments =
                functionArgumentsList
                        .stream()
                        .map(s -> toCanonicalType(getFirstWord(s)))
                        .collect(toList());
        log.info("FUNKCJA ARGS(usuniete nazwy argumentow: [{}]", normalizedArguments);

        String join = functionName.trim() + "(" + String.join(",", normalizedArguments) + ")";
        log.info(("FUNKCJA ARGS(join): [{}]"), join);
        return join;
    }

    private String getFirstWord(String s) {
        return s.replaceAll(" .*", "");
    }

    private String getFunctionSignatureHash(String normalizedFunctionSignature) {
        return Hash.sha3String(normalizedFunctionSignature).substring(2, 10);
    }

    private String toCanonicalType(String from) {
        Map<String, String> mapuj = new HashMap<>();
        mapuj.put("uint", "uint256");
        mapuj.put("int", "int256");
        mapuj.put("byte", "bytes1");


        String s = mapuj.get(from);
        if (s != null) {
            return s;
        }
        return from;
    }
}