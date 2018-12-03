package com.piotrjasina.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    List<SolidityFile> findByFunctionsIsContaining(Function function);

    SolidityFile findBySourceCodeHash(String sourceCodeHash);
}
