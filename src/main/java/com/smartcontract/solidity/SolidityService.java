package com.smartcontract.solidity;

import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.web3j.crypto.Hash.sha3String;

@Service
public class SolidityService {

    private static final Logger LOGGER = getLogger(SolidityService.class);

    private final SourceCodeParser sourceCodeParser;
    private final SolidityFileRepository solidityFileRepository;


    public SolidityService(@NonNull SolidityFileRepository solidityFileRepository,
                           @NonNull SourceCodeParser sourceCodeParser) {
        this.solidityFileRepository = solidityFileRepository;
        this.sourceCodeParser = sourceCodeParser;
    }

    public Optional<String> findSourceCodeByHash(@NonNull String _fileHash) {
        String fileHash = _fileHash.startsWith("0x") ? _fileHash : "0x" + _fileHash;
        return solidityFileRepository
                .findBySourceCodeHash(fileHash)
                .map(SolidityFile::getSourceCode);
    }

    public List<SolidityFile> findSolidityFilesBySelectors(@NonNull List<String> functionSelectors) {
        return solidityFileRepository.findSolidityFilesBySelectorContains(functionSelectors);
    }

    public SolidityFile save(@NonNull String sourceCode) throws IOException {
        return save(sourceCode.getBytes());
    }

    long getSolidityFilesCount() {
        return solidityFileRepository.count();
    }

    SolidityFile save(byte[] sourceCodeBytes) throws IOException {
        String sourceCode = new String(sourceCodeBytes, StandardCharsets.UTF_8);

        final String sourceCodeWithoutEmptyLinesAndUselessSpaces = sourceCode
                .replaceAll("(?m)\\s+$", "")
                .replaceAll("(?m) +", " ")
                .replaceAll("(?m)^\\s+", "");
        // (?m) - tells Java to accept the anchors ^ and $ to match at the start and end of each line
        // (otherwise they only match at the start/end of the entire string).
        String sourceCodeHash = sha3String(sourceCodeWithoutEmptyLinesAndUselessSpaces);

        Set<SolidityFunction> functionsFromFile = findSolidityFunctionsFromSourceFile(new ByteArrayInputStream(sourceCodeBytes));
        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }

    Set<SolidityFunction> findSolidityFunctionsFromSourceFile(InputStream inputStream) throws IOException {
        requireNonNull(inputStream, "Expected not-null inputStream");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Set<SolidityFunction> solidityFunctions = new HashSet<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Optional<SolidityFunction> function = sourceCodeParser.findFunctionInLine(line);

            function.ifPresent(solidityFunction -> {
                solidityFunctions.add(solidityFunction);
                LOGGER.info("SolidityFunction selector: {}", solidityFunction);
            });
        }
        return solidityFunctions;
    }
}
