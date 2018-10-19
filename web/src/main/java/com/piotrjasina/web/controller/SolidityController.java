package com.piotrjasina.web.controller;

import com.piotrjasina.web.dto.MethodDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/solidity")
@Slf4j
public class SolidityController {

    private static final String pattern = "(function )(\\w*)(\\(.*\\))(.*)";

    @GetMapping
    public String greeting(Model model) {
        model.addAttribute("name", "WITAM");
        return "solidity-reader";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        log.info("Rozpoczynam czytanie pliku");

        Pattern p = Pattern.compile(pattern);

        List<MethodDto> methodDtos = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Matcher matcher = p.matcher(line);
            if (matcher.find()) {
                String functionString = matcher.group(2).trim();
                log.info("FUNKCJA: [{}]", functionString);
                String functionArgs = matcher.group(3).trim();
                functionArgs = functionArgs.substring(1, functionArgs.length() - 1);
                String[] split = functionArgs.split(",");

                List<String> collect = Stream.of(split).map(String::trim).collect(toList());

                List<String> collect1 = collect.stream().map(s -> maper(s.replaceAll(" .*", ""))).collect(Collectors.toList());

                System.out.println(collect1);
                String join = String.join(",", collect1);
                join = functionString + "(" + join + ")";


                log.info("FUNKCJA ARGS: {}", join);
                String hashString = Hash.sha3String(join).substring(0, 10);
                log.info("HASH: {}", hashString);
                MethodDto methodDto = new MethodDto(join, hashString);
                methodDtos.add(methodDto);
            }
        }


        model.addAttribute("methoddtos", methodDtos);
        return "solidity-reader";
    }

    public String maper(String from) {
        Map<String, String> mapuj = new HashMap<>();
        mapuj.put("uint", "uint256");


        String s = mapuj.get(from);
        if (s != null) {
            return s;
        }
        return from;
    }
}
