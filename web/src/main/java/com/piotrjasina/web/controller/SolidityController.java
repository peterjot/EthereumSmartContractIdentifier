package com.piotrjasina.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/solidity")
public class SolidityController {

    @GetMapping
    public String greeting(Model model) {
        model.addAttribute("name", "WITAM");
        return "solidity-reader";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {

        String fileString;
        try {
            fileString = new String(file.getBytes(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            fileString = "";
        }

        model.addAttribute("fileString", fileString);
        return "solidity-reader";
    }
}
