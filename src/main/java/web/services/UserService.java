package web.services;

import web.models.User;

import java.util.List;

public interface UserService {
    void add(User user);

    void update(User user);

    void deleteById(Long id);

    User findById(Long id);

    Long findByEmail(String email);

    List<User> listUsers();
}
