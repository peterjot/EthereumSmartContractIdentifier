package com.smartcontract;

import com.smartcontract.solidity.SolidityService;
import com.smartcontract.solidity.dto.SolidityFileDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;


@Controller
@RequiredArgsConstructor
public class SolidityController {

    @NonNull
    private final SolidityService solidityService;


    @GetMapping("/")
    public String showPage(@NonNull Model model) {
        model.addAttribute("filesCount", solidityService.getSolidityFilesCount());
        return "index";
    }

    @GetMapping("/solidity")
    public String showPage() {
        return "solidity-page";
    }

    @PostMapping("/solidity")
    public String handleFileUpload(@NonNull @RequestParam("file") MultipartFile file,
                                   @NonNull Model model) throws IOException {
        SolidityFileDto savedSolidityFile = solidityService.save(file.getBytes());

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getSolidityFunctions());
        model.addAttribute("solidityFileHash", savedSolidityFile.getSourceCodeHash());

        return "solidity-page";
    }

    @PostMapping("/solidity/text")
    public String handleSourceCodeUpload(@NonNull @RequestParam("sourceCode") String sourceCode,
                                         @NonNull Model model) throws IOException {

        SolidityFileDto savedSolidityFile = solidityService.save(sourceCode);

        model.addAttribute("solidityFileFunctions", savedSolidityFile.getSolidityFunctions());
        model.addAttribute("solidityFileHash", savedSolidityFile.getSourceCodeHash());

        return "solidity-page";
    }

    @GetMapping(value = "/solidity/sourceCode", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSourceCodeByHash(@NonNull @RequestParam("fileHash") String fileHash,
                                      @NonNull Model model) {

        return solidityService.findSourceCodeByHash(fileHash)
                .map(foundSourceCode(fileHash, model))
                .orElseGet(notFoundSourceCode(model));
    }

    private Supplier<String> notFoundSourceCode(Model model) {
        return () -> {
            model.addAttribute("sourceCode", "Source code not found");
            return "source-code";
        };
    }

    private Function<String, String> foundSourceCode(String fileHash, Model model) {
        return sourceCode -> {
            model.addAttribute("sourceCode", sourceCode);
            model.addAttribute("sourceCodeHash", fileHash);
            return "source-code";
        };
    }
}
