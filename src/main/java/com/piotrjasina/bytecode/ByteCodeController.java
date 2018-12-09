package com.piotrjasina.bytecode;


import com.piotrjasina.solidity.SolidityFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/bytecode")
public class ByteCodeController {

    private final ByteCodeService byteCodeService;

    @Autowired
    public ByteCodeController(ByteCodeService byteCodeService) {
        this.byteCodeService = byteCodeService;
    }

    @PostMapping
    public String handleByteCodeUpload(@RequestParam("byteCodeString") String byteCodeString, Model model) {
        Map<SolidityFile, Double> implementationsWithCount = byteCodeService.findSolidityFileWithCountByByteCode(byteCodeString);

        model.addAttribute("implementationsWithCount", implementationsWithCount);
        return "bytecode-reader";
    }

    @GetMapping
    public String greeting() {
        return "bytecode-reader";
    }
}
