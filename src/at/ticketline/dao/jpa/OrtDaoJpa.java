package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.entity.Kuenstler;
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

		}

		if (ort.getAdresse() != null) {
			if (ort.getAdresse().getStrasse() != null) {

			}

			if (ort.getAdresse().getPlz() != null) {

			}

			if (ort.getAdresse().getOrt() != null) {

			}

			if (ort.getAdresse().getLand() != null) {

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
