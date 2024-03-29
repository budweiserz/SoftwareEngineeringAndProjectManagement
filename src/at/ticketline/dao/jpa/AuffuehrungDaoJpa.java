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
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;


public class AuffuehrungDaoJpa extends GenericDaoJpa<Auffuehrung,Integer> implements AuffuehrungDao {

    /**
     * @author Michael Ion
     */
    @Override
    public List<Auffuehrung> findByAuffuehrung(final Auffuehrung auffuehrung, final Auffuehrung bisAuffuehrung) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Auffuehrung> query = builder.createQuery(Auffuehrung.class);
        Root<Auffuehrung> rootAuffuehrung = query.from(Auffuehrung.class);
        
        List<Predicate> wherePredicates = new ArrayList<Predicate>();
        
        /*
         *  Suchkriterien sind: Datum/Uhrzeit, Preis, Veranstaltung, Säle
         */
        ParameterExpression<Date> from = builder.parameter(Date.class);
        ParameterExpression<Date> to = builder.parameter(Date.class);
        if(auffuehrung.getDatumuhrzeit() != null && bisAuffuehrung != null && bisAuffuehrung.getDatumuhrzeit() != null) {   
           wherePredicates.add( builder.between(rootAuffuehrung.<Date>get("datumuhrzeit"), from, to));
        }
        
        if(auffuehrung.getVeranstaltung() != null && auffuehrung.getVeranstaltung().getBezeichnung() != null) {
            String vb = ("%"+auffuehrung.getVeranstaltung().getBezeichnung()+"%").toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootAuffuehrung.<Veranstaltung>get("veranstaltung").<String>get("bezeichnung") ), vb) );
        }
        
        if(auffuehrung.getSaal() != null && auffuehrung.getSaal().getBezeichnung() != null) {
            String sb = ("%"+auffuehrung.getSaal().getBezeichnung()+"%").toUpperCase();
            wherePredicates.add( builder.like( builder.upper( rootAuffuehrung.<Saal>get("saal").<String>get("bezeichnung") ), sb) );
        }
        
        if(auffuehrung.getPreis() != null) {
            wherePredicates.add( builder.equal(rootAuffuehrung.get("preis"), auffuehrung.getPreis()));
        }
        
        Predicate whereClause = builder.and(wherePredicates.toArray(new Predicate[0]));
        
        query.where(whereClause);
        
        return this.entityManager.createQuery(query).setParameter(from, auffuehrung.getDatumuhrzeit(), TemporalType.DATE)
                .setParameter(to, bisAuffuehrung.getDatumuhrzeit(), TemporalType.DATE).getResultList();
    }
}
