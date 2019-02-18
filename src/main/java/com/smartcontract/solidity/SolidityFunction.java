package com.smartcontract.solidity;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SolidityFunction {

    private final String selector;
    private final String signature;

    SolidityFunction(String selector, String signature) {
        requireNonNull(selector, "Expected not-null selector");
        requireNonNull(signature, "Expected not-null signature");
        this.selector = selector;
        this.signature = signature;
    }

    public String getSelector() {
        return selector;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return "SolidityFunction{" +
                "selector='" + selector + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolidityFunction)) return false;
        SolidityFunction that = (SolidityFunction) o;
        return Objects.equals(selector, that.selector) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selector, signature);
    }
}
