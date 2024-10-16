package com.service.service.repository;
import com.service.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be defined here
}