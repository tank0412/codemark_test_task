package ru.codemark.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
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
