package io.c12.bala.db.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;

@MongoEntity(collection = "userMaster")
public class User extends ReactivePanacheMongoEntityBase {

    @BsonId
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String status;
    public Instant createdAt;
    public Instant updatedAt;
    public Instant deletedAt;

}
