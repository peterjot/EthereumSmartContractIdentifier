package com.smartcontract.bytecode;


import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

import static lombok.Lombok.checkNotNull;


@Controller
@RequestMapping("/bytecode")
public class BytecodeController {

    private static final String NO_IMPL_MESSAGE = "Any implementation was found";

    private final BytecodeService bytecodeService;

    @Autowired
    public BytecodeController(BytecodeService bytecodeService) {
        checkNotNull(bytecodeService, "Expected not-null bytecodeService");
        this.bytecodeService = bytecodeService;
    }

    @PostMapping
    public String findImplementationsByBytecode(@RequestParam("bytecode") String bytecode, Model model) {
        checkNotNull(bytecode, "Expected not-null bytecode");
        checkNotNull(model, "Expected not-null model");

        List<Pair<String, Double>> implementationsWithCount = bytecodeService.findFileHashWithPercentageOfMatch(bytecode);

        model.addAttribute("implementationsWithCount", implementationsWithCount);
        if (implementationsWithCount.isEmpty()) {
            model.addAttribute("message", NO_IMPL_MESSAGE);
        }

        return "bytecode-reader";
    }

    @GetMapping
    public String greeting(Model model) {
        checkNotNull(model, "Expected not-null model");

        model.addAttribute("implementationsWithCount", new HashMap<>());
        return "bytecode-reader";
    }
}
