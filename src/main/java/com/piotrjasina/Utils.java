package com.piotrjasina;

import org.web3j.crypto.Hash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Utils {

    private static Charset charset = Charset.defaultCharset();


    private Utils() {
        throw new AssertionError();
    }

    public static String convertToString(InputStream inputStream) throws IOException {
        checkNotNull(inputStream, "Expected non-null inputStream");

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            return bufferedReader
                    .lines()
                    .collect(
                            joining(
                                    lineSeparator()));
        }
    }

    public static String stringHash(String sourceCode) {
        return Hash.sha3String(sourceCode);
    }
}
