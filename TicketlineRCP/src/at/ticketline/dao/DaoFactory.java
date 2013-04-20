package at.ticketline.dao;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.BaseEntity;

public class DaoFactory {
	protected static final Logger LOG = LoggerFactory.getLogger(DaoFactory.class);
	
	protected static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	protected static final Map<String, GenericDao<?, ?>> DAO_MAP = new HashMap<String, GenericDao<?, ?>>();
	
	public static GenericDao<?, ?> getByEntity(Class<? extends BaseEntity> clazz) {
		if (clazz == null) {
			throw new DaoException("Parameter clazz may not be null");
		}
		
		String name = clazz.getSimpleName();
		
		if (DAO_MAP.containsKey(name)) {
			LOG.info("Using existing DAO for entity " + name);
			return DAO_MAP.get(name);
		}
		
		String className = "at.ticketline.dao.jpa." + name + "DaoJpa";
		LOG.info("Creating Dao {}", className);
		
		GenericDao<?, ?> dao = DaoUtils.createDaoObject(className);
		List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(dao.getClass());
		dao = (GenericDao<?, ?>)Proxy.newProxyInstance(dao.getClass().getClassLoader(), allInterfaces.toArray(new Class<?>[allInterfaces.size()]), new DaoProxy(dao, VALIDATOR));
			
		DAO_MAP.put(name, dao);
		
		return dao;
	}

	public static Validator getValidator() {
		return DaoFactory.VALIDATOR;
	}
}