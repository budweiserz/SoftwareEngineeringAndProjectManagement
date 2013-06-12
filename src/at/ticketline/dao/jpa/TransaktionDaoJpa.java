package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.entity.Transaktion;

public class TransaktionDaoJpa extends GenericDaoJpa<Transaktion,Integer> implements TransaktionDao {

	@Override
	public List<Transaktion> findByTransaktion(Transaktion transaktion) {
		
		if (transaktion == null) {
    		throw new IllegalArgumentException("Der Suchparameter 'transaktion' darf nicht null sein");
    	}
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaktion> query = builder.createQuery(Transaktion.class);
        Root<Transaktion> rootTransaktion = query.from(Transaktion.class);
        
        List<Predicate> wherePredicates = new ArrayList<Predicate>();
        
        if (transaktion.getKunde() != null) {
            String nn = transaktion.getKunde().getNachname().replace('*', '%').replace('?', '_').toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootTransaktion.<String>get("nachname") ), nn) );
        }
        
        if (transaktion.getKunde() != null) {
            String vn = transaktion.getKunde().getVorname().replace('*', '%').replace('?', '_').toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootTransaktion.<String>get("vorname") ), vn) );
        }
        
        
        if (transaktion.getReservierungsnr() != null) {
            wherePredicates.add( builder.equal(rootTransaktion.<Integer>get("reservierungsNr"), transaktion.getReservierungsnr()) );
        }
        
        Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
        
        query.where(whereClause);
        
        return this.entityManager.createQuery(query).getResultList();
        
	}

}
