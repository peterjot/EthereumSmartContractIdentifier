package com.piotrjasina;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.web3j.crypto.Hash;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UtilsTest {

    private static final String PATTERN_MAPPING_VARIABLE_VALUE = "^\\s*mapping\\s*\\(\\s*([a-zA-Z0-9][a-zA-Z0-9]*)\\s*=>\\s*(.*)\\s*\\)\\s*public\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(=.*)?\\s*;+\\s*$";
    private static final String PATTERN_MAPPING_VALUE = "^\\s*mapping\\s*\\(\\s*([a-zA-Z0-9][a-zA-Z0-9]*)\\s*=>\\s*(.*)\\s*\\)\\s*";

    private static final Pattern PATTERN_MAPPING_VARIABLE = Pattern.compile(PATTERN_MAPPING_VARIABLE_VALUE);
    private static final Pattern PATTERN_MAPPING = Pattern.compile(PATTERN_MAPPING_VALUE);

    @Test
    public void stringHash() {
        System.out.println(Hash.sha3String("testtest"));
    }

    @Test
    public void regexForMaping() {
        Pattern pattern2 = Pattern.compile(PATTERN_MAPPING_VARIABLE_VALUE);

        String[] lines = getTestFile().split("\n");

        for (String line : lines) {
            Matcher matcher2 = pattern2.matcher(line);

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
    public void regexForArray(){
        String[] lines = getFileWithArrayAttributes().split("\n");
        String arrayPatternValue = "^\\s*[a-zA-Z0-9][a-zA-Z0-9]*(\\s*\\[\\s*[0-9]*\\s*]\\s*)+";
        Pattern arrayPattern = Pattern.compile(arrayPatternValue);

        for(String line: lines){
            Matcher matcher = arrayPattern.matcher(line);

            if(matcher.find()){
                log.info("Found array line: {}",line);
            }


        }

    }


    private String getFileWithArrayAttributes(){
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

    @Test
    public void regexForMoreMappingMaping() {


        String[] lines = getMappingProblemFile().split("\n");

        for (String line : lines) {
            findMappingGetter(line).ifPresent(System.out::println);
        }

    }

    private Optional<String> findMappingGetter(String line) {
        Matcher matcher2 = PATTERN_MAPPING_VARIABLE.matcher(line);

        if (matcher2.find()) {
            List<String> mappingArguments = new ArrayList<>();
            String mappingName = matcher2.group(3);
            String mappingReturn = matcher2.group(2);
            mappingArguments.add(toCanonicalType(matcher2.group(1)));
            System.out.println(mappingReturn);

            while (true) {
                Matcher mappingMatcher = PATTERN_MAPPING.matcher(mappingReturn);
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
}