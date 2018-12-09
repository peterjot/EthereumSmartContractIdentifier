package com.piotrjasina.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    List<SolidityFile> findByFunctionsIsContaining(Function function);

    Optional<SolidityFile> findBySourceCodeHash(String sourceCodeHash);
}
