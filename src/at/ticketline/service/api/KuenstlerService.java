package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.Kuenstler;

public interface KuenstlerService {

    public void save(Kuenstler kuenstler);
    
    public List<Kuenstler> findByKuenstler(Kuenstler query);
    
    public List<Kuenstler> findByKuenstlerWithAnySex(Kuenstler query);
}
