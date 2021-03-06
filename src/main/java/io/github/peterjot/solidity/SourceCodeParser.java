package io.github.peterjot.solidity;

import lombok.NonNull;
import org.slf4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static io.github.peterjot.solidity.SolidityPattern.*;
import static java.util.Arrays.asList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.web3j.crypto.Hash.sha3String;

class SourceCodeParser {

    private static final Logger LOGGER = getLogger(SourceCodeParser.class);
    private static final String CANONICAL_ARRAY_KEY_TYPE = "uint256";
    private static final Map<String, String> CANONICAL_TYPES = Map.ofEntries(
            entry("uint", "uint256"),
            entry("int", "int256"),
            entry("byte", "bytes1"),
            entry("fixed", "fixed128x18"),
            entry("ufixed", "ufixed128x18)")
    );


    Optional<SolidityFunction> findFunctionInLine(@NonNull String line) {
        List<Optional<SolidityFunction>> functions =
                Stream.of(
                        findFunctionSignature(line),
                        findMappingGetter(line),
                        findArrayGetter(line),
                        findNormalVariableGetter(line)
                ).filter(Optional::isPresent).collect(toList());

        if (functions.size() > 1) {
            throw new IllegalStateException("Expected only one function, but found: " + functions.size());
        }

        if (functions.size() == 1) {
            return functions.listIterator().next();
        }

        return Optional.empty();
    }

    private Optional<SolidityFunction> findFunctionSignature(String line) {
        Matcher matcher = FUNCTION_PATTERN.matcher(line);

        if (!matcher.find()) {
            return Optional.empty();
        }

        String functionName = matcher.group(FUNCTION_NAME_GROUP_ID);
        String functionArguments = matcher.group(FUNCTION_ARGUMENTS_GROUP_ID);

        String functionSignature = getNormalizedFunctionSignature(functionName, functionArguments);
        LOGGER.info("Function signature: [{}]", functionSignature);

        String functionSelector = getFunctionSelector(functionSignature);
        return Optional.of(new SolidityFunction(functionSelector, functionSignature));
    }

    private Optional<SolidityFunction> findMappingGetter(String line) {
        Matcher mappingVariableMatcher = MAPPING_PUBLIC_VARIABLE_PATTERN.matcher(line);

        if (!mappingVariableMatcher.find()) {
            return Optional.empty();
        }

        LOGGER.info("Found public mapping variable: [{}]", line);

        String mappingName = mappingVariableMatcher.group(MAPPING_VARIABLE_NAME_GROUP_ID);
        List<String> mappingKeys = getMappingKeys(mappingVariableMatcher);

        SolidityFunction solidityFunction = getSolidityFunction(mappingName, mappingKeys);

        return Optional.of(solidityFunction);
    }

    private List<String> getMappingKeys(Matcher mappingVariableMatcher) {
        List<String> canonicalMappingKeys = new ArrayList<>();
        canonicalMappingKeys.add(getMappingKey(mappingVariableMatcher, MAPPING_VARIABLE_KEY_GROUP_ID));

        String mappingValue = mappingVariableMatcher.group(MAPPING_VARIABLE_VALUE_GROUP_ID);
        Matcher mappingMatcher = MAPPING_PATTERN.matcher(mappingValue);

        while (mappingMatcher.find()) {
            String canonicalArgument = getMappingKey(mappingMatcher, MAPPING_KEY_GROUP_ID);
            canonicalMappingKeys.add(canonicalArgument);
            mappingValue = mappingMatcher.group(MAPPING_VALUE_GROUP_ID);
            mappingMatcher = MAPPING_PATTERN.matcher(mappingValue);
        }

        canonicalMappingKeys.addAll(getArrayParameters(mappingValue));

        return canonicalMappingKeys;
    }

    private List<String> getArrayParameters(String mappingValue) {
        Matcher arrayMatcher = ARRAY_PATTERN.matcher(mappingValue);

        if (arrayMatcher.find()) {
            return getArrayParameters(arrayMatcher);
        }

        return new ArrayList<>();
    }

    private String getMappingKey(Matcher mappingVariableMatcher, int mappingVariableKeyGroupId) {
        return toCanonicalType(mappingVariableMatcher.group(mappingVariableKeyGroupId));
    }

    private List<String> getArrayParameters(Matcher arrayMatcher) {
        String arrayValue = arrayMatcher.group(ARRAY_VALUE_GROUP_ID);
        return getArrayDimensionParameters(arrayValue);
    }

    private Optional<SolidityFunction> findArrayGetter(String line) {
        Matcher matcher = ARRAY_PUBLIC_VARIABLE_PATTERN.matcher(line);

        if (!matcher.find()) {
            return Optional.empty();
        }

        LOGGER.info("Found public array variable: {}", line);

        String arrayValue = matcher.group(ARRAY_PUBLIC_VALUE_GROUP_ID);
        String arrayName = matcher.group(ARRAY_PUBLIC_NAME_GROUP_ID);

        List<String> arrayArguments = getArrayDimensionParameters(arrayValue);

        return Optional.of(getSolidityFunction(arrayName, arrayArguments));
    }

    private SolidityFunction getSolidityFunction(String mappingName, List<String> canonicalMappingKeys) {
        String functionSignature = mappingName + "(" + String.join(",", canonicalMappingKeys) + ")";
        String functionSelector = getFunctionSelector(functionSignature);
        return new SolidityFunction(functionSelector, functionSignature);
    }


    private List<String> getArrayDimensionParameters(String arrayValue) {
        int dimensionCount = getArrayDimensionCount(arrayValue);
        return Collections.nCopies(dimensionCount, CANONICAL_ARRAY_KEY_TYPE);
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

    private String getNormalizedFunctionSignature(String functionName, String functionArgumentsString) {
        String normalizedArguments = getNormalizedArguments(functionArgumentsString);
        return functionName + "(" + normalizedArguments + ")";
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

        return getCanonicalType(baseType);
    }

    private String getCanonicalType(String baseType) {
        return Optional
                .ofNullable(CANONICAL_TYPES.get(baseType))
                .orElse(baseType);
    }

    private String getFirstWord(String s) {
        return s.replaceAll(" .*", "");
    }
}
