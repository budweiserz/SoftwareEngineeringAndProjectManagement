package at.ticketline.test;

import java.math.BigDecimal;

import at.ticketline.entity.Kategorie;

public class Kategorien {
    
    public static Kategorie createKategorieLoge() {

        Kategorie k = new Kategorie();
        k.setBezeichnung("Poebel");
        k.setPreismin(new BigDecimal(0));
        k.setPreismax(new BigDecimal(20));
        return k;
    }

    public static Kategorie createKategorieGallerie() {

        Kategorie k = new Kategorie();
        k.setBezeichnung("Buergertum");
        k.setPreismin(new BigDecimal(10));
        k.setPreismax(new BigDecimal(100));
        return k;
    }

    public static Kategorie createKategorieKlerus() {

        Kategorie k = new Kategorie();
        k.setBezeichnung("Klerus");
        k.setPreismin(new BigDecimal(50));
        k.setPreismax(new BigDecimal(200));
        return k;
    }

    public static Kategorie createKategorieBuergertum() {

        Kategorie k = new Kategorie();
        k.setBezeichnung("Gallerie");
        k.setPreismin(new BigDecimal(10));
        k.setPreismax(new BigDecimal(200));
        return k;
    }

    public static Kategorie createKategoriePoebel() {

        Kategorie k = new Kategorie();
        k.setBezeichnung("Loge");
        k.setPreismin(new BigDecimal(100));
        k.setPreismax(new BigDecimal(6000));
        return k;
    }
}