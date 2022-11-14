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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
                .map(user -> userToWsdlUser(user, false))
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
            userResponseList.add(userToWsdlUser(userByLogin, true));
        }

        QName qname = new QName("getUserByLoginRequest");
        return new JAXBElement<>(qname,
                GetUsersResponse.class, response);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public JAXBElement<StandardResponse> saveOrUpdateUser(@RequestPayload AddUserRequest request) {
        User userFromRequest = request.getUser();
        StandardResponse standardResponse = new StandardResponse();

        //validate user from request
        List<String> errors = standardResponse.getErrors();
        if (userFromRequest.getLogin() == null) {
            errors.add("Поле login не может быть null");
        }
        if (userFromRequest.getName() == null) {
            errors.add("Поле name не может быть null");
        }
        String userFromRequestPassword = userFromRequest.getPassword();
        if (userFromRequestPassword == null) {
            errors.add("Поле password не может быть null");
        } else {
            //now validate pw
            boolean pwDigitMatch = userFromRequestPassword.matches("(.*\\d.*)");
            boolean pwUppercaseLetterMatch = userFromRequestPassword.matches("(.*[A-Z].*)");

            if (!pwDigitMatch) {
                errors.add("Поле password должно содержать хотя бы одну цифру");
            }

            if (!pwUppercaseLetterMatch) {
                errors.add("Поле password должно содержать хотя бы одну заглавную букву");
            }
        }
        ru.codemark.demo.entity.User savedUser = null;
        if (errors.size() == 0) {
            ru.codemark.demo.entity.User convertedUser = wsdlUserToUser(userFromRequest, true);
            savedUser = userService.saveOrUpdateUser(convertedUser);

        }
        standardResponse.setSuccess(savedUser != null);

        QName qname = new QName("addUserRequest");
        return new JAXBElement<>(qname,
                StandardResponse.class, standardResponse);
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

    private User userToWsdlUser(ru.codemark.demo.entity.User savedUser, boolean includeRoles) {
        User user = new User();
        user.setLogin(savedUser.getLogin());
        user.setName(savedUser.getName());
        //FIXME: Is it really OK to pass user password to FE?
        user.setPassword(savedUser.getPassword());

        if (includeRoles) {
            List<Role> roleList = user.getRole();
            Set<ru.codemark.demo.entity.Role> savedUserRoles = savedUser.getUserRoles();

            if (savedUserRoles != null && savedUserRoles.size() > 0) {
                //convert user role to xml user role
                for (ru.codemark.demo.entity.Role savedRole : savedUserRoles) {
                    Role role = new Role();
                    role.setRoleId(savedRole.getRoleId());
                    role.setName(savedRole.getName());
                    roleList.add(role);
                }
            }
        }

        return user;
    }

    private ru.codemark.demo.entity.User wsdlUserToUser(User savedUser, boolean includeRoles) {
        ru.codemark.demo.entity.User user = new ru.codemark.demo.entity.User();
        user.setLogin(savedUser.getLogin());
        user.setName(savedUser.getName());
        user.setPassword(savedUser.getPassword());

        if (includeRoles) {
            Set<ru.codemark.demo.entity.Role> roleSet = new HashSet<>();
            user.setUserRoles(roleSet);
            List<Role> savedUserUserRoles = savedUser.getRole();

            if (savedUserUserRoles != null && savedUserUserRoles.size() > 0) {
                //convert user role to xml user role
                for (Role savedRole : savedUserUserRoles) {
                    ru.codemark.demo.entity.Role role = new ru.codemark.demo.entity.Role();
                    role.setRoleId(savedRole.getRoleId());
                    role.setName(savedRole.getName());
                    roleSet.add(role);
                }
            }
        }

        return user;
    }

}
