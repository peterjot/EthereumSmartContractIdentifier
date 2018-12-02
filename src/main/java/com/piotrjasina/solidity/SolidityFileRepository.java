package com.piotrjasina.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    public SolidityFile findByFunctionsIsContaining(Function function);
}
