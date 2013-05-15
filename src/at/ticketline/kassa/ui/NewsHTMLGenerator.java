package at.ticketline.kassa.ui;

import java.util.List;

import at.ticketline.entity.News;

public interface NewsHTMLGenerator {
	
	public String generateHTMLFromNewsItems(List<News> items);

}
