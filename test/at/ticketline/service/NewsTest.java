package at.ticketline.service;

import static org.junit.Assert.*;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.entity.News;
import at.ticketline.service.api.NewsService;
import at.ticketline.service.impl.NewsServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

/**
 * Testet die Funktionalität der News-Serviceschicht (Alle laden)
 * 
 * @author Rafael Konlechner
 */
public class NewsTest extends AbstractDaoTest {

    private static NewsDao newsDao;
    private static NewsService newsService;

    @BeforeClass
    public static void init() {

        newsDao = (NewsDao) DaoFactory.getByEntity(News.class);
        newsService = new NewsServiceImpl(newsDao);

        /**
         * Erzeuge Test-Daten fuer die Datenbank: Rueckgängig machen mit
         * "DELETE FROM news"
         */

        /*
         * EntityManagerUtil.beginTransaction();
         * 
         * for (int i = 0; i <= 100; i++) {
         * 
         * newsDao.persist(EntityGenerator.getValidNews(i)); }
         * 
         * EntityManagerUtil.commitTransaction();
         */
    }

    @Test(expected = ConstraintViolationException.class)
    public void invalidPersist_shouldReturnEmptyList() {

        newsDao.persist(new News());
        List<News> news = newsService.findAll();
        assertNotNull(news);
        assertTrue(news.size() == 0);
    }

    @Test
    public void findAllNews_shouldReturn20Elements() {

        for (int i = 0; i < 20; i++) {

            newsDao.persist(EntityGenerator.getValidNews(i));
        }

        List<News> news = newsService.findAll();
        assertNotNull(news);
        assertTrue(news.size() == 20);
    }
}