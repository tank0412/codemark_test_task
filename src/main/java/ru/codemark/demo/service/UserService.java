package ru.codemark.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codemark.demo.entity.Role;
import ru.codemark.demo.entity.User;
import ru.codemark.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

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

    @Transactional
    public User saveOrUpdateUser(User user) {
        User userByLogin = userRepository.findByLogin(user.getLogin());
        if (userByLogin != null) {
            //this field will not be null because it was checked earlier
            userByLogin.setPassword(user.getPassword());
            userByLogin.setName(user.getName());

            Hibernate.initialize(userByLogin.getUserRoles());
            Set<Role> userRoles = user.getUserRoles();
            if (userRoles != null && userRoles.size() > 0) {
                userByLogin.setUserRoles(userRoles);
            }
            user = userByLogin;
        }
        User savedUser = userRepository.save(user);
        Hibernate.initialize(savedUser.getUserRoles());
        return savedUser;
    }

    @Transactional
    public Integer deleteUserByLogin(String login) {
        return userRepository.deleteByLogin(login);
    }

}
