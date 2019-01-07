package com.smartcontract.api;

import com.smartcontract.solidity.SolidityFile;
import com.smartcontract.solidity.SolidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/api")
public class SolidityApiController {


    private final SolidityService solidityService;

    @Autowired
    public SolidityApiController(SolidityService solidityService) {
        checkNotNull(solidityService, "Expected not-null solidityService");
        this.solidityService = solidityService;
    }

    @PostMapping("/solidityFiles")
    public SolidityFile uploadFile(@RequestBody String sourceCode) throws IOException {
        checkNotNull(sourceCode, "Expected not-null sourceCode");
        return solidityService.save(sourceCode);
    }

    @GetMapping(value = "/solidityFiles/sourceCode", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSourceCodeByHash(@RequestParam("fileHash") String fileHash) {
        checkNotNull(fileHash, "Expected not-null fileHash");
        Optional<String> sourceCodeByHash = solidityService.findSourceCodeByHash(fileHash);
        return sourceCodeByHash
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/solidityFiles")
    public List<SolidityFile> findFiles() {
        return solidityService.findAllFiles();
    }
}
