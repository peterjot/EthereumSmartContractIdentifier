package com.smartcontract.bytecode;


import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableMap;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
enum Opcode {

    STOP(0x00, 0),// "Halts execution."
    ADD(0x01, 0),// "Addition operation."
    MUL(0x02, 0),// "Multiplication operation."
    SUB(0x03, 0),// "Subtraction operation."
    DIV(0x04, 0),// "Integer division operation."
    SDIV(0x05, 0),// "Signed integer division operation (truncated)."
    MOD(0x06, 0),// "Modulo remainder operation."
    SMOD(0x07, 0),// "Signed modulo remainder operation."
    ADDMOD(0x08, 0),// "Modulo addition operation."
    MULMOD(0x09, 0),// "Modulo multiplication operation."
    EXP(0x0A, 0),// "Exponential operation."
    SIGNEXTEND(0x0B, 0),// "Extend length of two's complement signed integer."
    LT(0x10, 0),// "Less-than comparision."
    GT(0x11, 0),// "Greater-than comparision."
    SLT(0x12, 0),// "Signed less-than comparision."
    SGT(0x13, 0),// "Signed greater-than comparision."
    EQ(0x14, 0),// "Equality comparision."
    ISZERO(0x15, 0),// "Simple not operator."
    AND(0x16, 0),// "Bitwise AND operation."
    OR(0x17, 0),// "Bitwise OR operation."
    XOR(0x18, 0),// "Bitwise XOR operation."
    NOT(0x19, 0),// "Bitwise NOT operation."
    BYTE(0x1A, 0),// "Retrieve single byte from word."
    SHA3(0x20, 0),// "Compute Keccak-256 selector."
    ADDRESS(0x30, 0),// "Get address of currently executing account     ."
    BALANCE(0x31, 0),// "Get balance of the given account."
    ORIGIN(0x32, 0),// "Get execution origination address."
    CALLER(0x33, 0),// "Get caller address."
    CALLVALUE(0x34, 0),// "Get deposited value by the instruction/transaction responsible for this execution."
    CALLDATALOAD(0x35, 0),// "Get input data of current environment."
    CALLDATASIZE(0x36, 0),// "Get size of input data in current environment."
    CALLDATACOPY(0x37, 0),// "Copy input data in current environment to memory."
    CODESIZE(0x38, 0),// "Get size of code running in current environment."
    CODECOPY(0x39, 0),// "Copy code running in current environment to memory."
    GASPRICE(0x3A, 0),// "Get price of gas in current environment."
    EXTCODESIZE(0x3B, 0),// "Get size of an account's code."
    EXTCODECOPY(0x3C, 0),// "Copy an account's code to memory."
    RETURNDATASIZE(0x3D, 0),// "Get size of output data from the previous call from the current environment"
    RETURNDATACOPY(0x3E, 0),// "Copy output data from the previous call to memory"
    BLOCKHASH(0x40, 0),// "Get the selector of one of the 256 most recent complete blocks."
    COINBASE(0x41, 0),// "Get the block's beneficiary address."
    TIMESTAMP(0x42, 0),// "Get the block's timestamp."
    NUMBER(0x43, 0),// "Get the block's number."
    DIFFICULTY(0x44, 0),// "Get the block's difficulty."
    GASLIMIT(0x45, 0),// "Get the block's gas limit."
    POP(0x50, 0),// "Remove item from stack."
    MLOAD(0x51, 0),// "Load word from memory."
    MSTORE(0x52, 0),// "Save word to memory."
    MSTORE8(0x53, 0),// "Save byte to memory."
    SLOAD(0x54, 0),// "Load word from storage."
    SSTORE(0x55, 0),// "Save word to storage."
    JUMP(0x56, 0),// "Alter the program counter."
    JUMPI(0x57, 0),// "Conditionally alter the program counter."
    GETPC(0x58, 0),// "Get the value of the program counter prior to the increment."
    MSIZE(0x59, 0),// "Get the size of active memory in bytes."
    GAS(0x5A, 0),// "Get the amount of available gas, including the corresponding reduction the amount of available gas."
    JUMPDEST(0x5B, 0),// "Mark a valid destination for jumps."

    PUSH1(0x60, 1),// "Place 1 byte item on stack."
    PUSH2(0x61, 2),// "Place 2-byte item on stack."
    PUSH3(0x62, 3),// "Place 3-byte item on stack."
    PUSH4(0x63, 4),// "Place 4-byte item on stack."
    PUSH5(0x64, 5),// "Place 5-byte item on stack."
    PUSH6(0x65, 6),// "Place 6-byte item on stack."
    PUSH7(0x66, 7),// "Place 7-byte item on stack."
    PUSH8(0x67, 8),// "Place 8-byte item on stack."
    PUSH9(0x68, 9),// "Place 9-byte item on stack."
    PUSH10(0x69, 10),// "Place 10-byte item on stack."
    PUSH11(0x6A, 11),// "Place 11-byte item on stack."
    PUSH12(0x6B, 12),// "Place 12-byte item on stack."
    PUSH13(0x6C, 13),// "Place 13-byte item on stack."
    PUSH14(0x6D, 14),// "Place 14-byte item on stack."
    PUSH15(0x6E, 15),// "Place 15-byte item on stack."
    PUSH16(0x6F, 16),// "Place 16-byte item on stack."
    PUSH17(0x70, 17),// "Place 17-byte item on stack."
    PUSH18(0x71, 18),// "Place 18-byte item on stack."
    PUSH19(0x72, 19),// "Place 19-byte item on stack."
    PUSH20(0x73, 20),// "Place 20-byte item on stack."
    PUSH21(0x74, 21),// "Place 21-byte item on stack."
    PUSH22(0x75, 22),// "Place 22-byte item on stack."
    PUSH23(0x76, 23),// "Place 23-byte item on stack."
    PUSH24(0x77, 24),// "Place 24-byte item on stack."
    PUSH25(0x78, 25),// "Place 25-byte item on stack."
    PUSH26(0x79, 26),// "Place 26-byte item on stack."
    PUSH27(0x7A, 27),// "Place 27-byte item on stack."
    PUSH28(0x7B, 28),// "Place 28-byte item on stack."
    PUSH29(0x7C, 29),// "Place 29-byte item on stack."
    PUSH30(0x7D, 30),// "Place 30-byte item on stack."
    PUSH31(0x7E, 31),// "Place 31-byte item on stack."
    PUSH32(0x7F, 32),// "Place 32-byte (full word) item on stack."

