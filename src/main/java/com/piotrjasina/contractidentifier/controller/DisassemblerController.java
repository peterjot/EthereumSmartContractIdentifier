package com.piotrjasina.contractidentifier.controller;

import com.piotrjasina.contractidentifier.disassembler.Instruction;
import com.piotrjasina.contractidentifier.disassembler.SolidityDisassembler;
import com.piotrjasina.contractidentifier.dto.InstructionDto;
import com.piotrjasina.contractidentifier.mapper.InstructionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class DisassemblerController {

    private final SolidityDisassembler solidityDisassembler;

    @Autowired
    public DisassemblerController(SolidityDisassembler solidityDisassembler) {
        this.solidityDisassembler = solidityDisassembler;
    }


    @GetMapping("/disassembly/{bytecode}")
    public List<InstructionDto> disassembly(@PathVariable("bytecode") String byteCode, @RequestParam(value = "mnemonic", required = false) String mnemonic) {
        List<Instruction> instructions = solidityDisassembler.disassembly(byteCode);

        return instructions
                .stream()
                .filter(instruction -> filterWhenMnemonicSet(mnemonic, instruction.getOpcode().name()))
                .map(InstructionMapper::toInstructionDto)
                .collect(toList());
    }

    private boolean filterWhenMnemonicSet(String mnemonic, String name) {
        if (mnemonic != null) {
            return name.equals(mnemonic);
        }
        return true;
    }

}
