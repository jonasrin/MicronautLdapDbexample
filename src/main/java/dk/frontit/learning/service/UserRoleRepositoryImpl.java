package dk.frontit.learning.service;

import dk.frontit.learning.domain.Role;
import dk.frontit.learning.domain.User;
import dk.frontit.learning.domain.UserRole;
import dk.frontit.learning.repository.UserRoleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRoleRepositoryImpl  {

    @Inject
    UserRoleRepository repository;

    public UserRole save(User user, Role role) {
        UserRole userRole = new UserRole(user, role);
        return repository.save(userRole);
    }

    public Optional<UserRole> find(User user, Role role) {
        return repository.findByUserAndRole(user, role);
    }

    public void delete(Serializable id) {
        findById(id).ifPresent(userRole -> repository.delete(userRole));
    }

    public Optional<UserRole> findById(Serializable id) {
        return repository.findById((Long) id);

    }

    public List<String> findAllAuthoritiesByUsername(String username) {
        return repository.findAuthoritiesByUsername(username);
    }
}
