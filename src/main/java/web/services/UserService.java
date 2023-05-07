package web.services;

import web.models.User;

import java.util.List;

public interface UserService {
    void add(User user);

    List<User> listUsers();

}
