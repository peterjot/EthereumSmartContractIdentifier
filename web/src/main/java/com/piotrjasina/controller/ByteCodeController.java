package com.piotrjasina.controller;


import com.piotrjasina.dto.InstructionDto;
import com.piotrjasina.service.ByteCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/bytecode")
public class ByteCodeController {

    private static final String MNEMONIC = "PUSH4";
    private final ByteCodeService byteCodeService;

    @Autowired
    public ByteCodeController(ByteCodeService byteCodeService) {
        this.byteCodeService = byteCodeService;
    }

    @PostMapping
    public String handleByteCodeUpload(@RequestParam("byteCodeString") String byteCodeString, Model model) {
        log.info("ByteCodeString: {}", byteCodeString.substring(0, 100));

        List<InstructionDto> instructionsDto = byteCodeService.getOpcodeArgumentByMnemonic(byteCodeString, MNEMONIC);

        log.info("InstructionsDto: {}", instructionsDto);
        model.addAttribute("instructionsDtos", instructionsDto);
        model.addAttribute("byteCodeString", byteCodeString);
        return "bytecode-reader";
    }

    @GetMapping
    public String greeting() {
        return "bytecode-reader";
    }
}
