package com.piotrjasina.bytecode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;

@Slf4j
@Component
public class SolidityDisassembler {


    public List<Instruction> disassembly(String byteCode) {
        checkNotNull(byteCode);
        return createInstructions(byteCode);
    }

    private List<Instruction> createInstructions(String byteCode) {
        String preparedByteCode = prepareByteCode(byteCode);
        checkByteCodeLength(preparedByteCode);

        List<Instruction> instructions = new ArrayList<>();

        HexByteIterator hexByteIterator = new HexByteIterator(preparedByteCode);
        while (hexByteIterator.hasNext()) {

            String byteString = hexByteIterator.next();
            log.info("Current byteStr: {}", byteString);

            Opcode opcode = OpcodeTable.getOpcodeByHex(byteString);
            log.info("Current opcode: {}", opcode.name());

            byte[] bytes = parseHexBinary(
                    getParameter(opcode.getOperandSize(), hexByteIterator));
            log.info("Current argument: {}", DatatypeConverter.printHexBinary(bytes));

            instructions.add(new Instruction(opcode, bytes));
        }

        return instructions;
    }

    private void checkByteCodeLength(String bytecodeSource) {
        if (bytecodeSource.length() % 2 != 0) {
            throw new RuntimeException("This bytecodeSource has wrong length");
        }
    }

    private String prepareByteCode(String bytecodeSource) {
        if (bytecodeSource.startsWith("0x")) {
            return bytecodeSource.substring(2).toLowerCase();
        }
        return bytecodeSource;
    }

    private static String getParameter(int argumentsSize, HexByteIterator iterator) {
        StringBuilder stringBuilder = new StringBuilder();

        int argumentsCounter = argumentsSize;
        while (iterator.hasNext() && argumentsCounter > 0) {
            stringBuilder.append(iterator.next());
            argumentsCounter--;
        }

        int parameterRestCharsCount = 2 * argumentsCounter;
        String restChars = StringUtils.repeat("0", parameterRestCharsCount);
        stringBuilder.append(restChars);
        return stringBuilder.toString();
    }

}
