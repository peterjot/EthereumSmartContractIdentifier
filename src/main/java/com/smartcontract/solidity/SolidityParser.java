package com.smartcontract.solidity;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.smartcontract.Utils.stringHash;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Slf4j
class SolidityParser {

    private static final String FUNCTION_REGEX = "^\\s*function\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(\\s*([^(){}]*)\\s*\\)\\s*.*";
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION_REGEX);
    private static final int FUNCTION_NAME_GROUP_ID = 1;
    private static final int FUNCTION_ARGUMENTS_GROUP_ID = 2;

    private static final String MAPPING_PUBLIC_VARIABLE_REGEX =
            "^\\s*mapping\\s*\\(\\s*([a-zA-Z][a-zA-Z]*)\\s*=>\\s*(.*)\\s*\\)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*$";
    private static final Pattern MAPPING_PUBLIC_VARIABLE_PATTERN = Pattern.compile(MAPPING_PUBLIC_VARIABLE_REGEX);
    private static final int MAPPING_VARIABLE_ARGUMENTS_GROUP_ID = 1;
    private static final int MAPPING_VARIABLE_VALUE_GROUP_ID = 2;
    private static final int MAPPING_VARIABLE_NAME_GROUP_ID = 3;

    private static final String MAPPING_REGEX = "^\\s*mapping\\s*\\(\\s*([a-zA-Z0-9][a-zA-Z0-9]*)\\s*=>\\s*(.*)\\s*\\)\\s*";
    private static final Pattern MAPPING_PATTERN = Pattern.compile(MAPPING_REGEX);
    private static final int MAPPING_ARGUMENTS_GROUP_ID = 1;
    private static final int MAPPING_VALUE_GROUP_ID = 2;

    private static final String ARRAY_PUBLIC_VARIABLE_REGEX =
            "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*((\\s*\\[\\s*[0-9]*\\s*]\\s*)+)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*$";
    private static final Pattern ARRAY_PUBLIC_VARIABLE_PATTERN = Pattern.compile(ARRAY_PUBLIC_VARIABLE_REGEX);
    private static final int ARRAY_PUBLIC_VALUE_GROUP_ID = 1;
    private static final int ARRAY_PUBLIC_NAME_GROUP_ID = 3;

    private static final String ARRAY_REGEX = "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*((\\s*\\[\\s*[0-9]*\\s*]\\s*)+)\\s*";
    private static final Pattern ARRAY_PATTERN = Pattern.compile(ARRAY_REGEX);
    private static final int ARRAY_VALUE_GROUP_ID = 1;

    private static final String NORMAL_VARIABLE_REGEX =
            "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*$";
    private static final Pattern NORMAL_VARIABLE_PATTERN = Pattern.compile(NORMAL_VARIABLE_REGEX);
    private static final int NORMAL_VARIABLE_NAME_GROUP_ID= 1;

    Optional<SolidityFunction> findFunctionInLine(String line) {
        List<Optional<SolidityFunction>> functions =
                Stream.of(
                        findFunctionSignature(line),
                        findMappingGetter(line),
                        findArrayGetter(line),
                        findNormalVariableGetter(line)
                ).filter(Optional::isPresent).collect(toList());

        if (functions.size() == 1) {
            return functions.listIterator().next();
        } else if (functions.size() > 1) {
            throw new IllegalStateException("Expected only one function, but found :" + functions.size());
        }
        return Optional.empty();
    }

    private Optional<SolidityFunction> findNormalVariableGetter(String line) {
        Matcher matcher = NORMAL_VARIABLE_PATTERN.matcher(line);
        if(matcher.find()){
            log.info("Found public normal variable: {}",line);
            String variableName = matcher.group(NORMAL_VARIABLE_NAME_GROUP_ID);

            String functionSignature = variableName + "()";
            String functionSelector = getFunctionSelector(functionSignature);
            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }
        return Optional.empty();
    }

    private Optional<SolidityFunction> findFunctionSignature(String line) {
        Matcher matcher = FUNCTION_PATTERN.matcher(line);
        if (matcher.find()) {
            log.info("Found function: {}",line);
            String functionName = matcher.group(FUNCTION_NAME_GROUP_ID);
            String functionArguments = matcher.group(FUNCTION_ARGUMENTS_GROUP_ID);

            String functionSignature = normalizeFunctionSignature(functionName, functionArguments);

            String functionSelector = getFunctionSelector(functionSignature);
            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }

        return Optional.empty();
    }

    private Optional<SolidityFunction> findArrayGetter(String line) {
        Matcher matcher = ARRAY_PUBLIC_VARIABLE_PATTERN.matcher(line);
        System.out.println(line);
        if (matcher.find()) {
            log.info("Found public array variable: {}",line);

            String arrayValue = matcher.group(ARRAY_PUBLIC_VALUE_GROUP_ID);
            String variableName = matcher.group(ARRAY_PUBLIC_NAME_GROUP_ID);

            int dimensionCount = 0;
            String arrayArgumentType = "uint256";
            StringBuilder arrayTypeArguments = new StringBuilder(arrayArgumentType);
            for (int i = 0; i < arrayValue.length(); i++) {
                if (arrayValue.charAt(i) == '[') {
                    dimensionCount++;
                    if (dimensionCount > 1) {
                        arrayTypeArguments.append(",").append(arrayArgumentType);
                    }
                }
            }

            if (dimensionCount == 0) {
                throw new IllegalStateException("Expected greater than one dimensionCount, but got: " + dimensionCount);
            }

            String functionSignature = variableName + "(" + arrayTypeArguments + ")";
            String functionSelector = getFunctionSelector(functionSignature);

            return Optional.of(new SolidityFunction(functionSelector, functionSignature));
        }

        return Optional.empty();
    }

    private Optional<SolidityFunction> findMappingGetter(String line) {
        Matcher mappingVariableMatcher = MAPPING_PUBLIC_VARIABLE_PATTERN.matcher(line);

        if (mappingVariableMatcher.find()) {
            log.info("Found public mapping variable: {}", line);

            List<String> canonicalMappingArguments = new ArrayList<>();

            String mappingArgument = mappingVariableMatcher.group(MAPPING_VARIABLE_ARGUMENTS_GROUP_ID);
            String canonicalMappingArgument = toCanonicalType(mappingArgument);

            canonicalMappingArguments.add(canonicalMappingArgument);

            String variableValue = mappingVariableMatcher.group(MAPPING_VARIABLE_VALUE_GROUP_ID);
            while (true) {
                Matcher mappingMatcher = MAPPING_PATTERN.matcher(variableValue);
                Matcher arrayMatcher = ARRAY_PATTERN.matcher(variableValue);

                if (mappingMatcher.find()) {
                    String mappingNextArgument = mappingMatcher.group(MAPPING_ARGUMENTS_GROUP_ID);
                    String canonicalArgument = toCanonicalType(mappingNextArgument);
                    canonicalMappingArguments.add(canonicalArgument);
                    variableValue = mappingMatcher.group(MAPPING_VALUE_GROUP_ID);
                } else if (arrayMatcher.find()) {
                    variableValue = arrayMatcher.group(ARRAY_VALUE_GROUP_ID);

                    int dimensionCount = 0;
                    String canonicalArrayArgumentType = "uint256";
                    canonicalMappingArguments.add(canonicalArrayArgumentType);
                    for (int i = 0; i < variableValue.length(); i++) {
                        if (variableValue.charAt(i) == '[') {
                            dimensionCount++;
                            if (dimensionCount > 1) {
                                canonicalMappingArguments.add(canonicalArrayArgumentType);
                            }
                        }

                    }
                } else {
                    break;
                }
            }
            log.info("MappingArgs: {}", canonicalMappingArguments);

            String mappingName = mappingVariableMatcher.group(MAPPING_VARIABLE_NAME_GROUP_ID);
            String normalizedMappingGetterSignature = mappingName + "(" + String.join(",", canonicalMappingArguments) + ")";
            String functionSelector = getFunctionSelector(normalizedMappingGetterSignature);
            return Optional.of(new SolidityFunction(functionSelector, normalizedMappingGetterSignature));
        }
        return Optional.empty();
    }

    private String getFunctionSelector(String value) {
        return stringHash(value).substring(2, 10);
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
        log.info(("SolidityFunction signature(normalized): [{}]"), join);

        return join;
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

    private String getFirstWord(String s) {
        return s.replaceAll(" .*", "");
    }
}
