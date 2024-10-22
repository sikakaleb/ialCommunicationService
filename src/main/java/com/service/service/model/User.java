package com.service.service.model;

import com.service.service.entities.TargetUserType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User {
    @Id
    private String id;  // L'ID interne de MongoDB
    private String name;
    private String email;
    private String externalId;  // Le nouvel ID externe (doctorxxxx, nursexxxx, etc.)
    private TargetUserType targetUserType; // Type d'utilisateur

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public TargetUserType getTargetUserType() {
        return targetUserType;
    }

    public void setTargetUserType(TargetUserType targetUserType) {
        this.targetUserType = targetUserType;
    }
}
