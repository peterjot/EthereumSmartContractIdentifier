package com.smartcontract;

import com.smartcontract.solidity.SolidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static java.util.Objects.requireNonNull;


@Controller
public class MainController {

    private final SolidityService solidityService;

    @Autowired
    public MainController(SolidityService solidityService) {
        requireNonNull(solidityService, "Expected not-null solidityService");
        this.solidityService = solidityService;
    }

    @GetMapping("/")
    public String showPage(Model model) {
        requireNonNull(model, "Expected not-null model");

        model.addAttribute("filesCount", solidityService.getSolidityFilesCount());
        return "index";
    }
}
