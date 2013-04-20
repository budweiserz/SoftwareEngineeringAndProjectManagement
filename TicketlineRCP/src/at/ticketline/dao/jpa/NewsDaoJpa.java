package at.ticketline.dao.jpa;

import at.ticketline.dao.GenericDaoJpa;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.entity.News;

public class NewsDaoJpa extends GenericDaoJpa<News,Integer> implements NewsDao {

}