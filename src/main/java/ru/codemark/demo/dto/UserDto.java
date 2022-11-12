package ru.codemark.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserDto implements Serializable {
    private String login;

    private String name;
}
