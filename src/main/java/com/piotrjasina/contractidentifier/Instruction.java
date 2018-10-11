package com.piotrjasina.contractidentifier;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import org.apache.tomcat.util.buf.HexUtils;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

    @Getter
    private Opcode opcode;
    private List<Byte> argumentBytes = new ArrayList<>();

    public Instruction(Opcode opcode) {
        this.opcode = opcode;
    }

    public void addArgumentByte(byte b) {
        argumentBytes.add(b);
    }

    @Override
    public String toString() {
        if (argumentBytes.size() == 0) {
            return opcode.name() + "\n";
        }
        return opcode.name() + " 0x" + bytesToHexString() + "\n";

    }

    private String bytesToHexString() {
        return HexUtils.toHexString(byteListToArray());
    }

    private byte[] byteListToArray() {
        return Bytes.toArray(argumentBytes);
    }
}
