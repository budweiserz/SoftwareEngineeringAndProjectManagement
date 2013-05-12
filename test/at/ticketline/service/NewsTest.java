package at.ticketline.service;

import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.entity.News;
import at.ticketline.service.api.NewsService;
import at.ticketline.service.impl.NewsServiceImpl;
import at.ticketline.test.AbstractDaoTest;

/**
 * Testet die Funktionalität der News-Serviceschicht (Alle laden)
 *
 * @author Rafael Konlechner
 */
public class NewsTest extends AbstractDaoTest {

    private static NewsDao newsDao;
    private static NewsService newsService;
    private static ArrayList<String> violations;
    private List<News> news;

    @BeforeClass
    public static void init() {

        newsDao = (NewsDao) DaoFactory.getByEntity(News.class);
        newsService = new NewsServiceImpl(newsDao);
        violations = new ArrayList<String>();
    }

    @Before
    public void setup() {

        violations.clear();
        news = null;
    }

    @Test
    public void persistNewValidNewsn_shouldWork() {

        news = newsService.findAll();
        assertNotNull(news);
    }
}