package com.piotrjasina.contractidentifier.mapper;

import com.google.common.primitives.Bytes;
import com.piotrjasina.contractidentifier.disassembler.Instruction;
import com.piotrjasina.contractidentifier.dto.InstructionDto;
import org.apache.tomcat.util.buf.HexUtils;

import java.util.List;

public class InstructionMapper {

    public static InstructionDto toInstructionDto(Instruction instruction) {
        return new InstructionDto(
                instruction.getOpcode().name(),
                toArgumentString(instruction.getArgumentBytes()),
                unit8ToString(instruction.getUint8())
        );
    }


    private static String toArgumentString(List<Byte> argumentBytes) {
        if (argumentBytes.size() == 0) {
            return null;
        }
        return "0x" + bytesToHexString(argumentBytes);
    }

    private static String unit8ToString(int uint8) {
        return "0x" + String.format("%02X", uint8);
    }

    private static String bytesToHexString(List<Byte> argumentBytes) {
        byte[] bytes = Bytes.toArray(argumentBytes);
        return HexUtils.toHexString(bytes);
    }
}
