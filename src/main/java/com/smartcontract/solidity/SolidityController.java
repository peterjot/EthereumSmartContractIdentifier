package com.smartcontract.solidity;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static com.smartcontract.Util.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;


@Controller
@RequestMapping("/solidity")
public class SolidityController {

    private static final Logger LOGGER = getLogger(SolidityController.class);

    private final SolidityService solidityService;

    @Autowired
    public SolidityController(SolidityService solidityService) {
        checkNotNull(solidityService, "Expected not-null solidityService");
        this.solidityService = solidityService;
    }

    @GetMapping
    public String showPage() {
        return "solidity-reader";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        checkNotNull(file, "Expected not-null file");
        checkNotNull(model, "Expected not-null model");

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
}
