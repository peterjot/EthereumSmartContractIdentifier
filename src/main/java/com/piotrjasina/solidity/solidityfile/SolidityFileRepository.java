package com.piotrjasina.solidity.solidityfile;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    List<SolidityFile> findByFunctionSelectorsContaining(String functionSelector);

    Optional<SolidityFile> findBySourceCodeHash(String sourceCodeHash);

}
