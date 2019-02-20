package com.smartcontract.disassembler;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.isNull;

class OpcodeTable {

    private OpcodeTable() {
        throw new UnsupportedOperationException();
    }

    private static final Map<Integer, Opcode> opcodes =
            unmodifiableMap(new HashMap<Integer, Opcode>() {{
                for (Opcode opcode : Opcode.values()) {
                    if (!opcode.equals(Opcode.UNKNOWNCODE))
                        put(opcode.getHexValue(), opcode);
                }
            }});

    static Opcode getOpcodeByHex(String stringHex) {
        if (stringHex.length() != 2) {
            throw new IllegalArgumentException("Expected length=2 stringHex");
        }
        return getOpcodeByHex(Integer.parseInt(stringHex, 16));
    }

    static Opcode getOpcodeByHex(int hex) {
        Opcode opcode = opcodes.get(hex);
        if (isNull(opcode)) {
            return Opcode.UNKNOWNCODE;
        }
        return opcode;
    }
}
