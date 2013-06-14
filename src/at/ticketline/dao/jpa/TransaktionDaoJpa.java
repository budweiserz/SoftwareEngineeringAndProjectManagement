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
import at.ticketline.entity.Transaktionsstatus;

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

        wherePredicates.add( builder.notEqual(rootTransaktion.<Integer>get("status"), Transaktionsstatus.STORNO) );
        
        if (transaktion.getKunde() != null && transaktion.getKunde().getNachname() != null) {
            String nn = ("%"+transaktion.getKunde().getNachname()+"%").toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootTransaktion.get("kunde").<String>get("nachname") ), nn) );
        }
        
        if (transaktion.getKunde() != null && transaktion.getKunde().getVorname() != null) {
            String vn = ("%"+transaktion.getKunde().getVorname()+"%").toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootTransaktion.get("kunde").<String>get("vorname") ), vn) );
        }
        
        if (transaktion.getReservierungsnr() != null) {
            wherePredicates.add( builder.equal(rootTransaktion.<Integer>get("reservierungsNr"), transaktion.getReservierungsnr()) );
        }
        
        Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
        
        query.where(whereClause);
        
        return this.entityManager.createQuery(query).getResultList();
        
	}
}
