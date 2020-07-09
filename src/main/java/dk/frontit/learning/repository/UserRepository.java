package dk.frontit.learning.repository;

import dk.frontit.learning.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@Repository
public abstract class UserRepository implements CrudRepository<User, Long> {

    private static final String FIND_BY_USERNAME = "from User u where u.userName=:username";

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Optional<User> findByUsername(String username){
        User user;
        try {
            Query query = entityManager.createQuery(FIND_BY_USERNAME);
            query.setParameter("username", username);
            user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            return  Optional.empty();
        }
        return Optional.ofNullable(user);
    }
}
