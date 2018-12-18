package com.piotrjasina.api;

import com.piotrjasina.solidity.SolidityService;
import com.piotrjasina.solidity.solidityfile.SolidityFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/api")
public class SolidityApiController {


    private final SolidityService solidityService;

    @Autowired
    public SolidityApiController(SolidityService solidityService) {
        this.solidityService = solidityService;
    }

    @PostMapping("/solidityFiles")
    public SolidityFile uploadFile(@RequestBody String sourceCode) throws IOException {
        checkNotNull(sourceCode, "Expected not-null sourceCode");

        return solidityService.save(sourceCode);
    }

    @GetMapping(value = "/solidityFiles/sourceCode", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSourceCodeByHash(@RequestParam("fileHash") String fileHash) {
        checkNotNull(fileHash, "Expected not-null sourceCode");

        return solidityService.getSourceCodeByHash(fileHash);
    }

//    @GetMapping("/functions")
//    public List<Function> findFunctions() {
//        return solidityService.findAllFunctions();
//    }

    @GetMapping("/solidityFiles")
    public List<SolidityFile> findFiles() {
        return solidityService.findAllFiles();
    }


}
