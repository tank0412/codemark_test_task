package ru.codemark.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_login")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
