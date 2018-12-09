package com.piotrjasina;

import com.piotrjasina.solidity.FunctionRepository;
import com.piotrjasina.solidity.SolidityFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final SolidityFileRepository solidityFileRepository;
    private final FunctionRepository functionRepository;

    @Autowired
    public MainController(SolidityFileRepository solidityFileRepository, FunctionRepository functionRepository) {
        this.solidityFileRepository = solidityFileRepository;
        this.functionRepository = functionRepository;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {
        long solidityFilesCount = solidityFileRepository.count();
        long functionsCount = functionRepository.count();

        model.addAttribute("filesCount", solidityFilesCount);
        model.addAttribute("functionsCount", functionsCount);
        return "index";
    }
}