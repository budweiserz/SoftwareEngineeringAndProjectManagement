package at.ticketline.log;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlfEclipseLinkSessionLog extends AbstractSessionLog implements SessionLog {

    private static final String[] NAMESPACES = new String[] {
        "default", // trace
        SessionLog.SQL, // trace, debug
        SessionLog.TRANSACTION, // debug
        SessionLog.EVENT,
        SessionLog.CONNECTION, // trace, debug, info
        SessionLog.QUERY, // trace
        SessionLog.CACHE,
        SessionLog.PROPAGATION,
        SessionLog.SEQUENCING, // trace
        SessionLog.EJB,
        SessionLog.DMS,
        SessionLog.METAMODEL, // debug, info
        SessionLog.WEAVER,
        SessionLog.PROPERTIES, // trace
        SessionLog.SERVER
    };
    private static final Map<String, Logger> LOGGER = new HashMap<String, Logger>();

    static {
        for (String ns : NAMESPACES) {
            LOGGER.put(ns, LoggerFactory.getLogger("org.eclipse.eclipselink." + ns));
        } 
    }

    @Override
    public void log(SessionLogEntry entry) {
        if (entry.getLevel() == SessionLog.OFF) {
            return;
        }
        if (StringUtils.isBlank(entry.getMessage())) {
            return;
        }
        
        Logger logger = this.getLogger(entry.getNameSpace());
        
        if ((entry.getLevel() == SessionLog.FINEST) && (logger.isTraceEnabled())) {
            logger.trace(formatMessage(entry));
        } else if (((entry.getLevel() == SessionLog.FINER) || (entry.getLevel() == SessionLog.FINE)) && (logger.isDebugEnabled())) {
            logger.debug(formatMessage(entry));
        } else if (((entry.getLevel() == SessionLog.CONFIG) || (entry.getLevel() == SessionLog.INFO)) && (logger.isInfoEnabled())) {
            logger.info(formatMessage(entry));
        } else if (entry.getLevel() == SessionLog.WARNING) {
            logger.warn(formatMessage(entry));
        } else if (entry.getLevel() == SessionLog.SEVERE) {
            logger.error(formatMessage(entry));
        }

    }
    
    private Logger getLogger(String namespace) {
        if (LOGGER.containsKey(namespace)) {
            return LOGGER.get(namespace);
        } else {
            return LOGGER.get("default");
        }
    }

}
