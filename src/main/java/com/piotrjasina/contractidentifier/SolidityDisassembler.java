package com.piotrjasina.contractidentifier;

import org.apache.tomcat.util.buf.HexUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class SolidityDisassembler {

    private Map<Integer, Instruction> opcodes;

    public SolidityDisassembler() {
        opcodes = Arrays
                .stream(Instruction.values())
                .collect(toMap(Instruction::getMaskedOpcode, Function.identity()));
    }


    public void disassembly(String bytecodeSource) {
        String bytecode = bytecodeSource;
        if (bytecode.startsWith("0x")) {
            bytecode = bytecode.substring(2);
        }

        if (bytecode.length() % 2 != 0) {
            throw new RuntimeException("This bytecode have wrong length");
        }

        byte[] ffs = HexUtils.fromHexString(bytecode);

        for (int i=0; i<ffs.length ; i++) {
            Instruction instruction = opcodes.get(ffs[i] & 0xFF);

            if(instruction == null){
                System.out.println(String.format("0x%02X", ffs[i] & 0xFF));
                continue;
            }

            System.out.print("Operation: "+ instruction.name());
            if(instruction.getOperandSize()>0){
                System.out.print(" 0x");
            }
            for (int opSize = 0; opSize < instruction.getOperandSize(); opSize++) {
                i++;
                if(i>=ffs.length)
                    System.out.print(getStringFromByte((byte) 0x0));
                else
                    System.out.print(getStringFromByte(ffs[i]));
            }
            System.out.print("\n");

        }
    }

    private String getStringFromByte(byte ff) {
        String s = Long.toHexString(ff & 0xFF);
        return s.length()==1?"0"+s:s;
    }
}
