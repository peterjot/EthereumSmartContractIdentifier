package com.smartcontract.disassembler;

import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.smartcontract.disassembler.OpcodeTable.getOpcodeByHex;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

@Component
public class Disassembler {

    public List<Instruction> disassembly(String bytecode) {
        requireNonNull(bytecode, "Expected not-null bytecode");
        return getInstructions(bytecode);
    }

    private List<Instruction> getInstructions(String bytecode) {
        String validBytecode = getValidBytecode(bytecode);
        HexStringIterator hexStringIterator = new HexStringIterator(validBytecode);

        List<Instruction> instructions = new ArrayList<>();
        while (hexStringIterator.hasNext()) {
            Opcode opcode = getOpcodeByHex(hexStringIterator.next());
            String instructionParameter = getInstructionOperand(opcode.getOperandSize(), hexStringIterator);
            instructions.add(new Instruction(opcode, instructionParameter.toLowerCase()));
        }
        return unmodifiableList(instructions);
    }

    private String getValidBytecode(String bytecode) {
        checkBytecodeLength(bytecode);
        return bytecode.startsWith("0x") ? bytecode.substring(2).toLowerCase() : bytecode.toLowerCase();
    }

    private void checkBytecodeLength(String bytecode) {
        if (bytecode.length() % 2 != 0) {
            throw new IllegalStateException("Expected bytecode with even number of characters");
        }
    }

    private String getInstructionOperand(int operandSize, HexStringIterator iterator) {
        StringBuilder stringBuilder = new StringBuilder();

        int i = operandSize;
        while (iterator.hasNext() && i > 0) {
            stringBuilder.append(String.format("%02X", iterator.next()));
            i--;
        }
        stringBuilder.append(StringUtils.repeat("0", i * 2));

        return stringBuilder.toString();
    }
}
