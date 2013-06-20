package at.ticketline.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Person ist eine abstrakte Superklasse fuer Mitarbeiter und Kunden.
 * 
 */
@Entity
@Table(name = "PERSON", uniqueConstraints={
        @UniqueConstraint(columnNames={"username"})
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYP", discriminatorType = DiscriminatorType.CHAR)
public abstract class Person extends BaseEntity {

    private static final Logger LOG = LoggerFactory.getLogger(Person.class);
    private static final long serialVersionUID = 7257677923410786298L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    
    @Column(length = 50)
    @Size(max = 50)
    private String username;

    @Column(length = 64)
    @Size(max = 64)
    private String passwort;

    @Column(nullable = false, length = 50)
    @Size(max = 50)
    @NotNull
    protected String nachname;

    @Column(nullable = false, length = 30)
    @Size(max = 30)
    @NotNull
    protected String vorname;

    @Column(length = 30)
    @Size(max = 30)
    protected String titel;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    protected Geschlecht geschlecht;

    @Temporal(TemporalType.DATE)
    @Past
    protected GregorianCalendar geburtsdatum;

    @Embedded
    @Valid
    protected Adresse adresse;

    @Size(max = 255)
    protected String telnr;

    @Size(max = 255)
    @Email
    protected String email;

    @Pattern(regexp = "^[0-9]{5}$")
    protected String blz;

    @Pattern(regexp = "^[0-9]{3,16}$")
    protected String kontonr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORT_ID")
    protected Ort ort;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = this.hashPassword(passwort);
    }

    /**
     * Nachname
     */
    public String getNachname() {
        return this.nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Vorname
     */
    public String getVorname() {
        return this.vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Titel
     */
    public String getTitel() {
        return this.titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    /**
     * Geschlecht der Person
     */
    public Geschlecht getGeschlecht() {
        return this.geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public GregorianCalendar getGeburtsdatum() {
        return this.geburtsdatum;
    }

    public void setGeburtsdatum(GregorianCalendar geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Adresse getAdresse() {
        return this.adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * Telefonnummer des Kartenbesitzers
     */
    public String getTelnr() {
        return this.telnr;
    }

    public void setTelnr(String telnr) {
        this.telnr = telnr;
    }

    /**
     * Email-Adresse der Person
     */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBlz() {
        return this.blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }

    public String getKontonr() {
        return this.kontonr;
    }

    public void setKontonr(String kontonr) {
        this.kontonr = kontonr;
    }

    public Ort getOrt() {
        return this.ort;
    }

    public void setOrtverk(Ort ort) {
        this.ort = ort;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Person [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (username != null) {
            builder.append("username=");
            builder.append(username);
            builder.append(", ");
        }
        if (passwort != null) {
            builder.append("passwort=");
            builder.append(passwort);
            builder.append(", ");
        }
        if (nachname != null) {
            builder.append("nachname=");
            builder.append(nachname);
            builder.append(", ");
        }
        if (vorname != null) {
            builder.append("vorname=");
            builder.append(vorname);
            builder.append(", ");
        }
        if (titel != null) {
            builder.append("titel=");
            builder.append(titel);
            builder.append(", ");
        }
        if (geschlecht != null) {
            builder.append("geschlecht=");
            builder.append(geschlecht);
            builder.append(", ");
        }
        if (geburtsdatum != null) {
            builder.append("geburtsdatum=");
            builder.append(geburtsdatum);
            builder.append(", ");
        }
        if (adresse != null) {
            builder.append("adresse=");
            builder.append(adresse);
            builder.append(", ");
        }
        if (telnr != null) {
            builder.append("telnr=");
            builder.append(telnr);
            builder.append(", ");
        }
        if (email != null) {
            builder.append("email=");
            builder.append(email);
            builder.append(", ");
        }
        if (blz != null) {
            builder.append("blz=");
            builder.append(blz);
            builder.append(", ");
        }
        if (kontonr != null) {
            builder.append("kontonr=");
            builder.append(kontonr);
            builder.append(", ");
        }
        if (ort != null) {
            builder.append("ort=");
            builder.append(ort);
        }
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * Hilfsfunktion zum hashen der Passwörter
     * 
     * @param password plain text password
     * @return sha-256 hash des passworts
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
        
            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Hash-Algorithmus wird nicht unterstützt");
            return "";
        }
    }
}


