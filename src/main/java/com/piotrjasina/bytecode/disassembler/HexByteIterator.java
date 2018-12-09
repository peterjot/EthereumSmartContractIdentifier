package com.piotrjasina.bytecode.disassembler;

import java.util.Iterator;

class HexByteIterator implements Iterator<String> {

    private String byteCodeStr;
    private int position;

    HexByteIterator(String byteCodeStr) {
        this.byteCodeStr = byteCodeStr;
    }

    @Override
    public boolean hasNext() {
        return position + 2 <= byteCodeStr.length();
    }

    @Override
    public String next() {
        String hexByte = byteCodeStr.substring(position, position + 2);
        position += 2;
        return hexByte;
    }
}
