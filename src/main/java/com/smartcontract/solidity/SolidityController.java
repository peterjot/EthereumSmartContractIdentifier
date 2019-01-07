package com.smartcontract.solidity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping("/solidity")
@Slf4j
public class SolidityController {

    private final SolidityService solidityService;

    @Autowired
    public SolidityController(SolidityService solidityService) {
        checkNotNull(solidityService, "Expected not-null solidityService");
        this.solidityService = solidityService;
    }

    @GetMapping
    public String mainPage() {
        return "solidity-reader";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        checkNotNull(file, "Expected not-null file");
        checkNotNull(model, "Expected not-null model");

        log.info("Reading file name: [{}]", file.getOriginalFilename());

        SolidityFile savedSolidityFile = solidityService.save(file.getBytes());

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getSolidityFunctions());
        model.addAttribute("solidityFileHash", savedSolidityFile.getSourceCodeHash());
        return "solidity-reader";
    }

    @PostMapping("/text")
    public String handleSourceCodeUpload(@RequestParam("sourceCode") String sourceCode, Model model) throws Exception {
        checkNotNull(sourceCode, "Expected not-null sourceCode");
        checkNotNull(model, "Expected not-null model");

        SolidityFile savedSolidityFile = solidityService.save(sourceCode);

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getSolidityFunctions());
        model.addAttribute("solidityFileHash", savedSolidityFile.getSourceCodeHash());
        return "solidity-reader";
    }

    @GetMapping("/files")
    public String findSolidityFiles(Model model) {
        checkNotNull(model, "Expected not-null model");

        List<SolidityFile> solidityFiles = solidityService.findAllFiles();

        model.addAttribute("solidityFiles", solidityFiles);
        return "solidity-files";
    }

    @GetMapping("/solidityFunctions")
    public String findFunctions(Model model) {
        checkNotNull(model, "Expected not-null model");

        List<SolidityFunction> solidityFunctions = solidityService.findAllUniqueFunctions();

        model.addAttribute("solidityFunctions", solidityFunctions);
        return "solidity-functions";
    }
}
