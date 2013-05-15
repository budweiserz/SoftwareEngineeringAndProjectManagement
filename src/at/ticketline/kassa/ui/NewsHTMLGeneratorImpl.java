package at.ticketline.kassa.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import at.ticketline.entity.News;

public class NewsHTMLGeneratorImpl implements NewsHTMLGenerator {

	@Override
	public String generateHTMLFromNewsItems(List<News> items) {
		
		String page = "<html>";
		//Header hinzufuegen
		page += "<head></head>";
		
		//Body hinzufuegen
		page+= "<body>";
		
		//Tabelle mit News Elementen hinzufuegen
		page+= "<table>";
		
		for (int i = 0; i < items.size(); i++) {
			//News Element hinzufuegen
			page += "<tr>";
			
			page += "<td>";
			
			page += "<div style='background-color:#aa2222; padding:5px'>";
			
			page += "<span'>";
			
			page += "<font color='white' face='Arial'>";
			
			page += items.get(i).getTitel();
			
			page += "</font>";
			
			page += "</span>";
			
			page += "<span style='float:right;'>";
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			
			page += "<font color='white' face='Arial'>";
			
			page += formatter.format( items.get(i).getDatum());
			
			page += "</font>";
			
			page += "</span>";
			
			page += "</div>";
			
			page += "<div style='padding:5px'>";
			
			page += "<font face='Arial'>";
			
			page += items.get(i).getText();
			
			page += "</font>";
			
			page += "</div>";
			
			page += "</td>";
			
			page += "</tr>";
		}
		
		page+= "</table>";
		
		page+= "</body>";		
				
		page += "</html>";
		
		return page;
	}

}
