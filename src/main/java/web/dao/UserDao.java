package web.dao;

import web.models.User;

import java.util.List;

public interface UserDao {
    void add(User user);

    void update(Long id, User user);

    void deleteById(Long id);

    User findById(Long id);

    Long findByEmail(String email);

    List<User> listUsers();
}
