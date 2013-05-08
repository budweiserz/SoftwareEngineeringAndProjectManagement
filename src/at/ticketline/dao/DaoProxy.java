package at.ticketline.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DaoProxy implements InvocationHandler {
    protected transient final Logger log = LoggerFactory.getLogger(DaoProxy.class);
    
    private ThreadLocalDao threadLocalDao;
    
    protected String targetClassName = null;
    protected Map<Integer, Boolean> methods = new HashMap<Integer, Boolean>();
    protected Field persistenceContextField = null;
    protected Method persistenceContextMethod = null;

    @SuppressWarnings("unchecked")
    public DaoProxy(GenericDao<?, ?> dao, Validator validator) {
        this.targetClassName = dao.getClass().getName();
        this.threadLocalDao = new ThreadLocalDao(this.targetClassName, validator);
        
        Class<EntityManager> emClass;
        try {
            emClass = (Class<EntityManager>)Class.forName("javax.persistence.EntityManager");
        } catch (ClassNotFoundException e) {
            throw new DaoException("Couldn't find EntityManager class", e);
        }
        
        List<Field> fields = DaoUtils.getAllFields(dao.getClass());
        for (Field f : fields) {
            if (f.isAnnotationPresent(PersistenceContext.class)) {
                if (emClass.isAssignableFrom(f.getType())) {
                    this.log.trace("PersistenceContext annotation on field {}.{} found",
                            this.targetClassName, f.getName());
                    this.persistenceContextField = f;
                    break;
                } else {
                    this.log.trace("Field {}.{} with PersistenceContext annotation hasn't type EntityManager",
                            this.targetClassName, f.getName());
                }
            }
        }
        
        if (this.persistenceContextField != null) {
            return;
        }
        
        List<Method> methods = DaoUtils.getAllMethods(dao.getClass());
        for (Method m : methods) {
            if (m.isAnnotationPresent(PersistenceContext.class)) {

                Class<?>[] parameters = m.getParameterTypes();
                if (parameters.length != 1) {
                    this.log.trace("Method {}.{}() has PersistenceContext annotation, but has invalid number of parameters - must be one parameter", this.targetClassName, m.getName());
                    continue;
                }

                if (emClass.isAssignableFrom(parameters[0])) {
                  this.persistenceContextMethod = m;
                    this.log.trace(
                            "PersistenceContext annotation on method {} - {} found", this.targetClassName, m.getName());
                  
                } else {
                    this.log.trace("Parameter of method {} - {} is not compatible to javax.persistence.EntityManager", this.targetClassName, m.getName());
                }
                break;
            }
        }
        if (this.persistenceContextMethod != null) {
            return;
        }
        this.log.warn("No PersistenceContext annotation on {} found", this.targetClassName);
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args)
            throws Throwable {
        Object result = null;
        boolean localTransaction = false;
        boolean hasException = false;
        EntityManager em = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = em.getTransaction();

        if (this.isMethodTransactional(method)) {
            if (transaction.isActive()) {
                this.log.trace("Joining transaction for {}.{}()",
                        this.targetClassName, method.getName());
            } else {
                this.log.trace("Transaction for {}.{}() started",
                        this.targetClassName, method.getName());
                transaction.begin();
                localTransaction = true;
            }
        } else {
            this.log.trace("No transaction on {}.{}()", this.targetClassName,
                    method.getName());
        }

        this.setEntityManager(em);

        try {
            result = method.invoke(this.threadLocalDao.get(), args);
        } catch (InvocationTargetException e) {
            em.close();
            hasException = true;
            Throwable cause = e.getTargetException();
            if ((cause instanceof DaoException) || (cause instanceof ConstraintViolationException)) {
                throw cause;
            } else {
                throw new DaoException(cause);
            }
        } catch (Exception e) {
            hasException = true;
            throw new DaoException(e);
        } finally {
            if ((hasException) && (localTransaction)) {
                this.log.trace("Transaction rollback");
                transaction.rollback();
            } else if ((hasException) && (transaction.isActive())) {
                this.log.trace("Mark transaction to be rollbacked back");
                transaction.setRollbackOnly();
            }
        }

        if (localTransaction) {
            this.log.trace("Committing transaction");
            try {
                if (transaction.isActive()) {
                    transaction.commit();
                }
            } catch (RollbackException rbe) {
                throw new DaoException("Transaction failed", rbe);
            }
        }
        return result;
    }

    protected boolean isMethodTransactional(Method m) {
        int hashCode = m.hashCode();
        if (this.methods.containsKey(hashCode) == false) {
            this.methods.put(hashCode, m
                    .isAnnotationPresent(Transactional.class));
        }
        return this.methods.get(hashCode);
    }

    protected void setEntityManager(EntityManager em) {
        GenericDao<?, ?> dao = this.threadLocalDao.get();
        if (this.persistenceContextField != null) {
            try {
                this.persistenceContextField.set(dao, em);
            } catch (Exception e) {
                throw new DaoException(e);
            }
            return;
        }
        if (this.persistenceContextMethod != null) {
            try {
                this.persistenceContextMethod.invoke(dao, em);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

}
