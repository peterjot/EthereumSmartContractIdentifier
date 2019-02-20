package com.smartcontract.solidity;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
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
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final SolidityParser solidityParser;
    private final SolidityFileRepository solidityFileRepository;

    @Autowired
    public SolidityService(SolidityFileRepository solidityFileRepository, SolidityParser solidityParser) {
        requireNonNull(solidityFileRepository, "Expected not-null solidityFileRepository");
        requireNonNull(solidityParser, "Expected not-null solidityParser");
        this.solidityFileRepository = solidityFileRepository;
        this.solidityParser = solidityParser;
    }

    public Optional<String> findSourceCodeByHash(String _fileHash) {
        requireNonNull(_fileHash, "Expected not-null _fileHash");
        String fileHash = _fileHash.startsWith("0x") ? _fileHash : "0x" + _fileHash;

        return solidityFileRepository
                .findBySourceCodeHash(fileHash)
                .map(SolidityFile::getSourceCode);
    }

    public List<SolidityFile> findAllFiles() {
        return solidityFileRepository.findAll();
    }

    public List<SolidityFile> findSolidityFilesBySelectorIn(List<String> functionSelector) {
        requireNonNull(functionSelector, "Expected not-null functionSelector");
        return solidityFileRepository.findSolidityFilesBySelectorContainsAll(functionSelector);
    }

    public long getSolidityFilesCount() {
        return solidityFileRepository.count();
    }

    public SolidityFile save(String sourceCode) throws IOException {
        requireNonNull(sourceCode, "Expected not-null sourceCode");
        return save(sourceCode.getBytes());
    }

    SolidityFile save(byte[] sourceCodeBytes) throws IOException {
        String sourceCode = new String(sourceCodeBytes, CHARSET);
        String sourceCodeHash = sha3String(sourceCode);

        Set<SolidityFunction> functionsFromFile = findSolidityFunctionsFromSourceFile(new ByteArrayInputStream(sourceCodeBytes));

        LOGGER.info("SourceCode hash: [{}]", sourceCodeHash);
        LOGGER.info("SourceCode functions count: {}", functionsFromFile.size());

        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }

    Set<SolidityFunction> findSolidityFunctionsFromSourceFile(InputStream inputStream) throws IOException {
        requireNonNull(inputStream, "Expected not-null inputStream");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        Set<SolidityFunction> solidityFunctions = new HashSet<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Optional<SolidityFunction> function = solidityParser.findFunctionInLine(line);

            function.ifPresent(solidityFunction -> {
                solidityFunctions.add(solidityFunction);
                LOGGER.info("SolidityFunction selector: {}", solidityFunction);
            });
        }
        return solidityFunctions;
    }
}
