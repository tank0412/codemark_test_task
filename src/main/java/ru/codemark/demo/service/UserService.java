package ru.codemark.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codemark.demo.dto.UserDto;
import ru.codemark.demo.entity.User;
import ru.codemark.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        Hibernate.initialize(userByLogin.getUserRoles());
        return userByLogin;
    }

    @Transactional
    public Integer deleteUserByLogin(String login) {
        return userRepository.deleteByLogin(login);
    }

//    private List<UserDto> usersToDto(List<User> users) {
//        return users
//                .stream()
//                .map(user -> new UserDto(user.getLogin(), user.getName()))
//                .collect(Collectors.toList());
//    }
}
