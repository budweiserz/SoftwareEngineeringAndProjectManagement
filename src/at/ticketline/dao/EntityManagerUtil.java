package at.ticketline.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Erzeugt und speichert den aktuellen EntityManager.
 * Jeder Thread, erhaelt einen eigenen EntityManager, nachdem der EntityManager
 * nicht thread-sicher ist. 
 * Ausserdem bietet es Methoden fuer das Transaktionshandling an.
 */
public class EntityManagerUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(EntityManagerUtil.class);
    
    private static String UNIT_NAME = null;
    private static PersistenceProvider PROVIDER = null;
    private static EntityManagerFactory FACTORY = null;
    private static final ThreadLocal<EntityManager> ENTITY_MANAGER = new ThreadLocal<EntityManager>();

    /**
     * Initialisiert das EntityManagerUtil und muss vor der ersten Verwendung aufgerufen werden.
     *
     * @param unitName Name der Persistence Unit, die in persistence.xml angegeben ist
     * @param persistenceProvider Der zu verwendende Persistence Provider
     * @throws DaoException Wenn der Unit Name <code>null</code> oder leer ist, der Persistence Provider <code>null</code> ist, die Methode bereits aufgerufen wurde oder bei Fehlern in der Initialisierung
     */
    public static synchronized void init(String unitName, PersistenceProvider persistenceProvider) {
        if ((unitName == null) || (unitName.trim().length() == 0)) {
            throw new DaoException("Persistence Unit Name may not be null or empty");
        }
        if (persistenceProvider == null) {
            throw new DaoException("Persistence Provider may not be null");
        }
        if (FACTORY != null) {
            throw new DaoException("EntityManagerUtil already initialized");
        }
        LOG.info("Initializing EntityManagerUtil for persistence unit {}", unitName);
        UNIT_NAME = unitName;
        PROVIDER = persistenceProvider;
        try {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put(PersistenceUnitProperties.CLASSLOADER, EntityManagerUtil.class.getClassLoader());
            FACTORY = PROVIDER.createEntityManagerFactory(EntityManagerUtil.UNIT_NAME, map);
        } catch (Exception e) {
            throw new DaoException("Couldn't initialize EntityManagerUtil", e);
        }
    }
    
    /**
     * Liefert den EntityManager des aktuellen Threads. Erzeugt einen neuen
     * EntityManager, wenn noch keiner vorhanden ist.
     *
     * @return den vorhandenen oder neuen EntityManager
     * @throws DaoException Wenn die Klasse noch nicht mit {@link #init(String)} initalisiert wurde
     * @see java.lang.ThreadLocal
     */
    public static EntityManager getCurrentEntityManager() {
        if (FACTORY == null) {
            throw new DaoException("EntityManagerUtil not initialized");
        }
        
        EntityManager em = ENTITY_MANAGER.get();
        if ((em == null) || (em.isOpen() == false)) {
            LOG.info("Creating new entity manager");
            em = EntityManagerUtil.FACTORY.createEntityManager();
            ENTITY_MANAGER.set(em);
        }
        return em;
    }
    
    /**
     * Schliesst den EntityManager des aktuellen Threads. Falls noch kein EntityManager an den Thread
     * gebunden ist oder der EntityManager nicht offen ist, wird nichts gemacht.
     *
     * @throws DaoException Wenn die Klasse noch nicht mit {@link #init(String)} initalisiert wurde
     * @see java.lang.ThreadLocal
     */
    public static void closeCurrentEntityManager() {
        if (FACTORY == null) {
            throw new DaoException("EntityManagerUtil not initialized");
        }
        
        EntityManager em = ENTITY_MANAGER.get();
        if ((em == null) || (em.isOpen() == false)) {
            return;
        }
        em.close();
        ENTITY_MANAGER.remove();
    }
    
    /**
     * Startet eine neue Transaktion. 
     * Ist bereits eine Transaktion aktiv, wird diese weiterverwendet.
     *
     * @throws DaoException wenn die Klasse nicht initialisiert wurde oder ein Fehler auftritt
     * @see javax.persistence.EntityTransaction#begin()
     */
    public static void beginTransaction() {
        if (FACTORY == null) {
            throw new DaoException("EntityManagerUtil not initialized");
        }
        
        EntityManager em = EntityManagerUtil.getCurrentEntityManager();
        if (em.getTransaction().isActive()) {
            return;
        }
        em.getTransaction().begin();
    }
    
    /**
     * Fuehrt auf der aktuellen Transaktion ein Commit durch
     *
     * @throws DaoException wenn die Klasse nicht initialisiert wurde oder ein Fehler auftritt
     * @see javax.persistence.EntityTransaction#commit()
     */
    public static void commitTransaction() {
        if (FACTORY == null) {
            throw new DaoException("EntityManagerUtil not initialized");
        }
        
        EntityManager em = EntityManagerUtil.getCurrentEntityManager();
        if (em.getTransaction().isActive() == false) {
            return;
        }
        try {
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new DaoException("Commit failed", e);
        }
        
    }

    /**
     * Fuehrt auf der aktuellen Transaktion ein Rollback durch
     *
     * @throws DaoException wenn die Klasse nicht initialisiert wurde oder ein Fehler auftritt
     * @see javax.persistence.EntityTransaction#rollback()
     */
    public static void rollbackTransaction() {
        if (FACTORY == null) {
            throw new DaoException("EntityManagerUtil not initialized");
        }
        
        EntityManager em = EntityManagerUtil.getCurrentEntityManager();
        if (em.getTransaction().isActive() == false) {
            return;
        }
        try {
            em.getTransaction().rollback();
        } catch (RollbackException e) {
            throw new DaoException("Exception during rollback", e);
        }
    }
    
    /**
     * Schliesst den <code>EntityFactoryManager</code>
     * 
     */
    public static void closeFactory() {
        if (FACTORY == null) {
            throw new DaoException("EntityManagerUtil not initialized");
        }
        
        if (FACTORY.isOpen()) {
            try {
                FACTORY.close();
                LOG.info(FACTORY.getClass().getSimpleName() + " was closed");
            } catch (IllegalStateException e) {
                LOG.warn(FACTORY.getClass().getSimpleName() + " already closed");
            }
        }
    }
}
