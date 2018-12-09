package com.piotrjasina.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FunctionRepository extends MongoRepository<Function, String> {
    Optional<Function> findBySelector(String selector);

    Optional<Function> findBySignature(String signature);
}
