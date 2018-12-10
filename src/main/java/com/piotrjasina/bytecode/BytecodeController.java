package com.piotrjasina.bytecode;


import com.piotrjasina.solidity.solidityfile.SolidityFile;
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
public class BytecodeController {

    private final BytecodeService bytecodeService;

    @Autowired
    public BytecodeController(BytecodeService bytecodeService) {
        this.bytecodeService = bytecodeService;
    }

    @PostMapping
    public String findImplementationsByBytecode(@RequestParam("bytecode") String bytecode, Model model) {
        Map<SolidityFile, Double> implementationsWithCount = bytecodeService.findSolidityFileWithCountByBytecode(bytecode);

        model.addAttribute("implementationsWithCount", implementationsWithCount);
        return "bytecode-reader";
    }

    @GetMapping
    public String greeting() {
        return "bytecode-reader";
    }
}
