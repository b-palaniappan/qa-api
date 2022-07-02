package io.c12.bala.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.c12.bala.api.exception.UserNotFoundException;
import io.c12.bala.api.model.constant.UserStatus;
import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.db.entity.User;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
@Slf4j
public class UserService {

    @Inject
    ModelMapper modelMapper;

    UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Multi<UserDto> getAllUsersPage(int pageIndex, int pageSize) {
        pageIndex = pageIndex - 1;  // page index start at zero.
        if (pageIndex < 0) {
            pageIndex = 0;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        Multi<User> userMulti = User.findAll().page(Page.of(pageIndex, pageSize)).stream();
        return userMulti.onItem().transform(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> findUserById(String id) {
        Uni<User> uniUser = User.findActiveUserById(id);
        return uniUser.onItem().ifNull().failWith(() -> new UserNotFoundException(id))
                .onItem().ifNotNull().transform(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        // Create nanoid
        user.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 25);
        user.createdAt = Instant.now();
        user.updatedAt = Instant.now();
        user.status = UserStatus.ACTIVE;
        return user.persist().map(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> updateUser(String id, UserDto userDto) {
        Uni<User> uniUpdateUser = User.findById(id);
        return uniUpdateUser.onItem().transform(user -> {
            user.firstName = userDto.getFirstName();
            user.lastName = userDto.getLastName();
            user.email = userDto.getEmail();
            user.updatedAt = Instant.now();
            return user;
        }).call(u -> u.persistOrUpdate()).map(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> partialUpdateUser(String id, UserDto userDto) {
        Uni<User> uniUser = User.findById(id);
        return uniUser.onItem().transform(u -> {
            if (Objects.nonNull(userDto.getFirstName())) {
                u.firstName = userDto.getFirstName();
            }
            if (Objects.nonNull(userDto.getLastName())) {
                u.lastName = userDto.getLastName();
            }
            if (Objects.nonNull(userDto.getEmail())) {
                u.email = userDto.getEmail();
            }
            u.updatedAt = Instant.now();
            return u;
        }).call(u -> u.persistOrUpdate()).map(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> deleteUser(String id) {
        Uni<User> uniUser = User.findById(id);
        return uniUser.onItem().transform(u -> {
            u.deletedAt = Instant.now();
            u.status = UserStatus.TERMINATED;
            return u;
        }).call(u -> u.persistOrUpdate()).map(u -> modelMapper.map(u, UserDto.class));
    }

}
