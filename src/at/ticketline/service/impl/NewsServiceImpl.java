package at.ticketline.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.api.NewsDao;
import at.ticketline.entity.News;
import at.ticketline.service.api.NewsService;

/**
 * @author Rafael Konlechner
 */
public class NewsServiceImpl implements NewsService {
    private static final Logger LOG = LoggerFactory
            .getLogger(NewsServiceImpl.class);

    private NewsDao newsDao;

    public NewsServiceImpl(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    /**
     * @return Liste aller gespeicherten News oder eine leere Liste, wenn keine
     *         News gespeichert sind
     */
    @Override
    public List<News> findAll() {

        LOG.info("Loading news");
        List<News> result = newsDao.findAll();

        if (result == null) {

            result = new ArrayList<News>();
        }

        return result;
    }
}