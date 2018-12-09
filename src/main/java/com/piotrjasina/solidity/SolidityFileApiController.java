package com.piotrjasina.solidity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/api")
public class SolidityFileApiController {


    private final SolidityFileService solidityFileService;

    @Autowired
    public SolidityFileApiController(SolidityFileService solidityFileService) {
        this.solidityFileService = solidityFileService;
    }

    @PostMapping("/solidity/sourceCodes")
    public SolidityFile uploadFile(@RequestBody String sourceCode) throws IOException {
        checkNotNull(sourceCode, "Expected not-null sourceCode");

        return solidityFileService.save(sourceCode);
    }

    @GetMapping(value = "/solidity/sourceCodes", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getSourceCodeByHash(@RequestParam("fileHash") String fileHash) {
        checkNotNull(fileHash, "Expected not-null sourceCode");

        return solidityFileService.getSourceCodeByHash(fileHash);
    }

    @GetMapping("/solidity/functions")
    public List<Function> findFunctions() throws IOException {
        return solidityFileService.findAllFunctions();
    }

    @GetMapping("/solidity/files")
    public List<SolidityFile> findFiles() throws IOException {
        return solidityFileService.findAllFiles();
    }


}
