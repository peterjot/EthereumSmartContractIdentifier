package com.piotrjasina.bytecode.disassembler;

import java.util.Iterator;

class HexByteIterator implements Iterator<String> {

    private String bytecode;
    private int position;

    HexByteIterator(String bytecode) {
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
