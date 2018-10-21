package com.piotrjasina.controller;

import com.piotrjasina.dto.MethodDto;
import com.piotrjasina.service.SolidityFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.List;

@Controller
@RequestMapping("/solidity")
@Slf4j
public class SolidityFileController {

    private final SolidityFileService solidityFileService;

    @Autowired
    public SolidityFileController(SolidityFileService solidityFileService) {
        this.solidityFileService = solidityFileService;
    }

    @GetMapping
    public String greeting() {
        return "solidity-reader";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        List<MethodDto> methodDtos = solidityFileService.getMethodsFromFile(new InputStreamReader(file.getInputStream()));

        model.addAttribute("methodDtos", methodDtos);
        return "solidity-reader";
    }
}
