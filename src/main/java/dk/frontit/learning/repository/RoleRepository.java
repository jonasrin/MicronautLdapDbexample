package dk.frontit.learning.repository;

import dk.frontit.learning.domain.Role;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
public abstract class RoleRepository implements CrudRepository<Role, Long> {

    EntityManager entityManager;

    public RoleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Role findByAuthority(String authority) {
        Role role;
        try {
            Query query = entityManager.createQuery("from Role r where r.authority=:auth");
            query.setParameter("auth", authority);
            role = (Role) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return role;
    }
}
