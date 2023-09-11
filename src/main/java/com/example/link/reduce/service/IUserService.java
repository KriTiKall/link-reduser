package com.example.link.reduce.service;

import com.example.link.reduce.data.entity.UserEntity;

import java.util.Optional;

public interface IUserService {

    UserEntity saveUser(UserEntity userEntity);

    Optional<UserEntity> getUserById(Long id);

    boolean existsByUserLogin(String login);


    Optional<UserEntity> getUserByLogin(String login);

    boolean existsByUserEmail(String email);
}
