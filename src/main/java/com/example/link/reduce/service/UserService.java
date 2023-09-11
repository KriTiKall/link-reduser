package com.example.link.reduce.service;

import com.example.link.reduce.data.entity.UserEntity;
import com.example.link.reduce.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService implements IUserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return repository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserEntity> getUserByLogin(String name) {
        return repository.findByLogin(name);
    }

    @Override
    public boolean existsByUserLogin(String login) {
        return getUserByLogin(login).isPresent();
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }
}
