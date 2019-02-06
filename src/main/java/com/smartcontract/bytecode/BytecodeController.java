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
    public String findFileHashesByBytecode(@RequestParam("bytecode") String bytecode, Model model) {
        checkNotNull(bytecode, "Expected not-null bytecode");
        checkNotNull(model, "Expected not-null model");

        List<Pair<String, Double>> implementations = bytecodeService.findFileHashWithValueOfMatch(bytecode);

        model.addAttribute("implementationsWithValueOfMatch", implementations);
        if (implementations.isEmpty()) {
            model.addAttribute("message", "No implementation was found");
        }

        return "bytecode-reader";
    }

    @GetMapping
    public String showPage(Model model) {
        checkNotNull(model, "Expected not-null model");

        model.addAttribute("implementationsWithValueOfMatch", new HashMap<>());
        return "bytecode-reader";
    }
}
