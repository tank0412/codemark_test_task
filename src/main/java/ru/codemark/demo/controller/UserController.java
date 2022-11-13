package ru.codemark.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.codemark.demo.generated.*;
import ru.codemark.demo.service.UserService;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class UserController {
    private final static String NAMESPACE_URI = "http://codemark.ru/demo/generated";
    @Autowired
    private UserService userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsersRequest")
    @ResponsePayload
    public JAXBElement<GetUsersResponse> getAllUsers() {
        List<ru.codemark.demo.entity.User> allUsers = userService.getAllUsers();
        GetUsersResponse response = new GetUsersResponse();
        List<User> userResponseList = response.getUser();

        List<User> convertedSavedUsers = allUsers.stream()
                .map(this::userToWsdlUser)
                .collect(Collectors.toList());
        userResponseList.addAll(convertedSavedUsers);


        QName qname = new QName("getUsersRequest");
        return new JAXBElement<>(qname,
                GetUsersResponse.class, response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    @ResponsePayload
    public JAXBElement<GetUsersResponse> getUserByLogin(@RequestPayload GetUserByLoginRequest request) {
        ru.codemark.demo.entity.User userByLogin = userService.getUserByLogin(request.getLogin());
        GetUsersResponse response = new GetUsersResponse();
        List<User> userResponseList = response.getUser();

        if (userByLogin != null) {
            userResponseList.add(userToWsdlUser(userByLogin));
        }

        QName qname = new QName("getUserByLoginRequest");
        return new JAXBElement<>(qname,
                GetUsersResponse.class, response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserByLoginRequest")
    @ResponsePayload
    public JAXBElement<StandardResponse> deleteUserByLogin(@RequestPayload DeleteUserByLoginRequest request) {
        Integer deletedNumber = userService.deleteUserByLogin(request.getLogin());

        StandardResponse response = new StandardResponse();
        response.setSuccess(deletedNumber > 0);

        QName qname = new QName("deleteUserByLoginRequest");
        return new JAXBElement<>(qname,
                StandardResponse.class, response);
    }

    private User userToWsdlUser(ru.codemark.demo.entity.User savedUser) {
        User user = new User();
        user.setLogin(savedUser.getLogin());
        user.setName(savedUser.getName());
        return user;
    }

}
