package com.smartcontract.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
interface SolidityFileRepository extends MongoRepository<SolidityFile, String> {

    @Query("{\"solidityFunctions\": {$elemMatch: {\"selector\": {$in: ?0}}}}")
    List<SolidityFile> findSolidityFilesBySelectorContainsAll(Set<String> functionSelector);

    Optional<SolidityFile> findBySourceCodeHash(String sourceCodeHash);
}
