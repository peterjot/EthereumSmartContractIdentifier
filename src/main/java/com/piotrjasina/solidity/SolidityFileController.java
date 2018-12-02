package com.piotrjasina.solidity;

import com.piotrjasina.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

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
        log.info("Get solidity page");

        return "solidity-reader";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        log.info("Posted file");
        SolidityFile savedSolidityFile = solidityFileService.save(file.getBytes());

        model.addAttribute("functions", savedSolidityFile.getFunctions());
        return "solidity-reader";
    }

    @GetMapping("/all")
    public String findAll(Model model){
        List<SolidityFile> solidityFiles = solidityFileService.findAll();
        model.addAttribute("solidityFiles", solidityFiles);
        return "solidity-view";
    }

}
