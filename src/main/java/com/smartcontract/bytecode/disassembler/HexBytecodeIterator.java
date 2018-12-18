package com.smartcontract.bytecode.disassembler;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

class HexBytecodeIterator implements Iterator<String> {

    private String bytecode;
    private int position;

    HexBytecodeIterator(String bytecode) {
        checkNotNull(bytecode, "Expected not-null bytecode");
        this.bytecode = bytecode;
    }

    @Override
    public boolean hasNext() {
        return position + 2 <= bytecode.length();
    }

    @Override
    public String next() {
        String hexByte = bytecode.substring(position, position + 2);
        position += 2;
        return hexByte;
    }
}
