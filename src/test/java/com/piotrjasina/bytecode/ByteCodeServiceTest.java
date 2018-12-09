package com.piotrjasina.bytecode;

import com.piotrjasina.solidity.Function;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.Assert.*;

public class ByteCodeServiceTest {


    @Test
    public void CountDuplicates(){

        Function function1 = new Function("123","321");
        Function function2 = function1;
        Function function3 = new Function("123","321");

        List<Function> functions = Arrays.asList(function1,function2,function3);

        Map<Function, Long> collect = functions.stream().collect(groupingBy(identity(), counting()));
        System.out.println(collect);
    }

}