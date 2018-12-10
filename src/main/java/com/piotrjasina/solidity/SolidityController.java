package com.piotrjasina.solidity;

import com.piotrjasina.solidity.function.Function;
import com.piotrjasina.solidity.solidityfile.SolidityFile;
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

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getFunctions());
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

    @GetMapping("/functions")
    public String findFunctions(Model model) {
        checkNotNull(model, "Expected not-null model");

        List<Function> functions = solidityService.findAllFunctions();

        model.addAttribute("functions", functions);
        return "solidity-functions";
    }

}
