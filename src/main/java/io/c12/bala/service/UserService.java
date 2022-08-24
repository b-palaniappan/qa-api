package io.c12.bala.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.c12.bala.api.exception.UserNotFoundException;
import io.c12.bala.api.model.constant.UserStatus;
import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.db.entity.User;
import io.c12.bala.rest.client.GeoCodeService;
import io.c12.bala.rest.client.model.geo.GeoCode;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Objects;

import static io.c12.bala.api.config.ApiConstants.NANOID_DEFAULT_ALPHABET;
import static io.c12.bala.api.config.ApiConstants.USER_UNIQUE_ID_PREFIX;

@ApplicationScoped
@Slf4j
public class UserService {

    @Inject
    ModelMapper modelMapper;

    @Inject
    @RestClient
    GeoCodeService geoCodeService;

    @ConfigProperty(name = "api-key")
    String hereMapApiKey;

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
        Uni<User> uniUser = User.findByIdAndStatusActive(id);
        return uniUser.onItem().ifNull().failWith(() -> new UserNotFoundException(id))
            .onItem().ifNotNull().transform(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        // Create nanoid
        user.id = USER_UNIQUE_ID_PREFIX + NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NANOID_DEFAULT_ALPHABET, 25);
        user.createdAt = Instant.now();
        user.updatedAt = Instant.now();
        user.status = UserStatus.ACTIVE;

        // Try GeoCode service
        Uni<GeoCode> geoCodeSetUni = this.geoCodeService.getGeoCodeByAddress("5003 Hawthorne Dr West Des Moines Iowa", hereMapApiKey, "countryCode:USA");
        // For logging only.
        geoCodeSetUni.subscribe().with(result -> log.info("data - {}", result), failure -> log.error("failed with exception ", failure));

        return user.persist().map(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> updateUser(String id, UserDto userDto) {
        Uni<User> uniUpdateUser = User.findByIdAndStatusActive(id).onItem().ifNull().failWith(() -> new UserNotFoundException(id));
        return uniUpdateUser.onItem().transform(user -> {
            user.firstName = userDto.getFirstName();
            user.lastName = userDto.getLastName();
            user.email = userDto.getEmail();
            user.updatedAt = Instant.now();
            return user;
        }).call(u -> u.persistOrUpdate()).map(u -> modelMapper.map(u, UserDto.class));
    }

    public Uni<UserDto> partialUpdateUser(String id, UserDto userDto) {
        Uni<User> uniUser = User.findByIdAndStatusActive(id).onItem().ifNull().failWith(() -> new UserNotFoundException(id));
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
        Uni<User> uniUser = User.findByIdAndStatusActive(id);
        return uniUser.onItem().ifNull().fail()
            .onItem().ifNotNull().transform(u -> {
                u.deletedAt = Instant.now();
                u.status = UserStatus.TERMINATED;
                return u;
            }).call(u -> u.persistOrUpdate()).map(u -> modelMapper.map(u, UserDto.class))
            .onFailure().recoverWithNull();
    }

}
