package at.ticketline.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<E, ID extends Serializable> {

    @Transactional
    public E persist(E entity);

    @Transactional
    public void remove(E entity);

    public E findById(ID id);

    public List<E> findAll();

    @Transactional
    public E merge(E entity);

    public void flush();

    public Long count();

}
