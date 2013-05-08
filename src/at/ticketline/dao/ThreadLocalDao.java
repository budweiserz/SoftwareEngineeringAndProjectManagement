package at.ticketline.dao;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalDao extends ThreadLocal<GenericDao<?, ?>> {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalDao.class);
    
    private String className;
    private boolean isGenericDaoJpa = false;
    private Validator validator;

    public ThreadLocalDao(String className, Validator validator) {
        this.className = className;
        this.validator = validator;
        
        GenericDao<?, ?> dao = DaoUtils.createDaoObject(this.className);
        if (DaoUtils.hasSuperclass(dao.getClass(), GenericDaoJpa.class.getName())) {
            this.isGenericDaoJpa = true;
        }
    }
    
    @Override
    protected GenericDao<?, ?> initialValue() {
        LOG.info("Creating new thread-local DAO {}", this.className);
        GenericDao<?, ?> dao = DaoUtils.createDaoObject(this.className);
        if (this.isGenericDaoJpa) {
            GenericDaoJpa<?, ?> jpaDao = (GenericDaoJpa<?, ?>)dao;
            jpaDao.setValidator(this.validator);
        }
        return dao;
    }
    
    @Override
    public void set(GenericDao<?, ?> dao) {
        throw new DaoException("Setting DAO is forbidden");
    }
}
