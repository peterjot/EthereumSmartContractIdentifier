pragma solidity ^0.4.24;

contract SimpleSocialNetwork {
    struct Comment {
        string text;
    }

    struct Post {
        string text;
    }

    mapping(uint => mapping(uint232 => uint)) public mymap1;
    mapping(uint => mapping(uint => mapping(uint => uint))) public mymap2;
    mapping(uint => mapping(uint => mapping(uint => mapping(uint => uint)))) public mymap3;


}