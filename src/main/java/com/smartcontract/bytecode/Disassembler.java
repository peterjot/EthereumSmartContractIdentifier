package com.smartcontract.bytecode;

import lombok.NonNull;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.smartcontract.bytecode.Opcode.getOpcodeByHex;
import static java.util.Collections.unmodifiableList;


class Disassembler {

    List<Instruction> disassembly(@NonNull String bytecode) {
        return getInstructions(bytecode);
    }

    private List<Instruction> getInstructions(String bytecode) {
        String validBytecode = getValidBytecode(bytecode);
        HexStringIterator hexStringIterator = new HexStringIterator(validBytecode);

        List<Instruction> instructions = new ArrayList<>();
        while (hexStringIterator.hasNext()) {
            Opcode opcode = getOpcodeByHex(hexStringIterator.next());
            String instructionParameter = getInstructionOperand(opcode.getOperandSize(), hexStringIterator);
            instructions.add(new Instruction(opcode, instructionParameter));
        }
        return unmodifiableList(instructions);
    }

    private String getValidBytecode(String bytecode) {
        checkBytecodeLength(bytecode);
        return bytecode.startsWith("0x") ? bytecode.substring(2).toLowerCase() : bytecode.toLowerCase();
    }

    private void checkBytecodeLength(String bytecode) {
        if (bytecode.length() % 2 != 0) {
            throw new IllegalStateException("Expected solidity with even number of characters");
        }
    }

    private String getInstructionOperand(int operandSize, HexStringIterator iterator) {
        StringBuilder stringBuilder = new StringBuilder();

        int i = operandSize;
        while (iterator.hasNext() && i > 0) {
            stringBuilder.append(String.format("%02X", iterator.next()));
            i--;
        }
        stringBuilder.append(StringUtils.repeat("00", i));

        return stringBuilder.toString();
    }
}
