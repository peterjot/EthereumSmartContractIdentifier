package com.piotrjasina.solidity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FunctionRepository extends MongoRepository<Function, String> {
    Function findBySelector(String selector);

    Function findBySignature(String signature);
}
