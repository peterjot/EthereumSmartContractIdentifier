package com.smartcontract.api;

import com.smartcontract.bytecode.BytecodeService;
import com.smartcontract.solidity.IdentifiedSolidityFileDto;
import com.smartcontract.solidity.SolidityFile;
import com.smartcontract.solidity.SolidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;


@RestController
@RequestMapping("/api")
public class SolidityApiController {

    private final BytecodeService bytecodeService;
    private final SolidityService solidityService;

    @Autowired
    public SolidityApiController(BytecodeService bytecodeService, SolidityService solidityService) {
        requireNonNull(bytecodeService, "Expected not-null bytecodeService");
        requireNonNull(solidityService, "Expected not-null solidityService");
        this.bytecodeService = bytecodeService;
        this.solidityService = solidityService;
    }

    @PostMapping("/bytecode")
    public ResponseEntity<List<IdentifiedSolidityFileDto>> findTop10FileHashesByBytecode(
            @RequestParam(value = "bytecode") String bytecode,
            @RequestParam(value = "allFiles") boolean allFiles) {
        requireNonNull(bytecode, "Expected not-null bytecode");


        List<IdentifiedSolidityFileDto> implementations;
        if (allFiles) {
            implementations = bytecodeService.findAllFileHashesWithValueOfMatch(bytecode);
        } else {
            implementations = bytecodeService.findTop10FileHashesWithValueOfMatch(bytecode);
        }

        if (implementations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(implementations);
    }

    @PostMapping("/solidityFiles")
    public ResponseEntity<SolidityFile> uploadFile(@RequestBody String sourceCode) throws IOException {
        requireNonNull(sourceCode, "Expected not-null sourceCode");
        return ResponseEntity.ok(solidityService.save(sourceCode));
    }

    @GetMapping(value = "/solidityFiles/sourceCode", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSourceCodeByHash(@RequestParam("fileHash") String fileHash) {
        requireNonNull(fileHash, "Expected not-null fileHash");

        Optional<String> sourceCodeByHash = solidityService.findSourceCodeByHash(fileHash);
        return sourceCodeByHash
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/solidityFiles")
    public ResponseEntity<List<SolidityFile>> findFiles() {
        return ResponseEntity.ok(solidityService.findAllFiles());
    }
}
