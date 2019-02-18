package com.smartcontract.solidity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.Objects.requireNonNull;


@Controller
@RequestMapping("/solidity")
public class SolidityController {

    private final SolidityService solidityService;

    @Autowired
    public SolidityController(SolidityService solidityService) {
        requireNonNull(solidityService, "Expected not-null solidityService");
        this.solidityService = solidityService;
    }

    @GetMapping
    public String showPage() {
        return "solidity-page";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        requireNonNull(file, "Expected not-null file");
        requireNonNull(model, "Expected not-null model");

        SolidityFile savedSolidityFile = solidityService.save(file.getBytes());

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getSolidityFunctions());
        model.addAttribute("solidityFileHash", savedSolidityFile.getSourceCodeHash());
        return "solidity-page";
    }

    @PostMapping("/text")
    public String handleSourceCodeUpload(@RequestParam("sourceCode") String sourceCode, Model model) throws Exception {
        requireNonNull(sourceCode, "Expected not-null sourceCode");
        requireNonNull(model, "Expected not-null model");

        SolidityFile savedSolidityFile = solidityService.save(sourceCode);

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getSolidityFunctions());
        model.addAttribute("solidityFileHash", savedSolidityFile.getSourceCodeHash());
        return "solidity-page";
    }

    @GetMapping(value = "/sourceCode", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSourceCodeByHash(@RequestParam("fileHash") String fileHash, Model model) {
        requireNonNull(fileHash, "Expected not-null fileHash");

        Optional<String> sourceCodeByHash = solidityService.findSourceCodeByHash(fileHash);
        if (sourceCodeByHash.isPresent()) {
            model.addAttribute("sourceCode", sourceCodeByHash.get());
        } else {
            model.addAttribute("sourceCode", "Source code not found");
        }
        return "source-code";
    }
}
