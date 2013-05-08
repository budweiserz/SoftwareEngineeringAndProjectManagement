package at.ticketline.dao.jpa;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.entity.Mitarbeiter;

public class MitarbeiterDaoJpa extends GenericDaoJpa<Mitarbeiter,Integer> implements MitarbeiterDao {

    @Override
    public Mitarbeiter findByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

}
