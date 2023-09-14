package com.example.link.reduce.service;

import com.example.link.reduce.data.entity.UserEntity;
import com.example.link.reduce.data.repository.UserRepository;
import com.example.link.reduce.model.dto.User;
import com.example.link.reduce.model.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;


    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User register(UserEntity userEntity) {
        return map(repository.save(userEntity));
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return map(repository.findByLogin(login).get());
    }

    public User map(UserEntity entity) {
        return new User(
                entity.getLogin(),
                entity.getName(),
                entity.getEmail()
        );
    }

    @Override
    public boolean existsByUserLogin(String login) {
        return repository.findByLogin(login).isPresent();
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }
}
