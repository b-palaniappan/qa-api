package io.c12.bala.db.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.util.Date;

@MongoEntity(collection = "userMaster")
public class User extends ReactivePanacheMongoEntity {

    public String firstName;
    public String lastName;
    public String email;
    public String status;
    public Date createdAt;
    public Date updatedAt;
    public Date deletedAt;

}
