package com.service.service.repository;
import com.service.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be defined here
    Optional<User> findByExternalId(String externalId);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByExternalIdAndTargetUserType(String externalId, String targetUserType);
    Optional<User> findByEmailAndTargetUserType(String email, String targetUserType);
    Optional<User> findByNameAndTargetUserType(String name, String targetUserType);
    Optional<User> findById(String externalId);
}