    DUP1(0x80, 0),// "Duplicate 1st stack item."
    DUP2(0x81, 0),// "Duplicate 2nd stack item."
    DUP3(0x82, 0),// "Duplicate 3rd stack item."
    DUP4(0x83, 0),// "Duplicate 4th stack item."
    DUP5(0x84, 0),// "Duplicate 5th stack item."
    DUP6(0x85, 0),// "Duplicate 6th stack item."
    DUP7(0x86, 0),// "Duplicate 7th stack item."
    DUP8(0x87, 0),// "Duplicate 8th stack item."
    DUP9(0x88, 0),// "Duplicate 9th stack item."
    DUP10(0x89, 0),// "Duplicate 10th stack item."
    DUP11(0x8A, 0),// "Duplicate 11th stack item."
    DUP12(0x8B, 0),// "Duplicate 12th stack item."
    DUP13(0x8C, 0),// "Duplicate 13th stack item."
    DUP14(0x8D, 0),// "Duplicate 14th stack item."
    DUP15(0x8E, 0),// "Duplicate 15th stack item."
    DUP16(0x8F, 0),// "Duplicate 16th stack item."

    SWAP1(0x90, 0),// "Exchange 1st and 2nd stack items."
    SWAP2(0x91, 0),// "Exchange 1st and 3rd stack items."
    SWAP3(0x92, 0),// "Exchange 1st and 4th stack items."
    SWAP4(0x93, 0),// "Exchange 1st and 5th stack items."
    SWAP5(0x94, 0),// "Exchange 1st and 6th stack items."
    SWAP6(0x95, 0),// "Exchange 1st and 7th stack items."
    SWAP7(0x96, 0),// "Exchange 1st and 8th stack items."
    SWAP8(0x97, 0),// "Exchange 1st and 9th stack items."
    SWAP9(0x98, 0),// "Exchange 1st and 10th stack items."
    SWAP10(0x99, 0),// "Exchange 1st and 11th stack items."
    SWAP11(0x9A, 0),// "Exchange 1st and 12th stack items."
    SWAP12(0x9B, 0),// "Exchange 1st and 13th stack items."
    SWAP13(0x9C, 0),// "Exchange 1st and 14th stack items."
    SWAP14(0x9D, 0),// "Exchange 1st and 15th stack items."
    SWAP15(0x9E, 0),// "Exchange 1st and 16th stack items."
    SWAP16(0x9F, 0),// "Exchange 1st and 17th stack items."

    LOG0(0xA0, 0),// "Append log record with no topics."
    LOG1(0xA1, 0),// "Append log record with one topic."
    LOG2(0xA2, 0),// "Append log record with two topics."
    LOG3(0xA3, 0),// "Append log record with three topics."
    LOG4(0xA4, 0),// "Append log record with four topics."

    CREATE(0xF0, 0),// "Create a new account with associated code."
    CALL(0xF1, 0),// "Message-call into an account."
    CALLCODE(0xF2, 0),// "Message-call into this account with alternative account's code."
    RETURN(0xF3, 0),// "Halt execution returning output data."
    DELEGATECALL(0xF4, 0),// "Message-call into this account with an alternative account's code, but persisting into this account with an alternative account's code."
    STATICCALL(0xFA, 0),// "Static message-call into an account."
    REVERT(0xFD, 0),// "Stop execution and revert state changes, without consuming all provided gas and providing a reason."
    INVALID(0xFE, 0),// "Designated invalid instruction"
    SELFDESTRUCT(0xFF, 0),// "Halt execution and register account for later deletion."

    UNKNOWNCODE(0xFFFFF, 0);// "Unknown code"

    private static final Map<Integer, Opcode> opcodes;

    static {
        opcodes = unmodifiableMap(new HashMap<Integer, Opcode>() {{
            for (Opcode opcode : Opcode.values()) {
                put(opcode.getHexValue(), opcode);
            }
        }});
    }

    private final int hexValue;
    private final int operandSize;


    static Opcode getOpcodeByHex(@NonNull String stringHex) {
        if (stringHex.length() != 2) {
            throw new IllegalArgumentException("Expected length=2 stringHex");
        }
        return getOpcodeByHex(Integer.parseInt(stringHex, 16));
    }

    static Opcode getOpcodeByHex(int hex) {
        return Optional
                .ofNullable(opcodes.get(hex))
                .orElse(Opcode.UNKNOWNCODE);
    }
}
