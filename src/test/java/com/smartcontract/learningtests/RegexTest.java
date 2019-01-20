package com.smartcontract.learningtests;

import com.smartcontract.solidity.SolidityParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.Hash;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RegexTest {


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

    private static final String ARRAY_REGEX = "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*(\\s*\\[\\s*[0-9]*\\s*]\\s*)+";// TODO ADD ARRAY_PATTERN SUPPORT IN MAPPING_PATTERN RETURN
    private static final Pattern ARRAY_PATTERN = Pattern.compile(ARRAY_REGEX);// TODO ADD ARRAY_PATTERN SUPPORT IN MAPPING_PATTERN RETURN

    @Test
    @Ignore
    public void stringHash() {
        System.out.println(Hash.sha3String("testtest"));
    }

    @Test
    @Ignore
    public void regexForMapping() {

        String[] lines = getTestFile().split("\n");

        for (String line : lines) {
            Matcher matcher2 = MAPPING_VARIABLE_PATTERN.matcher(line);

            if (matcher2.find()) {
                System.out.println(matcher2.group(1));
                System.out.println(matcher2.group(2));
                String functionSignature = matcher2.group(2) + "(" + matcher2.group(1) + ")";
                log.info("Found mapping: {}", line);
                log.info("Created function signature: {}", functionSignature);
            }
        }

    }

    @Test
    @Ignore
    public void regexForArray() {
        String[] lines = getFileWithArrayAttributes().split("\n");

        for (String line : lines) {
            Matcher matcher = ARRAY_PATTERN.matcher(line);

            if (matcher.find()) {
                log.info("Found array line: {}", line);
            }
        }
    }

    @Test
    @Ignore
    public void regexForMoreMappingMapping() {
        String[] lines = getMappingProblemFile().split("\n");

        for (String line : lines) {
            findMappingGetter(line).ifPresent(System.out::println);
        }

    }

    private String getMappingProblemFile() {
        return "pragma solidity ^0.4.24;\n" +
                "\n" +
                "contract SimpleSocialNetwork {\n" +
                "\n" +
                "\n" +
                "        mapping(uint => mapping(uint232 => uint)) public mymap1;\n" +
                "\n" +
                "\n" +
                "}";
    }

    private Optional<String> findMappingGetter(String line) {
        Matcher matcher2 = MAPPING_VARIABLE_PATTERN.matcher(line);

        if (matcher2.find()) {
            List<String> mappingArguments = new ArrayList<>();
            String mappingName = matcher2.group(3);
            String mappingReturn = matcher2.group(2);
            mappingArguments.add(toCanonicalType(matcher2.group(1)));
            System.out.println(mappingReturn);

            while (true) {
                Matcher mappingMatcher = MAPPING_PATTERN.matcher(mappingReturn);
                if (mappingMatcher.find()) {
                    String mappingArgument = mappingMatcher.group(1);
                    mappingArguments.add(mappingArgument);
                    mappingReturn = mappingMatcher.group(2);
                } else {
                    break;
                }
            }

            return Optional.of(mappingName + "(" + String.join(",", mappingArguments) + ")");
        }
        return Optional.empty();
    }

    private String getFileWithArrayAttributes() {
        return "pragma solidity ^0.4.24;\n" +
                "\n" +
                "contract SimpleSocialNetwork {\n" +
                "    struct Comment {\n" +
                "        string text;\n" +
                "    }\n" +
                "\n" +
                "    struct Post {\n" +
                "        string text;\n" +
                "    }\n" +
                "\n" +
                "    uint232  [32][555] public mymap1;\n" +
                "    uint232  [32][555] public mymap5;\n" +
                "    uint232  [32][555] public mymap6;\n" +
                "    uint232   [32] [555] public mymap7;\n" +
                "    uint232  [32][ 555 ] public mymap8;\n" +
                "    uint232  [3 2][555] public mymap9;\n" +
                "    uint232  [ 32 ] [555 ] public mymap10;\n" +
                "    \n" +
                "    uint232[] public mymap2;\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "\n" +
                "\n" +
                "}";
    }

    private String getTestFile() {
        return "pragma solidity ^0.4.24;\n" +
                "\n" +
                "contract SimpleSocialNetwork {\n" +
                "    struct Comment {\n" +
                "        string text;\n" +
                "    }\n" +
                "\n" +
                "    struct Post {\n" +
                "        string text;\n" +
                "    }\n" +
                "\n" +
                "    mapping (address => uint[]) public postsFromAccount;\n" +
                "    mapping (uint => uint[]) public commentsFromPost;\n" +
                "    mapping (uint => address) public commentFromAccount;\n" +
                "    mapping (uint => address) public commentFromAccount;\n" +
                "\n" +
                "    Post[] public posts;\n" +
                "    Comment[] public comments;\n" +
                "\n" +
                "    event NewPostAdded(uint postId, uint commentId, address owner);\n" +
                "\n" +
                "    constructor () public {\n" +
                "        // created the first post and comment with ID\n" +
                "        // IDs 0 are invalid\n" +
                "        newPost(\"\");\n" +
                "        newComment(0, \"\");\n" +
                "    }\n" +
                "\n" +
                "    function hasPosts() public view returns(bool _hasPosts) {\n" +
                "        _hasPosts = posts.length > 0;\n" +
                "    }\n" +
                "\n" +
                "    function newPost(string _text) public {\n" +
                "        Post memory post = Post(_text);\n" +
                "        uint postId = posts.push(post) - 1;\n" +
                "        postsFromAccount[msg.sender].push(postId);\n" +
                "        emit NewPostAdded(postId, 0, msg.sender);\n" +
                "    }\n" +
                "\n" +
                "    function newComment(uint _postId, string _text) public {\n" +
                "        Comment memory comment = Comment(_text);\n" +
                "        uint commentId = comments.push(comment) - 1;\n" +
                "        commentsFromPost[_postId].push(commentId);\n" +
                "        commentFromAccount[commentId] = msg.sender;\n" +
                "        emit NewPostAdded(_postId, commentId, msg.sender);\n" +
                "    }\n" +
                "}";
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

    @Test
    public void functionInternal() {
        String FUNCTION_REGEX = "^\\s*function\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(\\s*([^(){}]*)\\s*\\)\\s*(?!.*(internal|private)).*$";

        Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION_REGEX);

        String line = "  function _fee()   returns(uint256){";

        Matcher matcher = FUNCTION_PATTERN.matcher(line);
        if (matcher.matches()) {
            System.out.println("Tak");
        }
    }

    @Test
    public void constantPublicVar() {
        String FUNCTION_REGEX = "^(internal|private)*$";

        Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION_REGEX);

        String line = "internalinternal";

        Matcher matcher = FUNCTION_PATTERN.matcher(line);
        if (matcher.matches()) {
            System.out.println("Tak");
        }
    }
}