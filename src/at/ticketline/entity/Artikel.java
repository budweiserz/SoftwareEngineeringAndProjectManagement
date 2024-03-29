package at.ticketline.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Fuer jede Veranstaltung des Systems koennen Werbeartikel definiert werden.
 * Diese Artikel werden dann im Online-Shop und in den Ticketline-Kassen
 * verkauft. Jeder Artikel ist genau einer Kategorie zugeordnet.
 * 
 * @see ArtikelKategorie
 */
@Entity
public class Artikel extends BaseEntity {

    private static final long serialVersionUID = 5239407186115538897L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    @Size(max = 50)
    @NotNull
    private String kurzbezeichnung;

    @Lob
    private String beschreibung;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ArtikelKategorie kategorie;

    @Size(max = 255)
    private String abbildung;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VERANSTALTUNG_ID", nullable = true)
    private Veranstaltung veranstaltung;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "artikel")
    private Set<BestellPosition> bestellPositionen = new HashSet<BestellPosition>();

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Name des Artikels
     */
    public String getKurzbezeichnung() {
        return this.kurzbezeichnung;
    }

    public void setKurzbezeichnung(String kurzbezeichnung) {
        this.kurzbezeichnung = kurzbezeichnung;
    }

    /**
     * Beschreibung des Artikels
     */
    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * Kategorie des Artikels
     */
    public ArtikelKategorie getKategorie() {
        return this.kategorie;
    }

    public void setKategorie(ArtikelKategorie kategorie) {
        this.kategorie = kategorie;
    }

    /**
     * Speicherort eines Bilds des Artikels
     */
    public String getAbbildung() {
        return this.abbildung;
    }

    public void setAbbildung(String abbildung) {
        this.abbildung = abbildung;
    }

    public Veranstaltung getVeranstaltung() {
        return this.veranstaltung;
    }

    public void setVeranstaltung(Veranstaltung veranstaltung) {
        this.veranstaltung = veranstaltung;
    }

    public Set<BestellPosition> getBestellPositionen() {
        return this.bestellPositionen;
    }

    public void setBestellPositionen(Set<BestellPosition> bestellPositionen) {
        this.bestellPositionen = bestellPositionen;
    }

    public BigDecimal getWert() {
        return new BigDecimal(0);
    }
    public String getEinheit() {
        return "";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Artikel [");
        if (this.id != null) {
            builder.append("id=").append(this.id).append(", ");
        }
        if (this.abbildung != null) {
            builder.append("abbildung=").append(this.abbildung).append(", ");
        }
        if (this.beschreibung != null) {
            builder.append("beschreibung=").append(this.beschreibung).append(
                    ", ");
        }
        if (this.kategorie != null) {
            builder.append("kategorie=").append(this.kategorie).append(", ");
        }
        if (this.kurzbezeichnung != null) {
            builder.append("kurzbezeichnung=").append(this.kurzbezeichnung)
                    .append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}
