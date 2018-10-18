package com.piotrjasina.api.disassembler;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.piotrjasina.api.disassembler.OpcodeTable.getOpcodeByByte;

@Component
public class SolidityDisassembler {


    public List<Instruction> disassembly(String byteCode) {
        checkNotNull(byteCode);
        return createInstructions(byteCode);
    }

    private List<Instruction> createInstructions(String byteCode) {
        String byteCodeWithoutPrefix = removePrefix(byteCode);
        checkByteCodeLength(byteCodeWithoutPrefix);

        byte[] bytes = convertStringToByteArray(byteCodeWithoutPrefix);

        List<Instruction> instructions = new ArrayList<>();

        for (int i = 0; i < bytes.length; i++) {
            Opcode opcode = getOpcodeByByte(bytes[i]);

            Instruction instruction = new Instruction(opcode, bytes[i]);

            for (int j = 0; j < opcode.getOperandSize(); j++) {
                i++;
                if (i >= bytes.length)
                    instruction.addArgumentByte((byte) 0);
                else
                    instruction.addArgumentByte(bytes[i]);
            }
            instructions.add(instruction);
        }
        return instructions;
    }

    private byte[] convertStringToByteArray(String bytecodeSource) {
        return HexUtils.fromHexString(bytecodeSource);
    }

    private void checkByteCodeLength(String bytecodeSource) {
        if (bytecodeSource.length() % 2 != 0) {
            throw new RuntimeException("This bytecodeSource has wrong length");
        }
    }

    private String removePrefix(String bytecodeSource) {
        if (bytecodeSource.startsWith("0x")) {
            return bytecodeSource.substring(2);
        }
        return bytecodeSource;
    }

}
