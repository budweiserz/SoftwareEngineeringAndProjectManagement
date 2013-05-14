package at.ticketline.dao.jpa;

import java.util.Collections;
import java.util.List;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.entity.News;

public class NewsDaoJpa extends GenericDaoJpa<News,Integer> implements NewsDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<News> findAll() {
		List<News> news = (List<News>) this.entityManager
				.createQuery("SELECT e FROM " + this.entityClass.getSimpleName() + " e ORDER BY e.datum").getResultList();
		Collections.reverse(news);
		return news;
	}


}