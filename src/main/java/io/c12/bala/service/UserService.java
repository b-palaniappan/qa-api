package io.c12.bala.service;

import io.c12.bala.api.model.user.UserDto;
import io.c12.bala.db.entity.User;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    ModelMapper modelMapper;

    UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Multi<UserDto> getAllUsersPage(int pageIndex, int pageSize) {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        Multi<User> userMulti = User.findAll().page(Page.of(pageIndex, pageSize)).stream();
        return userMulti.onItem().transform(u -> modelMapper.map(u, UserDto.class));
    }

}
