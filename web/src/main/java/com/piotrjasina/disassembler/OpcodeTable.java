package com.piotrjasina.disassembler;

import java.util.HashMap;
import java.util.Map;

class OpcodeTable {

    private static Map<String, Opcode> opcodes = new HashMap<String, Opcode>() {{
        put("00", com.piotrjasina.disassembler.Opcode.STOP);
        put("01", com.piotrjasina.disassembler.Opcode.ADD);
        put("02", com.piotrjasina.disassembler.Opcode.MUL);
        put("03", com.piotrjasina.disassembler.Opcode.SUB);
        put("04", com.piotrjasina.disassembler.Opcode.DIV);
        put("05", com.piotrjasina.disassembler.Opcode.SDIV);
        put("06", com.piotrjasina.disassembler.Opcode.MOD);
        put("07", com.piotrjasina.disassembler.Opcode.SMOD);
        put("08", com.piotrjasina.disassembler.Opcode.ADDMOD);
        put("09", com.piotrjasina.disassembler.Opcode.MULMOD);
        put("0a", com.piotrjasina.disassembler.Opcode.EXP);
        put("0b", com.piotrjasina.disassembler.Opcode.SIGNEXTEND);
        put("10", com.piotrjasina.disassembler.Opcode.LT);
        put("11", com.piotrjasina.disassembler.Opcode.GT);
        put("12", com.piotrjasina.disassembler.Opcode.SLT);
        put("13", com.piotrjasina.disassembler.Opcode.SGT);
        put("14", com.piotrjasina.disassembler.Opcode.EQ);
        put("15", com.piotrjasina.disassembler.Opcode.ISZERO);
        put("16", com.piotrjasina.disassembler.Opcode.AND);
        put("17", com.piotrjasina.disassembler.Opcode.OR);
        put("18", com.piotrjasina.disassembler.Opcode.XOR);
        put("19", com.piotrjasina.disassembler.Opcode.NOT);
        put("1a", com.piotrjasina.disassembler.Opcode.BYTE);
        put("20", com.piotrjasina.disassembler.Opcode.SHA3);
        put("30", com.piotrjasina.disassembler.Opcode.ADDRESS);
        put("31", com.piotrjasina.disassembler.Opcode.BALANCE);
        put("32", com.piotrjasina.disassembler.Opcode.ORIGIN);
        put("33", com.piotrjasina.disassembler.Opcode.CALLER);
        put("34", com.piotrjasina.disassembler.Opcode.CALLVALUE);
        put("35", com.piotrjasina.disassembler.Opcode.CALLDATALOAD);
        put("36", com.piotrjasina.disassembler.Opcode.CALLDATASIZE);
        put("37", com.piotrjasina.disassembler.Opcode.CALLDATACOPY);
        put("38", com.piotrjasina.disassembler.Opcode.CODESIZE);
        put("39", com.piotrjasina.disassembler.Opcode.CODECOPY);
        put("3a", com.piotrjasina.disassembler.Opcode.GASPRICE);
        put("3b", com.piotrjasina.disassembler.Opcode.EXTCODESIZE);
        put("3c", com.piotrjasina.disassembler.Opcode.EXTCODECOPY);
        put("3d", com.piotrjasina.disassembler.Opcode.RETURNDATASIZE);
        put("3e", com.piotrjasina.disassembler.Opcode.RETURNDATACOPY);
        put("40", com.piotrjasina.disassembler.Opcode.BLOCKHASH);
        put("41", com.piotrjasina.disassembler.Opcode.COINBASE);
        put("42", com.piotrjasina.disassembler.Opcode.TIMESTAMP);
        put("43", com.piotrjasina.disassembler.Opcode.NUMBER);
        put("44", com.piotrjasina.disassembler.Opcode.DIFFICULTY);
        put("45", com.piotrjasina.disassembler.Opcode.GASLIMIT);
        put("50", com.piotrjasina.disassembler.Opcode.POP);
        put("51", com.piotrjasina.disassembler.Opcode.MLOAD);
        put("52", com.piotrjasina.disassembler.Opcode.MSTORE);
        put("53", com.piotrjasina.disassembler.Opcode.MSTORE8);
        put("54", com.piotrjasina.disassembler.Opcode.SLOAD);
        put("55", com.piotrjasina.disassembler.Opcode.SSTORE);
        put("56", com.piotrjasina.disassembler.Opcode.JUMP);
        put("57", com.piotrjasina.disassembler.Opcode.JUMPI);
        put("58", com.piotrjasina.disassembler.Opcode.GETPC);
        put("59", com.piotrjasina.disassembler.Opcode.MSIZE);
        put("5a", com.piotrjasina.disassembler.Opcode.GAS);
        put("5b", com.piotrjasina.disassembler.Opcode.JUMPDEST);
        put("60", com.piotrjasina.disassembler.Opcode.PUSH1);
        put("61", com.piotrjasina.disassembler.Opcode.PUSH2);
        put("62", com.piotrjasina.disassembler.Opcode.PUSH3);
        put("63", com.piotrjasina.disassembler.Opcode.PUSH4);
        put("64", com.piotrjasina.disassembler.Opcode.PUSH5);
        put("65", com.piotrjasina.disassembler.Opcode.PUSH6);
        put("66", com.piotrjasina.disassembler.Opcode.PUSH7);
        put("67", com.piotrjasina.disassembler.Opcode.PUSH8);
        put("68", com.piotrjasina.disassembler.Opcode.PUSH9);
        put("69", com.piotrjasina.disassembler.Opcode.PUSH10);
        put("6a", com.piotrjasina.disassembler.Opcode.PUSH11);
        put("6b", com.piotrjasina.disassembler.Opcode.PUSH12);
        put("6c", com.piotrjasina.disassembler.Opcode.PUSH13);
        put("6d", com.piotrjasina.disassembler.Opcode.PUSH14);
        put("6e", com.piotrjasina.disassembler.Opcode.PUSH15);
        put("6f", com.piotrjasina.disassembler.Opcode.PUSH16);
        put("70", com.piotrjasina.disassembler.Opcode.PUSH17);
        put("71", com.piotrjasina.disassembler.Opcode.PUSH18);
        put("72", com.piotrjasina.disassembler.Opcode.PUSH19);
        put("73", com.piotrjasina.disassembler.Opcode.PUSH20);
        put("74", com.piotrjasina.disassembler.Opcode.PUSH21);
        put("75", com.piotrjasina.disassembler.Opcode.PUSH22);
        put("76", com.piotrjasina.disassembler.Opcode.PUSH23);
        put("77", com.piotrjasina.disassembler.Opcode.PUSH24);
        put("78", com.piotrjasina.disassembler.Opcode.PUSH25);
        put("79", com.piotrjasina.disassembler.Opcode.PUSH26);
        put("7a", com.piotrjasina.disassembler.Opcode.PUSH27);
        put("7b", com.piotrjasina.disassembler.Opcode.PUSH28);
        put("7c", com.piotrjasina.disassembler.Opcode.PUSH29);
        put("7d", com.piotrjasina.disassembler.Opcode.PUSH30);
        put("7e", com.piotrjasina.disassembler.Opcode.PUSH31);
        put("7f", com.piotrjasina.disassembler.Opcode.PUSH32);
        put("80", com.piotrjasina.disassembler.Opcode.DUP1);
        put("81", com.piotrjasina.disassembler.Opcode.DUP2);
        put("82", com.piotrjasina.disassembler.Opcode.DUP3);
        put("83", com.piotrjasina.disassembler.Opcode.DUP4);
        put("84", com.piotrjasina.disassembler.Opcode.DUP5);
        put("85", com.piotrjasina.disassembler.Opcode.DUP6);
        put("86", com.piotrjasina.disassembler.Opcode.DUP7);
        put("87", com.piotrjasina.disassembler.Opcode.DUP8);
        put("88", com.piotrjasina.disassembler.Opcode.DUP9);
        put("89", com.piotrjasina.disassembler.Opcode.DUP10);
        put("8a", com.piotrjasina.disassembler.Opcode.DUP11);
        put("8b", com.piotrjasina.disassembler.Opcode.DUP12);
        put("8c", com.piotrjasina.disassembler.Opcode.DUP13);
        put("8d", com.piotrjasina.disassembler.Opcode.DUP14);
        put("8e", com.piotrjasina.disassembler.Opcode.DUP15);
        put("8f", com.piotrjasina.disassembler.Opcode.DUP16);
        put("90", com.piotrjasina.disassembler.Opcode.SWAP1);
        put("91", com.piotrjasina.disassembler.Opcode.SWAP2);
        put("92", com.piotrjasina.disassembler.Opcode.SWAP3);
        put("93", com.piotrjasina.disassembler.Opcode.SWAP4);
        put("94", com.piotrjasina.disassembler.Opcode.SWAP5);
        put("95", com.piotrjasina.disassembler.Opcode.SWAP6);
        put("96", com.piotrjasina.disassembler.Opcode.SWAP7);
        put("97", com.piotrjasina.disassembler.Opcode.SWAP8);
        put("98", com.piotrjasina.disassembler.Opcode.SWAP9);
        put("99", com.piotrjasina.disassembler.Opcode.SWAP10);
        put("9a", com.piotrjasina.disassembler.Opcode.SWAP11);
        put("9b", com.piotrjasina.disassembler.Opcode.SWAP12);
        put("9c", com.piotrjasina.disassembler.Opcode.SWAP13);
        put("9d", com.piotrjasina.disassembler.Opcode.SWAP14);
        put("9e", com.piotrjasina.disassembler.Opcode.SWAP15);
        put("9f", com.piotrjasina.disassembler.Opcode.SWAP16);
        put("a0", com.piotrjasina.disassembler.Opcode.LOG0);
        put("a1", com.piotrjasina.disassembler.Opcode.LOG1);
        put("a2", com.piotrjasina.disassembler.Opcode.LOG2);
        put("a3", com.piotrjasina.disassembler.Opcode.LOG3);
        put("a4", com.piotrjasina.disassembler.Opcode.LOG4);
        put("f0", com.piotrjasina.disassembler.Opcode.CREATE);
        put("f1", com.piotrjasina.disassembler.Opcode.CALL);
        put("f2", com.piotrjasina.disassembler.Opcode.CALLCODE);
        put("f3", com.piotrjasina.disassembler.Opcode.RETURN);
        put("f4", com.piotrjasina.disassembler.Opcode.DELEGATECALL);
        put("fa", com.piotrjasina.disassembler.Opcode.STATICCALL);
        put("fd", com.piotrjasina.disassembler.Opcode.REVERT);
        put("fe", com.piotrjasina.disassembler.Opcode.INVALID);
        put("ff", com.piotrjasina.disassembler.Opcode.SELFDESTRUCT);
    }};

    static com.piotrjasina.disassembler.Opcode getOpcodeByByte(String hex) {
        Opcode opcode = opcodes.get(hex);
        if (opcode == null) {
            return Opcode.UNKNOWNCODE;
        }
        return opcode;
    }

}
