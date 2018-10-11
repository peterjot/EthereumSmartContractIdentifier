package com.piotrjasina.contractidentifier;

import lombok.ToString;

@ToString
public enum Instruction {

    STOP(0x00, 0, 0, 0, 0, "Halts execution."),
    ADD(0x01, 0, 2, 1, 3, "Addition operation."),
    MUL(0x02, 0, 2, 1, 5, "Multiplication operation."),
    SUB(0x03, 0, 2, 1, 3, "Subtraction operation."),
    DIV(0x04, 0, 2, 1, 5, "Integer division operation."),
    SDIV(0x05, 0, 2, 1, 5, "Signed integer division operation (truncated)."),
    MOD(0x06, 0, 2, 1, 5, "Modulo remainder operation."),
    SMOD(0x07, 0, 2, 1, 5, "Signed modulo remainder operation."),
    ADDMOD(0x08, 0, 3, 1, 8, "Modulo addition operation."),
    MULMOD(0x09, 0, 3, 1, 8, "Modulo multiplication operation."),
    EXP(0x0a, 0, 2, 1, 10, "Exponential operation."),
    SIGNEXTEND(0x0b, 0, 2, 1, 5, "Extend length of two's complement signed integer."),
    LT(0x10, 0, 2, 1, 3, "Less-than comparision."),
    GT(0x11, 0, 2, 1, 3, "Greater-than comparision."),
    SLT(0x12, 0, 2, 1, 3, "Signed less-than comparision."),
    SGT(0x13, 0, 2, 1, 3, "Signed greater-than comparision."),
    EQ(0x14, 0, 2, 1, 3, "Equality comparision."),
    ISZERO(0x15, 0, 1, 1, 3, "Simple not operator."),
    AND(0x16, 0, 2, 1, 3, "Bitwise AND operation."),
    OR(0x17, 0, 2, 1, 3, "Bitwise OR operation."),
    XOR(0x18, 0, 2, 1, 3, "Bitwise XOR operation."),
    NOT(0x19, 0, 1, 1, 3, "Bitwise NOT operation."),
    BYTE(0x1a, 0, 2, 1, 3, "Retrieve single byte from word."),
    SHA3(0x20, 0, 2, 1, 30, "Compute Keccak-256 hash."),
    ADDRESS(0x30, 0, 0, 1, 2, "Get address of currently executing account     ."),
    BALANCE(0x31, 0, 1, 1, 20, "Get balance of the given account."),
    ORIGIN(0x32, 0, 0, 1, 2, "Get execution origination address."),
    CALLER(0x33, 0, 0, 1, 2, "Get caller address."),
    CALLVALUE(0x34, 0, 0, 1, 2, "Get deposited value by the instruction/transaction responsible for this execution."),
    CALLDATALOAD(0x35, 0, 1, 1, 3, "Get input data of current environment."),
    CALLDATASIZE(0x36, 0, 0, 1, 2, "Get size of input data in current environment."),
    CALLDATACOPY(0x37, 0, 3, 0, 3, "Copy input data in current environment to memory."),
    CODESIZE(0x38, 0, 0, 1, 2, "Get size of code running in current environment."),
    CODECOPY(0x39, 0, 3, 0, 3, "Copy code running in current environment to memory."),
    GASPRICE(0x3a, 0, 0, 1, 2, "Get price of gas in current environment."),
    EXTCODESIZE(0x3b, 0, 1, 1, 20, "Get size of an account's code."),
    EXTCODECOPY(0x3c, 0, 4, 0, 20, "Copy an account's code to memory."),
    RETURNDATASIZE(0x3d, 0, 0, 1, 2, "Get size of output data from the previous call from the current environment"),
    RETURNDATACOPY(0x3e, 0, 3, 0, 3, "Copy output data from the previous call to memory"),
    BLOCKHASH(0x40, 0, 1, 1, 20, "Get the hash of one of the 256 most recent complete blocks."),
    COINBASE(0x41, 0, 0, 1, 2, "Get the block's beneficiary address."),
    TIMESTAMP(0x42, 0, 0, 1, 2, "Get the block's timestamp."),
    NUMBER(0x43, 0, 0, 1, 2, "Get the block's number."),
    DIFFICULTY(0x44, 0, 0, 1, 2, "Get the block's difficulty."),
    GASLIMIT(0x45, 0, 0, 1, 2, "Get the block's gas limit."),
    POP(0x50, 0, 1, 0, 2, "Remove item from stack."),
    MLOAD(0x51, 0, 1, 1, 3, "Load word from memory."),
    MSTORE(0x52, 0, 2, 0, 3, "Save word to memory."),
    MSTORE8(0x53, 0, 2, 0, 3, "Save byte to memory."),
    SLOAD(0x54, 0, 1, 1, 50, "Load word from storage."),
    SSTORE(0x55, 0, 2, 0, 0, "Save word to storage."),
    JUMP(0x56, 0, 1, 0, 8, "Alter the program counter."),
    JUMPI(0x57, 0, 2, 0, 10, "Conditionally alter the program counter."),
    GETPC(0x58, 0, 0, 1, 2, "Get the value of the program counter prior to the increment."),
    MSIZE(0x59, 0, 0, 1, 2, "Get the size of active memory in bytes."),
    GAS(0x5a, 0, 0, 1, 2, "Get the amount of available gas, including the corresponding reduction the amount of available gas."),
    JUMPDEST(0x5b, 0, 0, 0, 1, "Mark a valid destination for jumps."),
    PUSH1(0x60, 1, 0, 1, 3, "Place 1 byte item on stack."),
    PUSH2(0x61, 2, 0, 1, 3, "Place 2-byte item on stack."),
    PUSH3(0x62, 3, 0, 1, 3, "Place 3-byte item on stack."),
    PUSH4(0x63, 4, 0, 1, 3, "Place 4-byte item on stack."),
    PUSH5(0x64, 5, 0, 1, 3, "Place 5-byte item on stack."),
    PUSH6(0x65, 6, 0, 1, 3, "Place 6-byte item on stack."),
    PUSH7(0x66, 7, 0, 1, 3, "Place 7-byte item on stack."),
    PUSH8(0x67, 8, 0, 1, 3, "Place 8-byte item on stack."),
    PUSH9(0x68, 9, 0, 1, 3, "Place 9-byte item on stack."),
    PUSH10(0x69, 10, 0, 1, 3, "Place 10-byte item on stack."),
    PUSH11(0x6a, 11, 0, 1, 3, "Place 11-byte item on stack."),
    PUSH12(0x6b, 12, 0, 1, 3, "Place 12-byte item on stack."),
    PUSH13(0x6c, 13, 0, 1, 3, "Place 13-byte item on stack."),
    PUSH14(0x6d, 14, 0, 1, 3, "Place 14-byte item on stack."),
    PUSH15(0x6e, 15, 0, 1, 3, "Place 15-byte item on stack."),
    PUSH16(0x6f, 16, 0, 1, 3, "Place 16-byte item on stack."),
    PUSH17(0x70, 17, 0, 1, 3, "Place 17-byte item on stack."),
    PUSH18(0x71, 18, 0, 1, 3, "Place 18-byte item on stack."),
    PUSH19(0x72, 19, 0, 1, 3, "Place 19-byte item on stack."),
    PUSH20(0x73, 20, 0, 1, 3, "Place 20-byte item on stack."),
    PUSH21(0x74, 21, 0, 1, 3, "Place 21-byte item on stack."),
    PUSH22(0x75, 22, 0, 1, 3, "Place 22-byte item on stack."),
    PUSH23(0x76, 23, 0, 1, 3, "Place 23-byte item on stack."),
    PUSH24(0x77, 24, 0, 1, 3, "Place 24-byte item on stack."),
    PUSH25(0x78, 25, 0, 1, 3, "Place 25-byte item on stack."),
    PUSH26(0x79, 26, 0, 1, 3, "Place 26-byte item on stack."),
    PUSH27(0x7a, 27, 0, 1, 3, "Place 27-byte item on stack."),
    PUSH28(0x7b, 28, 0, 1, 3, "Place 28-byte item on stack."),
    PUSH29(0x7c, 29, 0, 1, 3, "Place 29-byte item on stack."),
    PUSH30(0x7d, 30, 0, 1, 3, "Place 30-byte item on stack."),
    PUSH31(0x7e, 31, 0, 1, 3, "Place 31-byte item on stack."),
    PUSH32(0x7f, 32, 0, 1, 3, "Place 32-byte (full word) item on stack."),
    DUP1(0x80, 0, 1, 2, 3, "Duplicate 1st stack item."),
    DUP2(0x81, 0, 2, 3, 3, "Duplicate 2nd stack item."),
    DUP3(0x82, 0, 3, 4, 3, "Duplicate 3rd stack item."),
    DUP4(0x83, 0, 4, 5, 3, "Duplicate 4th stack item."),
    DUP5(0x84, 0, 5, 6, 3, "Duplicate 5th stack item."),
    DUP6(0x85, 0, 6, 7, 3, "Duplicate 6th stack item."),
    DUP7(0x86, 0, 7, 8, 3, "Duplicate 7th stack item."),
    DUP8(0x87, 0, 8, 9, 3, "Duplicate 8th stack item."),
    DUP9(0x88, 0, 9, 10, 3, "Duplicate 9th stack item."),
    DUP10(0x89, 0, 10, 11, 3, "Duplicate 10th stack item."),
    DUP11(0x8a, 0, 11, 12, 3, "Duplicate 11th stack item."),
    DUP12(0x8b, 0, 12, 13, 3, "Duplicate 12th stack item."),
    DUP13(0x8c, 0, 13, 14, 3, "Duplicate 13th stack item."),
    DUP14(0x8d, 0, 14, 15, 3, "Duplicate 14th stack item."),
    DUP15(0x8e, 0, 15, 16, 3, "Duplicate 15th stack item."),
    DUP16(0x8f, 0, 16, 17, 3, "Duplicate 16th stack item."),
    SWAP1(0x90, 0, 2, 2, 3, "Exchange 1st and 2nd stack items."),
    SWAP2(0x91, 0, 3, 3, 3, "Exchange 1st and 3rd stack items."),
    SWAP3(0x92, 0, 4, 4, 3, "Exchange 1st and 4th stack items."),
    SWAP4(0x93, 0, 5, 5, 3, "Exchange 1st and 5th stack items."),
    SWAP5(0x94, 0, 6, 6, 3, "Exchange 1st and 6th stack items."),
    SWAP6(0x95, 0, 7, 7, 3, "Exchange 1st and 7th stack items."),
    SWAP7(0x96, 0, 8, 8, 3, "Exchange 1st and 8th stack items."),
    SWAP8(0x97, 0, 9, 9, 3, "Exchange 1st and 9th stack items."),
    SWAP9(0x98, 0, 10, 10, 3, "Exchange 1st and 10th stack items."),
    SWAP10(0x99, 0, 11, 11, 3, "Exchange 1st and 11th stack items."),
    SWAP11(0x9a, 0, 12, 12, 3, "Exchange 1st and 12th stack items."),
    SWAP12(0x9b, 0, 13, 13, 3, "Exchange 1st and 13th stack items."),
    SWAP13(0x9c, 0, 14, 14, 3, "Exchange 1st and 14th stack items."),
    SWAP14(0x9d, 0, 15, 15, 3, "Exchange 1st and 15th stack items."),
    SWAP15(0x9e, 0, 16, 16, 3, "Exchange 1st and 16th stack items."),
    SWAP16(0x9f, 0, 17, 17, 3, "Exchange 1st and 17th stack items."),
    LOG0(0xa0, 0, 2, 0, 375, "Append log record with no topics."),
    LOG1(0xa1, 0, 3, 0, 750, "Append log record with one topic."),
    LOG2(0xa2, 0, 4, 0, 1125, "Append log record with two topics."),
    LOG3(0xa3, 0, 5, 0, 1500, "Append log record with three topics."),
    LOG4(0xa4, 0, 6, 0, 1875, "Append log record with four topics."),
    CREATE(0xf0, 0, 3, 1, 32000, "Create a new account with associated code."),
    CALL(0xf1, 0, 7, 1, 40, "Message-call into an account."),
    CALLCODE(0xf2, 0, 7, 1, 40, "Message-call into this account with alternative account's code."),
    RETURN(0xf3, 0, 2, 0, 0, "Halt execution returning output data."),
    DELEGATECALL(0xf4, 0, 6, 1, 40, "Message-call into this account with an alternative account's code, but persisting into this account with an alternative account's code."),
    STATICCALL(0xfa, 0, 6, 1, 40, "Static message-call into an account."),
    REVERT(0xfd, 0, 2, 0, 0, "Stop execution and revert state changes, without consuming all provided gas and providing a reason."),
    INVALID(0xfe, 0, 0, 0, 0, "Designated invalid instruction"),
    SELFDESTRUCT(0xff, 0, 1, 0, 5000, "Halt execution and register account for later deletion.");


    private byte opcode;
    private int operandSize;
    private int pops;
    private int pushes;
    private int gas;
    private String description;


    Instruction(long opcodeInt, int operandSize, int pops, int pushes, int gas, String description) {
        if (opcodeInt > 255 || opcodeInt < 0) {
            throw new IllegalArgumentException("Expected hexByte in range 0-255, hexByte:" + opcodeInt);
        }
        opcode = (byte) (opcodeInt & 0xff);
        this.operandSize = operandSize;
        this.pops = pops;
        this.pushes = pushes;
        this.gas = gas;
        this.description = description;
    }

    public String toHexString() {
        return Integer.toHexString(opcode & 0xFF);
    }

    public int getMaskedOpcode() {
        return opcode & 0xFF;
    }

    public int getOperandSize() {
        return operandSize;
    }

    public int getInstructionSize(){
        return getOperandSize() + 1;
    }
}
