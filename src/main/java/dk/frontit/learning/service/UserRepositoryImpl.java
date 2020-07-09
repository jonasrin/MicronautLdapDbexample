package dk.frontit.learning.service;

import dk.frontit.learning.domain.User;
import dk.frontit.learning.repository.UserRepository;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@Singleton
public class UserRepositoryImpl {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User save(String email, String username, String password) {
        User user = new User(email, username, password);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Serializable id) {
        return userRepository.findById((Long) id);
    }

    public void delete(Serializable id) {
        findById(id).ifPresent(user -> userRepository.delete(user));
    }
}
