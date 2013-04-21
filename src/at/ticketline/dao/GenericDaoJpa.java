package at.ticketline.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericDaoJpa<E, ID extends Serializable> implements GenericDao<E, ID> {
	protected transient final Logger log = LoggerFactory.getLogger(GenericDaoJpa.class);
	protected Class<E> entityClass;
	protected String findAllQueryString;
	protected String countQueryString;
	protected Validator validator;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public GenericDaoJpa() {
		ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
		this.findAllQueryString = "SELECT e FROM " + this.entityClass.getSimpleName() + " e";
		this.countQueryString = "SELECT COUNT(e) FROM " + this.entityClass.getSimpleName() + " e";
	}
	
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void remove(E entity) {
		this.entityManager.remove(entity);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		return (List<E>) this.entityManager
		.createQuery(this.findAllQueryString).getResultList();
	}

	public E findById(ID id) {
		return this.entityManager.find(entityClass, id);
	}

	public Long count() {
		return (Long)this.entityManager
				.createQuery(this.countQueryString).getSingleResult();
	}
	
	public E merge(E entity) {
		Set<ConstraintViolation<E>> violations = this.validator.validate(entity);
		if (violations.isEmpty() == false) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
		
		return this.entityManager.merge(entity);
	}
	
    public void flush() {
    	this.entityManager.flush();
    }
    
	public E persist(E entity) {
		Set<ConstraintViolation<E>> violations = this.validator.validate(entity);
		if (violations.isEmpty() == false) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
		
		this.entityManager.persist(entity);
		return entity;
	}

}
