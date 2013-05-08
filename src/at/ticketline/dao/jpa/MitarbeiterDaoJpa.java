package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.entity.Mitarbeiter;

public class MitarbeiterDaoJpa extends GenericDaoJpa<Mitarbeiter,Integer> implements MitarbeiterDao {
    @Override
    public Mitarbeiter findById(Integer id) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Mitarbeiter> query = builder.createQuery(Mitarbeiter.class);
        Root<Mitarbeiter> rootMitarbeiter = query.from(Mitarbeiter.class);
        
        query.where(builder.equal( rootMitarbeiter.<Integer>get("id"), id));
        
        return this.entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Mitarbeiter findByUsername(String username) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Mitarbeiter> query = builder.createQuery(Mitarbeiter.class);
        Root<Mitarbeiter> rootMitarbeiter = query.from(Mitarbeiter.class);
        
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        // Bedingung für den Usernamen
        wherePredicates.add(builder.equal( rootMitarbeiter.<String>get("username"),username));

        Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
        
        query.where(whereClause);
        
        return this.entityManager.createQuery(query).getSingleResult();
    }
}
