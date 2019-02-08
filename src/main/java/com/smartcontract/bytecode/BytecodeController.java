package com.smartcontract.bytecode;


import com.smartcontract.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

import static com.smartcontract.Util.checkNotNull;


@Controller
@RequestMapping("/bytecode")
public class BytecodeController {

    private final BytecodeService bytecodeService;

    @Autowired
    public BytecodeController(BytecodeService bytecodeService) {
        checkNotNull(bytecodeService, "Expected not-null bytecodeService");
        this.bytecodeService = bytecodeService;
    }

    @PostMapping
    public String findTop10FileHashesByBytecode(
            @RequestParam(value = "bytecode") String bytecode,
            @RequestParam(value = "allFiles") boolean allFiles,
            Model model) {
        checkNotNull(bytecode, "Expected not-null bytecode");
        checkNotNull(model, "Expected not-null model");

        model.addAttribute("actualAllFiles", allFiles);
        model.addAttribute("actualBytecode", bytecode);

        List<Pair<String, Double>> implementations;
        if (isSearchTopTenFiles(allFiles)) {
            implementations = bytecodeService.findTop10FileHashesWithValueOfMatch(bytecode);
            model.addAttribute("implementationsWithValueOfMatch", implementations);
        } else {
            implementations = bytecodeService.findAllFileHashesWithValueOfMatch(bytecode);
            model.addAttribute("implementationsWithValueOfMatch", implementations);
        }

        if (implementations.isEmpty()) {
            model.addAttribute("message", "No implementation was found");
        }

        return "bytecode-page";
    }

    @GetMapping
    public String showpage(Model model) {
        model.addAttribute("implementationsWithValueOfMatch", new HashMap<>());
        return "bytecode-page";
    }

    private boolean isSearchTopTenFiles(Boolean allFiles) {
        return !allFiles;
    }
}
