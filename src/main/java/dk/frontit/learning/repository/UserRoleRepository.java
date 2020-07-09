package dk.frontit.learning.repository;

import dk.frontit.learning.domain.Role;
import dk.frontit.learning.domain.User;
import dk.frontit.learning.domain.UserRole;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public abstract class UserRoleRepository implements CrudRepository<UserRole, Long> {

    private static final String FIND_AUTHORITIES_BY_USERNAME = "FROM UserRole ur where ur.user.userName=:username";
    private static final String FIND_BY_USER_AND_ROLE = "FROM UserRole ur where ur.user.id=:userid and ur.role.id=:roleid";

    EntityManager entityManager;

    public UserRoleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List<String> findAuthoritiesByUsername(String username) {
        Query query = entityManager.createQuery(FIND_AUTHORITIES_BY_USERNAME);
        query.setParameter("username", username);
        return query.getResultList();
    }

    @Transactional
    public Optional<UserRole> findByUserAndRole(User user, Role role) {
        UserRole userRole;
        try {
            Query query = entityManager.createQuery(FIND_BY_USER_AND_ROLE);
            query.setParameter("userid", user.getId());
            query.setParameter("roleid", role.getId());
            userRole = (UserRole) query.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRole);
    }
}
