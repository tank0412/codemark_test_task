package ru.codemark.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codemark.demo.entity.User;
import ru.codemark.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User getUserByLogin(String login) {
        User userByLogin = userRepository.findByLogin(login);
        if (userByLogin != null) {
            Hibernate.initialize(userByLogin.getUserRoles());
        }
        return userByLogin;
    }

    public User saveOrUpdateUser(User user) {
        User savedUser = userRepository.save(user);
        Hibernate.initialize(savedUser.getUserRoles());
        return savedUser;
    }

    @Transactional
    public Integer deleteUserByLogin(String login) {
        return userRepository.deleteByLogin(login);
    }
}
