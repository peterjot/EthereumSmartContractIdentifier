package com.smartcontract.bytecode;


import com.smartcontract.solidity.IdentifiedSolidityFileDto;
import lombok.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;


@Controller
public class BytecodeController {

    private final BytecodeService bytecodeService;


    public BytecodeController(@NonNull BytecodeService bytecodeService) {
        this.bytecodeService = bytecodeService;
    }

    @PostMapping("/bytecode")
    public String findTop10FileHashesByBytecode(
            @NonNull @RequestParam(value = "bytecode") String bytecode,
            @NonNull Model model) {

        List<IdentifiedSolidityFileDto> implementations = bytecodeService.findTop10FileHashesWithValueOfMatch(bytecode);
        model.addAttribute("implementationsWithValueOfMatch", implementations);
        model.addAttribute("actualBytecode", bytecode);

        if (implementations.isEmpty()) {
            model.addAttribute("message", "No implementation was found");
        }

        return "bytecode-page";
    }

    @GetMapping("/bytecode")
    public String showPage(@NonNull Model model) {
        model.addAttribute("implementationsWithValueOfMatch", new HashMap<>());
        return "bytecode-page";
    }
}
