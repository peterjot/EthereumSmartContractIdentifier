package com.smartcontract.authentication;

import com.example.gtommee.rest_tutorial.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {
	Users findByUsername(String username);	
}