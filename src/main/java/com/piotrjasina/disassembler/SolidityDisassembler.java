package com.piotrjasina.disassembler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Component
public class SolidityDisassembler {


    public List<Instruction> disassembly(String byteCode) {
        checkNotNull(byteCode);
        return createInstructions(byteCode);
    }

    private List<Instruction> createInstructions(String byteCode) {
        String byteCodeWithoutPrefix = removePrefixAndToLower(byteCode);
        checkByteCodeLength(byteCodeWithoutPrefix);


        List<Instruction> instructions = new ArrayList<>();

        int byteCodeBytesCount = byteCodeWithoutPrefix.length() / 2;
        log.info("ByteCodeBytesCount: {}", byteCodeBytesCount);

        for (int i = 0; i < byteCodeBytesCount * 2; i += 2) {
            log.info("Lop i: {}", i);

            String byteString = byteCodeWithoutPrefix.substring(i, i + 2);
            log.info("Current byteString: {}", byteString);

            Opcode opcode = OpcodeTable.getOpcodeByByte(byteString);
            log.info("Current opcode: {}", opcode.name());

//            StringBuilder hexArg = new StringBuilder();
            byte[] bytes;
            if (i + opcode.getOperandSize() * 2 + 2 > byteCodeWithoutPrefix.length() - 1) {
                int ileBrak = i + opcode.getOperandSize() * 2 + 2 - byteCodeWithoutPrefix.length();
                String s = StringUtils.repeat("0", ileBrak);
                bytes = DatatypeConverter.parseHexBinary((byteCodeWithoutPrefix + s).substring(i + 2, i + opcode.getOperandSize() * 2 + 2));
            } else {
                bytes = DatatypeConverter.parseHexBinary(byteCodeWithoutPrefix.substring(i + 2, i + opcode.getOperandSize() * 2 + 2));
            }
//
//            for (int j = 0; j < opcode.getOperandSize(); j++) {
//                i+=2;
//                if (i >= byteCodeBytesCount)
//                    hexArg.append("00");
//                else
//                    hexArg.append(byteCodeWithoutPrefix.substring(i, i + 2));
//            }
            Instruction instruction = new Instruction(opcode, bytes);
            log.info("Current argument: {}", DatatypeConverter.printHexBinary(bytes));

            instructions.add(instruction);
            i = i + opcode.getOperandSize() * 2;
        }
        return instructions;
    }

    private void checkByteCodeLength(String bytecodeSource) {
        if (bytecodeSource.length() % 2 != 0) {
            throw new RuntimeException("This bytecodeSource has wrong length");
        }
    }

    private String removePrefixAndToLower(String bytecodeSource) {
        if (bytecodeSource.startsWith("0x")) {
            return bytecodeSource.substring(2).toLowerCase();
        }
        return bytecodeSource;
    }

}
