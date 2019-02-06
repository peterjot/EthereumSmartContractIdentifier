package com.smartcontract.disassembler;

import java.util.Iterator;


class HexStringIterator implements Iterator<String> {

    private final String bytecode;
    private int position = 0;

    HexStringIterator(String bytecode) {
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
