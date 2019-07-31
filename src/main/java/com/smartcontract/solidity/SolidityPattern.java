package com.smartcontract.solidity;

import java.util.regex.Pattern;

final class SolidityPattern {

    static final Pattern FUNCTION_PATTERN = Pattern.compile(
            "^\\s*function\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(\\s*([^(){}]*)\\s*\\)\\s*(?!.*(internal|private)).*$");
    static final int FUNCTION_NAME_GROUP_ID = 1;
    static final int FUNCTION_ARGUMENTS_GROUP_ID = 2;
    static final Pattern MAPPING_PUBLIC_VARIABLE_PATTERN = Pattern.compile(
            "^\\s*mapping\\s*\\(\\s*([a-zA-Z][a-zA-Z]*)\\s*=>\\s*(.*)\\s*\\)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*(//.*)?$");
    static final int MAPPING_VARIABLE_KEY_GROUP_ID = 1;
    static final int MAPPING_VARIABLE_VALUE_GROUP_ID = 2;
    static final int MAPPING_VARIABLE_NAME_GROUP_ID = 3;
    static final Pattern MAPPING_PATTERN = Pattern.compile(
            "^\\s*mapping\\s*\\(\\s*([a-zA-Z0-9][a-zA-Z0-9]*)\\s*=>\\s*(.*)\\s*\\)\\s*");
    static final int MAPPING_KEY_GROUP_ID = 1;
    static final int MAPPING_VALUE_GROUP_ID = 2;
    static final Pattern ARRAY_PUBLIC_VARIABLE_PATTERN = Pattern.compile(
            "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*((\\s*\\[\\s*[a-zA-Z0-9]*\\s*]\\s*)+)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*(//.*)?$");
    static final int ARRAY_PUBLIC_VALUE_GROUP_ID = 1;
    static final int ARRAY_PUBLIC_NAME_GROUP_ID = 3;
    static final Pattern ARRAY_PATTERN = Pattern.compile(
            "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*((\\s*\\[\\s*[a-zA-Z0-9]*\\s*]\\s*)+)\\s*");
    static final int ARRAY_VALUE_GROUP_ID = 1;
    static final Pattern NORMAL_VARIABLE_PATTERN = Pattern.compile(
            "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*\\s*(\\bconstant)*\\s*public\\s*(\\bconstant)*\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*(//.*)?$");
    static final int NORMAL_VARIABLE_NAME_GROUP_ID = 3;

    private SolidityPattern() {
        throw new UnsupportedOperationException();
    }
}
