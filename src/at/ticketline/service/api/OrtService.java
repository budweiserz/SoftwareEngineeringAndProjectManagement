package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.Ort;

public interface OrtService {
	
	public List<Ort> findByOrt(Ort query);

}
