package web.dao;

import org.springframework.stereotype.Repository;
import web.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class UserDaoImp implements UserDao {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @PersistenceContext
    private EntityManager entityManager;

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private boolean uniqEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email=" + "'" + email + "'", User.class);
            return query.getSingleResult() == null;

        } catch (NoResultException ignored) {
            return true;
        }
    }

    @Override
    public void add(User user) {
        String email = user.getEmail();
        if ((user.getFirstName() != null) &
                (user.getLastName() != null) &
                (email != null) &
                ((uniqEmail(email)) && validateEmail(email))) {
            entityManager.persist(user);
        }
    }

    @Override
    public void update(Long id, User user) {
        User oldUser = findById(id);
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        entityManager.merge(oldUser);
    }

    @Override
    public void deleteById(Long id) {
        User user = findById(id);
        if (user != null) entityManager.remove(user);
    }

    @Override
    public User findById(Long id) {
        Optional<User> foundUser = Optional.of(entityManager.find(User.class, id));
        return foundUser.orElse(new User("none", "none", "none"));
    }

    @Override
    public Long findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email=" + "'" + email + "'", User.class);
            return query.getSingleResult().getId();
        } catch (NoResultException ignored) {
            return 0L;
        }
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        List<User> listUsers = query.getResultList();
        if (listUsers.size() > 10) {
            return listUsers.stream().limit(10).toList();
        }
        return listUsers;
    }

}
