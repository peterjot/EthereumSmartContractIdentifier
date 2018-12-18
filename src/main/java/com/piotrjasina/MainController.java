package com.piotrjasina;

import com.piotrjasina.solidity.SolidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final SolidityService solidityService;

    @Autowired
    public MainController(SolidityService solidityService) {
        this.solidityService = solidityService;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("filesCount", solidityService.getSolidityFilesCount());

        return "index";
    }
}