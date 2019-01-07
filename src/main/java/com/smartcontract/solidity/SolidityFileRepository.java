package com.smartcontract.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    @Query("{\"solidityFunctions\": {$elemMatch: {\"selector\": ?0}}}")
    List<SolidityFile> findSolidityFilesByFunctionSelector(String functionSelector);

    Optional<SolidityFile> findBySourceCodeHash(String sourceCodeHash);
}
