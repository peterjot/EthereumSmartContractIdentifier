package com.smartcontract.solidity;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static com.smartcontract.solidity.SolidityPattern.*;
import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.web3j.crypto.Hash.sha3String;

@Component
class SolidityParser {

    private static final Logger LOGGER = getLogger(SolidityParser.class);

    private static final String CANONICAL_ARRAY_KEY_TYPE = "uint256";

    private static final Map<String, String> CANONICAL_TYPES = unmodifiableMap(new HashMap<String, String>() {{
        put("uint", "uint256");
        put("int", "int256");
        put("byte", "bytes1");
    }});


    Optional<SolidityFunction> findFunctionInLine(String line) {
        List<Optional<SolidityFunction>> functions =
                Stream.of(
                        findFunctionSignature(line),
                        findMappingGetter(line),
                        findArrayGetter(line),
                        findNormalVariableGetter(line)
                ).filter(Optional::isPresent).collect(toList());

        if (functions.size() > 1) {
            throw new IllegalStateException("Expected only one function, but found :" + functions.size());
        } else if (functions.size() == 1) {
            return functions.listIterator().next();
        }
        return Optional.empty();
    }

    private Optional<SolidityFunction> findFunctionSignature(String line) {
        Matcher matcher = FUNCTION_PATTERN.matcher(line);
        if (matcher.find()) {
            String functionName = matcher.group(FUNCTION_NAME_GROUP_ID);
            String functionArguments = matcher.group(FUNCTION_ARGUMENTS_GROUP_ID);
            String functionSignature = normalizeFunctionSignature(functionName, functionArguments);
            LOGGER.info("Function signature: {}", functionSignature);
            String functionSelector = getFunctionSelector(functionSignature);
            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }
        return Optional.empty();
    }

    private Optional<SolidityFunction> findMappingGetter(String line) {
        Matcher mappingVariableMatcher = MAPPING_PUBLIC_VARIABLE_PATTERN.matcher(line);
        if (mappingVariableMatcher.find()) {
            LOGGER.info("Found public mapping variable: {}", line);

            String mappingName = mappingVariableMatcher.group(MAPPING_VARIABLE_NAME_GROUP_ID);
            String mappingKey = mappingVariableMatcher.group(MAPPING_VARIABLE_KEY_GROUP_ID);
            String canonicalMappingKey = toCanonicalType(mappingKey);
            String mappingValue = mappingVariableMatcher.group(MAPPING_VARIABLE_VALUE_GROUP_ID);

            List<String> canonicalMappingKeys = new ArrayList<>();
            canonicalMappingKeys.add(canonicalMappingKey);
            while (true) {
                Matcher mappingMatcher = MAPPING_PATTERN.matcher(mappingValue);
                Matcher arrayMatcher = ARRAY_PATTERN.matcher(mappingValue);

                if (mappingMatcher.find()) {
                    String canonicalArgument = toCanonicalType(mappingMatcher.group(MAPPING_KEY_GROUP_ID));
                    canonicalMappingKeys.add(canonicalArgument);
                    mappingValue = mappingMatcher.group(MAPPING_VALUE_GROUP_ID);
                    continue;
                }
                if (arrayMatcher.find()) {
                    String arrayValue = arrayMatcher.group(ARRAY_VALUE_GROUP_ID);
                    List<String> arrayDimensionParameters = getArrayDimensionParameters(arrayValue);
                    canonicalMappingKeys.addAll(arrayDimensionParameters);
                }
                break;
            }
            String functionSignature = mappingName + "(" + join(",", canonicalMappingKeys) + ")";
            String functionSelector = getFunctionSelector(functionSignature);
            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }
        return Optional.empty();
    }

    private Optional<SolidityFunction> findArrayGetter(String line) {
        Matcher matcher = ARRAY_PUBLIC_VARIABLE_PATTERN.matcher(line);
        if (matcher.find()) {
            LOGGER.info("Found public array variable: {}", line);
            String arrayName = matcher.group(ARRAY_PUBLIC_NAME_GROUP_ID);
            String arrayValue = matcher.group(ARRAY_PUBLIC_VALUE_GROUP_ID);
            List<String> arrayArguments = getArrayDimensionParameters(arrayValue);
            String functionSignature = arrayName + "(" + join(",", arrayArguments) + ")";
            String functionSelector = getFunctionSelector(functionSignature);

            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }
        return Optional.empty();
    }

    private List<String> getArrayDimensionParameters(String arrayValue) {
        int dimensionCount = getArrayDimensionCount(arrayValue);
        List<String> arrayArguments = new ArrayList<>();
        for (int i = 0; i < dimensionCount; i++) {
            arrayArguments.add(CANONICAL_ARRAY_KEY_TYPE);
        }
        return arrayArguments;
    }

    private Optional<SolidityFunction> findNormalVariableGetter(String line) {
        Matcher matcher = NORMAL_VARIABLE_PATTERN.matcher(line);
        if (matcher.find()) {
            LOGGER.info("Found public normal variable: {}", line);
            String variableName = matcher.group(NORMAL_VARIABLE_NAME_GROUP_ID);
            String functionSignature = variableName + "()";
            String functionSelector = getFunctionSelector(functionSignature);
            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }
        return Optional.empty();
    }

    private int getArrayDimensionCount(String arrayValue) {
        int dimensionCount = 0;
        for (int i = 0; i < arrayValue.length(); i++) {
            if (arrayValue.charAt(i) == '[') {
                dimensionCount++;
            }
        }
        checkDimensionCount(dimensionCount);
        return dimensionCount;
    }

    private void checkDimensionCount(int dimensionCount) {
        if (dimensionCount <= 0) {
            throw new IllegalStateException("Expected greater than one dimensionCount, but got: " + dimensionCount);
        }
    }

    private String getFunctionSelector(String normalizedFunctionSignature) {
        return sha3String(normalizedFunctionSignature).substring(2, 10);
    }

    private String normalizeFunctionSignature(String functionName, String functionArgumentsString) {
        String normalizedArguments = getNormalizedArguments(functionArgumentsString);
        String normalizedFunctionSignature = functionName + "(" + normalizedArguments + ")";
//        LOGGER.info(("SolidityFunction signature(normalized): [{}]"), normalizedFunctionSignature);

        return normalizedFunctionSignature;
    }

    private String getNormalizedArguments(String functionArgumentsString) {
        String[] functionArguments = functionArgumentsString.split("\\s*,\\s*");
        List<String> functionArgumentsList = asList(functionArguments);

        return functionArgumentsList
                .stream()
                .map(s -> toCanonicalType(getFirstWord(s)))
                .collect(joining(","));
    }

    private String toCanonicalType(String baseType) {
        if (CANONICAL_TYPES.values().contains(baseType)) {
            return baseType;
        }

        for (int i = 0; i < baseType.length(); i++) {
            if (baseType.charAt(i) == '[') {
                String s = baseType.substring(0, i);
                String offSet = baseType.substring(i);
                return getCanonicalType(s) + offSet;
            }
        }

//        LOGGER.info("Base type: {}", baseType);
        return getCanonicalType(baseType);
    }

    private String getCanonicalType(String s) {
        String canonicalType = CANONICAL_TYPES.get(s);
        if (nonNull(canonicalType)) {
            return canonicalType;
        } else {
            return s;
        }
    }

    private String getFirstWord(String s) {
        return s.replaceAll(" .*", "");
    }
}
