package io.github.peterjot.bytecode;


import io.github.peterjot.bytecode.dto.IdentifiedSolidityFileDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BytecodeController {

    @NonNull
    private final BytecodeService bytecodeService;


    @PostMapping("/bytecode")
    public String findTop10FileHashesByBytecode(
            @NonNull @RequestParam(value = "solidity") String bytecode,
            @NonNull Model model) {

        List<IdentifiedSolidityFileDto> implementations = bytecodeService.findTop10FileHashesWithValueOfMatch(bytecode);
        model.addAttribute("implementationsWithValueOfMatch", implementations);
        model.addAttribute("actualBytecode", bytecode);

        if (implementations.isEmpty()) {
            model.addAttribute("message", "No implementation was found");
        }

        return "solidity-page";
    }

    @GetMapping("/bytecode")
    public String showPage(@NonNull Model model) {
        model.addAttribute("implementationsWithValueOfMatch", new HashMap<>());
        return "solidity-page";
    }
}
