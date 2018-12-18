package com.smartcontract;

import com.smartcontract.solidity.SolidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
public class MainController {

    private final SolidityService solidityService;

    @Autowired
    public MainController(SolidityService solidityService) {
        checkNotNull(solidityService, "Expected not-null solidityService");
        this.solidityService = solidityService;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {
        checkNotNull(model, "Expected not-null model");

        model.addAttribute("filesCount", solidityService.getSolidityFilesCount());
        return "index";
    }
}