package com.piotrjasina.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bytecode")
public class ByteCodeController {

    @GetMapping
    public String greeting() {
        return "bytecode-reader";
    }


    @PostMapping
    public String handleFileUpload(@RequestParam("byteCodeString") String byteCodeString, Model model) {

        model.addAttribute("byteCodeString", byteCodeString);
        return "bytecode-reader";
    }
}
