package at.ticketline.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;

/**
 * Kein ernstzunehmender Test
 * 
 * @author Rafael Konlechner
 * 
 */
public class CSVTest {

    @Test
    public void a() throws FileNotFoundException, IOException {

        LabeledCSVParser parser = new LabeledCSVParser(new CSVParser(
                new FileInputStream("csv/kunden.csv")));
        String[] labels = parser.getLabels();

        for (String s : labels) {
            System.out.println(s);
        }
    }

    @Test
    public void b() throws FileNotFoundException, IOException {

        LabeledCSVParser parser = new LabeledCSVParser(new CSVParser(
                new FileInputStream("csv/kunden.csv")));

        String[] labels = parser.getLine();

        for (String s : labels) {
            System.out.println(s);
        }

        String label = parser.getValueByLabel("Vorname");
        System.out.println(label);

        label = parser.getValueByLabel("Vorname");
        System.out.println(label);
        label = parser.getValueByLabel("Nachname");
        System.out.println(label);
    }

    @Test
    public void c() throws FileNotFoundException, IOException {

        LabeledCSVParser parser = new LabeledCSVParser(new CSVParser(
                new FileInputStream("csv/veranstaltungen.csv")));
        String[] labels = parser.getLabels();

        for (String s : labels) {
            System.out.println(s);
        }
    }

    @Test
    public void d() throws FileNotFoundException, IOException {

        LabeledCSVParser parser = new LabeledCSVParser(new CSVParser(
                new FileInputStream("csv/veranstaltungen.csv")));
        
        parser.getLine();
        String label = parser.getValueByLabel("Inhalt");
        while (label != null) {
            
            System.out.println(label);
            label = parser.getValueByLabel("Inhalt");
            parser.getLine();
        }
    }
}