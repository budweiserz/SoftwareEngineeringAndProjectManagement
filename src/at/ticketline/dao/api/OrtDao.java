package at.ticketline.dao.api;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Ort;


public interface OrtDao extends GenericDao<Ort,Integer> {
	
	public List<Ort> findByOrt(Ort query);

}
