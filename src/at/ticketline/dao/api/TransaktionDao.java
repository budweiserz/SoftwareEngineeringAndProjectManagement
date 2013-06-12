package at.ticketline.dao.api;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Transaktion;

public interface TransaktionDao extends GenericDao<Transaktion,Integer> {
	
	public List<Transaktion> findByTransaktion(Transaktion transaktion);
}
