package at.ticketline.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;

public class VeranstaltungDaoJpa extends GenericDaoJpa<Veranstaltung,Integer> implements VeranstaltungDao {

	@Override
	public List<Veranstaltung> findByVeranstaltung(final Veranstaltung veranstaltung, Integer minDauer, Integer maxDauer) {
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Veranstaltung> query = builder.createQuery(Veranstaltung.class);
		Root<Veranstaltung> rootVeranstaltung = query.from(Veranstaltung.class);

		List<Predicate> wherePredicates = new ArrayList<Predicate>();

		// where Bedingung für Bezeichnung der Veranstaltung
		if (veranstaltung.getBezeichnung() != null) {
			String bezeichnung = ("%"+veranstaltung.getBezeichnung()+"%").toUpperCase();
			wherePredicates.add( builder.like( builder.upper(rootVeranstaltung.<String>get("bezeichnung") ), bezeichnung));
		}

		// where Bedingung für Kategorie der Veranstaltung
		if (veranstaltung.getKategorie() != null) {
			String kategorie = ("%"+veranstaltung.getKategorie()+"%").toUpperCase();
			wherePredicates.add( builder.like( builder.upper(rootVeranstaltung.<String>get("kategorie") ), kategorie));
		}

		// where Bedingung für die minimale Dauer der Veranstaltung
		if (minDauer != null) {
			wherePredicates.add( builder.greaterThanOrEqualTo(rootVeranstaltung.<Integer>get("dauer"), minDauer) );
		}

		// where Bedingung für die maximale Dauer der Veranstaltung
		if (maxDauer != null) {
			wherePredicates.add( builder.lessThanOrEqualTo(rootVeranstaltung.<Integer>get("dauer"), maxDauer) );
		}

		// where Bedingung für den Inhalt der Veranstaltung
		if (veranstaltung.getInhalt() != null) {
			String inhalt = ("%"+veranstaltung.getInhalt()+"%").toUpperCase();
			wherePredicates.add( builder.like( builder.upper(rootVeranstaltung.<String>get("inhalt") ), inhalt));
		}

		Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));

		query.where(whereClause);

		return this.entityManager.createQuery(query).getResultList();
	}
}
