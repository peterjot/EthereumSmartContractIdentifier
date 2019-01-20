package com.smartcontract.disassembler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.smartcontract.disassembler.OpcodeTable.getOpcodeByHex;
import static java.lang.String.format;

@Slf4j
@Component
public class Disassembler {

    public Set<Instruction> disassembly(String bytecode) {
        checkNotNull(bytecode, "Expected not-null bytecode");
        return getInstructions(bytecode);
    }

    private Set<Instruction> getInstructions(String bytecode) {
        String preparedBytecode = prepareBytecode(bytecode);

        Set<Instruction> instructions = new LinkedHashSet<>();
        HexBytecodeIterator hexBytecodeIterator = new HexBytecodeIterator(preparedBytecode);

        while (hexBytecodeIterator.hasNext()) {

            Opcode opcode = getOpcodeByHex(hexBytecodeIterator.next());

            String instructionParameter = getInstructionParameter(opcode.getOperandSize(), hexBytecodeIterator);

            Instruction instruction = new Instruction(opcode, instructionParameter.toLowerCase());

            instructions.add(instruction);
        }
        return instructions;
    }

    private String prepareBytecode(String bytecode) {
        checkBytecodeLength(bytecode);
        if (bytecode.startsWith("0x")) {
            return bytecode.substring(2).toLowerCase();
        }
        return bytecode;
    }

    private void checkBytecodeLength(String bytecode) {
        if (bytecode.length() % 2 != 0) {
            throw new BytecodeStringException(format("This bytecode has wrong length [%d]", bytecode.length()));
        }
    }

    private String getInstructionParameter(int parametersSize, HexBytecodeIterator iterator) {
        StringBuilder stringBuilder = new StringBuilder();

        int parametersCounter = parametersSize;
        while (iterator.hasNext() && parametersCounter > 0) {
            stringBuilder.append(iterator.next());
            parametersCounter--;
        }

        String othersParameterBytes = fillOthersBytesWithZero(parametersCounter);
        stringBuilder.append(othersParameterBytes);
        return stringBuilder.toString();
    }

    private String fillOthersBytesWithZero(int parametersCounter) {
        int parameterOthersCharsCount = 2 * parametersCounter;
        return StringUtils.repeat("0", parameterOthersCharsCount);
    }
}
