package com.smartcontract;

import com.smartcontract.bytecode.BytecodeService;
import com.smartcontract.bytecode.dto.IdentifiedSolidityFileDto;
import com.smartcontract.solidity.SolidityService;
import com.smartcontract.solidity.dto.SolidityFileDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class SolidityApiController {

    @NonNull
    private final BytecodeService bytecodeService;

    @NonNull
    private final SolidityService solidityService;


    @PostMapping("/api/bytecode")
    public ResponseEntity<List<IdentifiedSolidityFileDto>> findTop10FileHashesByBytecode(
            @NonNull @RequestBody String bytecode) {

        List<IdentifiedSolidityFileDto> implementations = bytecodeService.findTop10FileHashesWithValueOfMatch(bytecode);

        if (implementations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(implementations);
    }

    @PostMapping("/api/solidityFiles")
    public ResponseEntity<SolidityFileDto> uploadFile(@NonNull @RequestBody String sourceCode) throws IOException {
        return ResponseEntity.ok(solidityService.save(sourceCode));
    }

    @GetMapping(value = "/api/sourceCode/{fileHash}.sol", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSourceCodeByHash(@NonNull @PathVariable("fileHash") String fileHash) {
        return solidityService
                .findSourceCodeByHash(fileHash)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
