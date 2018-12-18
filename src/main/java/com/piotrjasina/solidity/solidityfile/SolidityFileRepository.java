package com.piotrjasina.solidity.solidityfile;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    @Query("{\"functions\": {$elemMatch: {\"selector\": ?0}}}")
    List<SolidityFile> findByFunctionSelectorsIsContaining(String functionSelector);

    @Query("{\"functions\": {$elemMatch: {\"signature\": ?0}}}")
    List<SolidityFile> findByFunctionSignaturesIsContaining(String functionSignature);

    List<SolidityFile> findByFunctionsIsContaining(Function function);


    Optional<SolidityFile> findBySourceCodeHash(String sourceCodeHash);

}
