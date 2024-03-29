package at.ticketline.entity;

import java.util.GregorianCalendar;
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.eclipse.e4.core.di.annotations.Creatable;

/**
 * Ein Kuenstler ist eine Person, die in irgendeiner Weise an einer
 * Veranstaltung mitgewirkt hat. Dabei kann es sich um Schauspieler, Regisseure,
 * Kameramaenner usw. handeln. In der Entitaet Kuenstler werden die
 * persoenlichen Daten gespeichert. Jeder Kuesnstler hat eine eindeutige Nummer.
 * Zusaetzlich werden noch sein Vorname, Nachname, Titel, das Geschlecht, sein
 * Geburtsdatum und eine Biographie gespeichert
 * 
 */
@SuppressWarnings("restriction")
@Creatable
@Entity
public class Kuenstler extends BaseEntity {

    private static final long serialVersionUID = -4137918784573189760L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    @Size(max = 50)
    @NotNull
    private String nachname;

    @Column(nullable = false, length = 50)
    @Size(max = 50)
    @NotNull
    private String vorname;

    @Column(length = 30)
    @Size(max = 30)
    private String titel;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Geschlecht geschlecht;

    @Temporal(TemporalType.DATE)
    @Past
    private GregorianCalendar geburtsdatum;

    @Lob
    private String biographie;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "kuenstler")
    private Set<Engagement> engagements = new HashSet<Engagement>();

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Nachname oder Kuenstlername
     */
    public String getNachname() {
        return this.nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Vorname und/oder Kuenstlername
     */
    public String getVorname() {
        return this.vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * z.B.: Dr., DI
     */
    public String getTitel() {
        return this.titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    /**
     * Geschlecht des Kuenstlers
     */
    public Geschlecht getGeschlecht() {
        return this.geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    /**
     * Geburtsdatum kleiner Heute
     */
    public GregorianCalendar getGeburtsdatum() {
        return this.geburtsdatum;
    }

    public void setGeburtsdatum(GregorianCalendar geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    /**
     * Biographie des Kuenstlers
     */
    public String getBiographie() {
        return this.biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public Set<Engagement> getEngagements() {
        return this.engagements;
    }

    public void setEngagements(Set<Engagement> engagements) {
        this.engagements = engagements;
    }

    public String getName() {
        if (getId() == null) {
            return "Neuer KÃ¼nstler";
        } else {
            return getVorname() + " "
                    + getNachname();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Kuenstler [");
        if (this.id != null) {
            builder.append("id=").append(this.id).append(", ");
        }
        if (this.biographie != null) {
            builder.append("biographie=").append(this.biographie).append(", ");
        }
        if (this.geburtsdatum != null) {
            builder.append("geburtsdatum=").append(this.geburtsdatum).append(
                    ", ");
        }
        if (this.geschlecht != null) {
            builder.append("geschlecht=").append(this.geschlecht).append(", ");
        }
        if (this.nachname != null) {
            builder.append("nachname=").append(this.nachname).append(", ");
        }
        if (this.titel != null) {
            builder.append("titel=").append(this.titel).append(", ");
        }
        if (this.vorname != null) {
            builder.append("vorname=").append(this.vorname);
        }
        builder.append("]");
        return builder.toString();
    }

}
