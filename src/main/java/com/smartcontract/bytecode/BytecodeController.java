package com.smartcontract.bytecode;


import com.smartcontract.solidity.IdentifiedSolidityFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;


@Controller
public class BytecodeController {

    private final BytecodeService bytecodeService;


    @Autowired
    public BytecodeController(BytecodeService bytecodeService) {
        requireNonNull(bytecodeService, "Expected not-null bytecodeService");
        this.bytecodeService = bytecodeService;
    }

    @PostMapping("/bytecode")
    public String findTop10FileHashesByBytecode(
            @RequestParam(value = "bytecode") String bytecode,
            @RequestParam(value = "allFiles") boolean allFiles,
            Model model) {
        requireNonNull(bytecode, "Expected not-null bytecode");
        requireNonNull(model, "Expected not-null model");

        model.addAttribute("actualAllFiles", allFiles);
        model.addAttribute("actualBytecode", bytecode);

        List<IdentifiedSolidityFileDto> implementations;
        if (allFiles) {
            implementations = bytecodeService.findAllFileHashesWithValueOfMatch(bytecode);
        } else {
            implementations = bytecodeService.findTop10FileHashesWithValueOfMatch(bytecode);
        }
        model.addAttribute("implementationsWithValueOfMatch", implementations);

        if (implementations.isEmpty()) {
            model.addAttribute("message", "No implementation was found");
        }

        return "bytecode-page";
    }

    @GetMapping("/bytecode")
    public String showPage(Model model) {
        requireNonNull(model, "Expected not-null model");

        model.addAttribute("implementationsWithValueOfMatch", new HashMap<>());
        return "bytecode-page";
    }
}
