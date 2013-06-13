package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.entity.Ort;

public class OrtDaoJpa extends GenericDaoJpa<Ort,Integer> implements OrtDao {

	@Override
	public List<Ort> findByOrt(final Ort ort) {

		if (ort == null) {
			throw new IllegalArgumentException("Der Parameter 'ort' darf nicht null sein");
		}

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Ort> query = builder.createQuery(Ort.class);
		Root<Ort> rootOrt = query.from(Ort.class);

		List<Predicate> wherePredicates = new ArrayList<Predicate>();

		if (ort.getBezeichnung() != null) {			
			String nn = ("%"+ort.getBezeichnung()+"%").toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootOrt.<String>get("bezeichnung") ), nn) );
		}

		if (ort.getAdresse() != null) {
			if (ort.getAdresse().getStrasse() != null) {
				String nn = ("%"+ort.getAdresse().getStrasse()+"%").toUpperCase();
	            wherePredicates.add( builder.like( builder.upper( rootOrt.get("adresse").<String>get("strasse") ), nn) );
			}

			if (ort.getAdresse().getPlz() != null) {
				String nn = ("%"+ort.getAdresse().getPlz()+"%").toUpperCase();
	            wherePredicates.add( builder.like( builder.upper( rootOrt.get("adresse").<String>get("plz") ), nn) );
			}

			if (ort.getAdresse().getOrt() != null) {
				String nn = ("%"+ort.getAdresse().getOrt()+"%").toUpperCase();
	            wherePredicates.add( builder.like( builder.upper( rootOrt.get("adresse").<String>get("ort") ), nn) );
			}

			if (ort.getAdresse().getLand() != null) {
				String nn = ("%"+ort.getAdresse().getLand()+"%").toUpperCase();
	            wherePredicates.add( builder.like( builder.upper( rootOrt.get("adresse").<String>get("land") ), nn) );
			}
		}
		
		if (ort.getOrtstyp() != null) {
			wherePredicates.add( builder.equal(rootOrt.get("typ"), ort.getOrtstyp()) );
		}

		Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));

		query.where(whereClause);

		return this.entityManager.createQuery(query).getResultList();
	}

}
