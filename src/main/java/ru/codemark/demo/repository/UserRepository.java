package ru.codemark.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.demo.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    List<User> findAll();

    User findByLogin(String login);

    Integer deleteByLogin(String login);
}
