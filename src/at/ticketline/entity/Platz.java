package at.ticketline.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Ein Platz ist ein reservierter oder gebuchter Platz in einer bestimmten Reihe
 * in einer bestimmten Auffuehrung. Plaetze werden nur dann erstellt, wenn sie
 * im Fall einer Reservierung oder Buchung auch benoetigt werden. Ein Platz kann
 * ueber den Saal, die Reihe und die Nummer genau bestimmt werden. Es darf keine
 * hoehere Nummer geben, als eine Reihe Plaetze hat. Wird eine Reservierung oder
 * Buchung storniert, so wird der Platz wieder freigegeben. Die Kategorie wird
 * zusaetzlich gespeichert, falls sich im nachhinein die Zuordnung der Kategorie
 * zu den Reihen aendert. Der Preis eines Platzes sollte sich daher immer aus
 * der im Platz gespeicherten Kategorie berechnet werden.
 * 
 */
@Entity
public class Platz extends BaseEntity {

    private static final long serialVersionUID = 8231117682597707232L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull
    private Integer nummer;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private PlatzStatus status = PlatzStatus.FREI;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUFFUEHRUNG_ID")
    private Auffuehrung auffuehrung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REIHE_ID")
    private Reihe reihe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSAKTION_ID")
    private Transaktion transaktion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KATEGORIE_ID")
    private Kategorie kategorie;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNummer() {
        return this.nummer;
    }

    public void setNummer(Integer nummer) {
        this.nummer = nummer;
    }

    public PlatzStatus getStatus() {
        return this.status;
    }

    public void setStatus(PlatzStatus status) {
        this.status = status;
    }

    public Auffuehrung getAuffuehrung() {
        return this.auffuehrung;
    }

    public void setAuffuehrung(Auffuehrung auffuehrung) {
        this.auffuehrung = auffuehrung;
    }

    public Reihe getReihe() {
        return this.reihe;
    }

    public void setReihe(Reihe reihe) {
        this.reihe = reihe;
    }

    public Transaktion getTransaktion() {
        return this.transaktion;
    }

    public void setTransaktion(Transaktion transaktion) {
        this.transaktion = transaktion;
    }

    public Kategorie getKategorie() {
        return this.kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Platz [");
        if (this.id != null) {
            builder.append("id=").append(this.id).append(", ");
        }
        if (this.nummer != null) {
            builder.append("nummer=").append(this.nummer).append(", ");
        }
        if (this.status != null) {
            builder.append("status=").append(this.status);
        }
        builder.append("]");
        return builder.toString();
    }
}
