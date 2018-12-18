package com.smartcontract.bytecode.disassembler;

import com.smartcontract.exception.BytecodeStringException;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.smartcontract.bytecode.disassembler.OpcodeTable.getOpcodeByHex;
import static java.lang.String.format;

@Slf4j
public class SolidityDisassembler {

    public static List<Instruction> disassembly(String bytecode) {
        checkNotNull(bytecode, "Expected not-null bytecode");
        return createInstructions(bytecode);
    }

    private static List<Instruction> createInstructions(String bytecode) {
        String preparedBytecode = prepareBytecode(bytecode);

        List<Instruction> instructions = new ArrayList<>();
        HexBytecodeIterator hexBytecodeIterator = new HexBytecodeIterator(preparedBytecode);

        while (hexBytecodeIterator.hasNext()) {
            Opcode opcode = getOpcodeByHex(hexBytecodeIterator.next());

            String instructionParameter = getInstructionParameter(opcode.getOperandSize(), hexBytecodeIterator);

            Instruction instruction = new Instruction(opcode, instructionParameter.toLowerCase());

            instructions.add(instruction);
        }
        return instructions;
    }

    private static String prepareBytecode(String bytecode) {
        checkBytecodeLength(bytecode);
        if (bytecode.startsWith("0x")) {
            return bytecode.substring(2).toLowerCase();
        }
        return bytecode;
    }

    private static void checkBytecodeLength(String bytecode) {
        if (bytecode.length() % 2 != 0) {
            throw new BytecodeStringException(format("This bytecode has wrong length [%d]", bytecode.length()));
        }
    }

    private static String getInstructionParameter(int parametersSize, HexBytecodeIterator iterator) {
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

    private static String fillOthersBytesWithZero(int parametersCounter) {
        int parameterOthersCharsCount = 2 * parametersCounter;
        return StringUtils.repeat("0", parameterOthersCharsCount);
    }
}
