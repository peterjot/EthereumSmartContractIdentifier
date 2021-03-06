package io.github.peterjot.solidity;

import io.github.peterjot.solidity.dto.SolidityFileDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.web3j.crypto.Hash.sha3String;

@RequiredArgsConstructor
public class SolidityService {

    private static final Logger LOGGER = getLogger(SolidityService.class);

    @NonNull
    private final SourceCodeParser sourceCodeParser;

    @NonNull
    private final SolidityFileRepository solidityFileRepository;


    public Optional<String> findSourceCodeByHash(@NonNull String _fileHash) {
        String fileHash = _fileHash.startsWith("0x") ? _fileHash : "0x" + _fileHash;
        return solidityFileRepository
                .findBySourceCodeHash(fileHash)
                .map(SolidityFile::getSourceCode);
    }

    public Set<SolidityFileDto> findSolidityFilesBySelectors(@NonNull List<String> functionSelectors) {
        final List<SolidityFile> solidityFiles = solidityFileRepository.findSolidityFilesBySelectorContains(functionSelectors);
        return SolidityConverter.fromEntity(solidityFiles);
    }

    public SolidityFileDto save(@NonNull String sourceCode) throws IOException {
        return save(sourceCode.getBytes());
    }

    public SolidityFileDto save(byte[] sourceCodeBytes) throws IOException {
        String sourceCode = new String(sourceCodeBytes, StandardCharsets.UTF_8);
        Set<SolidityFunction> functionsFromFile = findSolidityFunctionsFromSourceFile(new ByteArrayInputStream(sourceCodeBytes));
        SolidityFile savedSolidityFile = saveSolidityFile(sourceCode, functionsFromFile);
        return SolidityConverter.fromEntity(savedSolidityFile);
    }

    public long getSolidityFilesCount() {
        return solidityFileRepository.count();
    }

    Set<SolidityFunction> findSolidityFunctionsFromSourceFile(@NonNull InputStream inputStream) throws IOException {
        requireNonNull(inputStream, "Expected not-null inputStream");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Set<SolidityFunction> solidityFunctions = new HashSet<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Optional<SolidityFunction> function = sourceCodeParser.findFunctionInLine(line);

            function.ifPresent(solidityFunction -> {
                solidityFunctions.add(solidityFunction);
                LOGGER.info("Found function selector: {}", solidityFunction);
            });
        }
        return solidityFunctions;
    }

    private SolidityFile saveSolidityFile(String sourceCode, Set<SolidityFunction> functionsFromFile) {
        String sourceCodeHash = getSourceCodeHash(sourceCode);
        return solidityFileRepository.save(new SolidityFile(sourceCodeHash, sourceCode, functionsFromFile));
    }

    private String getSourceCodeHash(String sourceCode) {
        // (?m) - tells Java to accept the anchors ^ and $ to match at the start and end of each line
        // (otherwise they only match at the start/end of the entire string).
        return sha3String(sourceCode
                .replaceAll("(?m)\\s+$", "")
                .replaceAll("(?m) +", " ")
                .replaceAll("(?m)^\\s+", ""));
    }
}
