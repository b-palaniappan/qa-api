package io.c12.bala.db.entity;

import io.c12.bala.api.model.constant.UserStatus;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.smallrye.mutiny.Uni;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;

@MongoEntity(collection = "userMaster")
@Getter
@Setter
public class User extends ReactivePanacheMongoEntityBase {

    @BsonId
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public UserStatus status;
    public Instant createdAt;
    public Instant updatedAt;
    public Instant deletedAt;

    public static Uni<User> findActiveUserById(String id) {
        return find("id = ?1 and status = ?2", id, UserStatus.ACTIVE).firstResult();
    }
}
