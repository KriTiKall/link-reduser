package com.example.link.reduce.model.interfaces;

import com.example.link.reduce.data.entity.UserEntity;
import com.example.link.reduce.model.User;

import java.util.Optional;

public interface IUserService {

    User register(UserEntity userEntity);

    @Deprecated
    Optional<UserEntity> getUserById(Long id);

    boolean existsByUserLogin(String login);


    User getUserByLogin(String login);

    boolean existsByUserEmail(String email);
}
