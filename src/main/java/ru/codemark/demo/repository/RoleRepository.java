package ru.codemark.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.demo.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
