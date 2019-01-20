package com.smartcontract.solidity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static lombok.Lombok.checkNotNull;
import static org.web3j.crypto.Hash.sha3String;

@Service
@Slf4j
public class SolidityService {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final SolidityParser solidityParser = new SolidityParser();
    private final SolidityFileRepository solidityFileRepository;

    @Autowired
    public SolidityService(SolidityFileRepository solidityFileRepository) {
        checkNotNull(solidityFileRepository, "Expected not-null solidityFileRepository");
        this.solidityFileRepository = solidityFileRepository;
    }

    public Optional<String> findSourceCodeByHash(String _fileHash) {
        checkNotNull(_fileHash, "Expected not-null _fileHash");
        String fileHash;
        if (!_fileHash.startsWith("0x")) {
            fileHash = "0x" + _fileHash;
        } else {
            fileHash = _fileHash;
        }

        Optional<SolidityFile> solidityFile = solidityFileRepository.findBySourceCodeHash(fileHash);

        return solidityFile.map(SolidityFile::getSourceCode);
    }

    public List<SolidityFile> findAllFiles() {
        return solidityFileRepository.findAll();
    }

    public List<SolidityFile> findSolidityFilesBySelectorIn(List<String> functionSelector) {
        checkNotNull(functionSelector, "Expected not-null functionSelector");
        return solidityFileRepository.findSolidityFilesBySelectorContainsAll(functionSelector);
    }

    public long getSolidityFilesCount() {
        return solidityFileRepository.count();
    }

    public SolidityFile save(String sourceCode) throws IOException {
        checkNotNull(sourceCode, "Expected not-null sourceCode");
        return save(sourceCode.getBytes());
    }

    SolidityFile save(byte[] sourceCodeBytes) throws IOException {
        String sourceCode = new String(sourceCodeBytes, CHARSET);
        String sourceCodeHash = sha3String(sourceCode);

        Set<SolidityFunction> functionsFromFile = findSolidityFunctionsFromSourceFile(new ByteArrayInputStream(sourceCodeBytes));

        log.info("SourceCode hash: [{}]", sourceCodeHash);
        log.info("SourceCode functions count: {}", functionsFromFile.size());

        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }

    Set<SolidityFunction> findSolidityFunctionsFromSourceFile(InputStream inputStream) throws IOException {
        checkNotNull(inputStream, "Expected not-null inputStream");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        Set<SolidityFunction> solidityFunctions = new HashSet<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Optional<SolidityFunction> function = solidityParser.findFunctionInLine(line);

            function.ifPresent(solidityFunction -> {
                solidityFunctions.add(solidityFunction);
                log.info("SolidityFunction selector: {}", solidityFunction);
            });
        }
        return solidityFunctions;
    }
}
