package com.piotrjasina.contractidentifier;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.piotrjasina.contractidentifier.OpcodeTable.getOpcodeByByte;

@Slf4j
class SolidityDisassembler {

    private List<Instruction> instructions = new ArrayList<>();

    public List<Instruction> disassembly(String bytesString) {
        checkNotNull(bytesString);

        bytesString = removePrefix(bytesString);

        checkByteSourceLength(bytesString);

        byte[] bytes = convertBytesStringToByteArray(bytesString);

        for (int i = 0; i < bytes.length; i++) {
            Opcode opcode = getOpcodeByByte(bytes[i]);

            Instruction instruction = new Instruction(opcode);

            for (int opSize = 0; opSize < opcode.getOperandSize(); opSize++) {
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

    private byte[] convertBytesStringToByteArray(String bytecodeSource) {
        return HexUtils.fromHexString(bytecodeSource);
    }

    private void checkByteSourceLength(String bytecodeSource) {
        if (bytecodeSource.length() % 2 != 0) {
            throw new RuntimeException("This bytecodeSource have wrong length");
        }
    }

    private String removePrefix(String bytecodeSource) {
        if (bytecodeSource.startsWith("0x")) {
            bytecodeSource = bytecodeSource.substring(2);
        }
        return bytecodeSource;
    }

}
