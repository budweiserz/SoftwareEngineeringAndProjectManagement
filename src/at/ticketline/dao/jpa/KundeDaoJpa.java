package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.entity.Kunde;

public class KundeDaoJpa extends GenericDaoJpa<Kunde,Integer> implements KundeDao {

    @Override
    public List<Kunde> findByKunde(final Kunde kunde) {
    	if (kunde == null) {
    		throw new IllegalArgumentException("Der Suchparameter 'kunde' darf nicht null sein");
    	}
    	
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Kunde> query = builder.createQuery(Kunde.class);
        Root<Kunde> rootKuenstler = query.from(Kunde.class);
        
        List<Predicate> wherePredicates = new ArrayList<Predicate>();
        
        if (kunde.getNachname() != null) {
            String nn = kunde.getNachname().replace('*', '%').replace('?', '_').toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootKuenstler.<String>get("nachname") ), nn) );
        }
        
        if (kunde.getVorname() != null) {
            String vn = kunde.getVorname().replace('*', '%').replace('?', '_').toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootKuenstler.<String>get("vorname") ), vn) );
        }
        
        Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
        
        query.where(whereClause);
        
        return this.entityManager.createQuery(query).getResultList();
    }

}
