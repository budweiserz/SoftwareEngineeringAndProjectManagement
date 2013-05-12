package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.News;

/**
 * @author Rafael Konlechner
 */
public interface NewsService {

    /**
     * Laedt alle gespeicherten Unternehmensnachrichten in eine Liste
     */
    public List<News> findAll();
}