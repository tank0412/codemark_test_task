package ru.codemark.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.demo.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}