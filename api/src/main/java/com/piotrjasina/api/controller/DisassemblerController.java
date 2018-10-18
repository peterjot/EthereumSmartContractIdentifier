package com.piotrjasina.api.controller;

import com.piotrjasina.api.dto.InstructionDto;
import com.piotrjasina.api.service.DisassemblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disassembler")
public class DisassemblerController {

    private final DisassemblerService disassemblerService;

    @Autowired
    public DisassemblerController(DisassemblerService disassemblerService) {
        this.disassemblerService = disassemblerService;
    }

    @GetMapping
    public String hello() {
        return "Welcome in disassembler api";
    }

    @GetMapping("/{bytecode}")
    public ResponseEntity<List<InstructionDto>> disassembly(@PathVariable("bytecode") String byteCode, @RequestParam(value = "mnemonic", required = false) String mnemonic) {

        List<InstructionDto> instructionDtos;
        if (mnemonic == null) {
            instructionDtos = disassemblerService.dissassemblyWithoutArgument(byteCode);
        } else {
            instructionDtos = disassemblerService.dissassemblyByMnemonic(byteCode, mnemonic);
        }

        if (instructionDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(instructionDtos);

    }


}
