package at.ticketline.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

public class DaoUtils {

	private DaoUtils() {
	}
	
	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> l = new ArrayList<Field>();
		if (clazz == null) {
			return l;
		}
		l.addAll(Arrays.asList(clazz.getDeclaredFields()));
		if (clazz.getSuperclass() != null) {
			l.addAll(DaoUtils.getAllFields(clazz.getSuperclass()));
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> c : interfaces) {
			l.addAll(DaoUtils.getAllFields(c));
		}
		return l;
	}

	public static List<Method> getAllMethods(Class<?> clazz) {
		List<Method> l = new ArrayList<Method>();
		if (clazz == null) {
			return l;
		}
		l.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		if (clazz.getSuperclass() != null) {
			l.addAll(DaoUtils.getAllMethods(clazz.getSuperclass()));
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> c : interfaces) {
			l.addAll(DaoUtils.getAllMethods(c));
		}
		return l;
	}
	
	public static boolean hasSuperclass(Class<?> clazz, String superclass) {
		if (clazz == null) {
			throw new DaoException("Parameter clazz may not be null");
		}
		if (StringUtils.isEmpty(superclass)) {
			throw new DaoException("Parameter superclass may not be null");
		}

		List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(clazz);
		for (Class<?> c : allSuperclasses) {
			if (superclass.equals(c.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static GenericDao<?, ?> createDaoObject(String className) {
		if (StringUtils.isEmpty(className)) {
			throw new DaoException("Parameter className may not be null or empty");
		}
		try {
			Class<?> clazz = Class.forName(className);
			if (DaoUtils.hasInterface(clazz, GenericDao.class.getName()) == false) {
				throw new DaoException("DAO " + className + " must implement interface " + GenericDao.class.getName());
			}
			return (GenericDao<?, ?>) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new DaoException(e);
		} catch (IllegalAccessException e) {
			throw new DaoException(e);
		} catch (ClassNotFoundException e) {
			throw new DaoException("DAO class " + className + " couldn't be found", e);
		}
	}
	
	public static boolean hasInterface(Class<?> clazz, String interfaceName) {
		if (clazz == null) {
			throw new DaoException("Parameter clazz may not be null");
		}
		if (StringUtils.isEmpty(interfaceName)) {
			throw new DaoException("Parameter interfaceName may not be null");
		}
		List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(clazz);
		for (Class<?> i : allInterfaces) {
			if (interfaceName.equals(i.getName())) {
				return true;
			}
		}
		return false;
	}
}
