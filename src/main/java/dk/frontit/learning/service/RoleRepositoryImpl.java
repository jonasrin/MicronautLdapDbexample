package dk.frontit.learning.service;

import dk.frontit.learning.domain.Role;
import dk.frontit.learning.repository.RoleRepository;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Optional;

@Singleton
public class RoleRepositoryImpl {

    @Inject
    RoleRepository roleRepository;


    public Role save(String authority) {
        Role role = new Role(authority);
        return roleRepository.save(role);
    }

    public Optional<Role> find(String authority) {
        return Optional.ofNullable(roleRepository.findByAuthority(authority));
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public void delete(Serializable id) {
        findById((Long) id).ifPresent(role -> roleRepository.delete(role));
    }
}
