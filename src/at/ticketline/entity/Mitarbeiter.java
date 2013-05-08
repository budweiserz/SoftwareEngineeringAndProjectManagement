package at.ticketline.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

/**
 * 
 * Im Mitarbeiter werden die Stammdaten der Mitarbeiter gespeichert. Jeder
 * Mitarbeiter ist einem Ort zugeordnet.
 * 
 */
@Entity
@DiscriminatorValue(value = "M")
public class Mitarbeiter extends Person {

    private static final long serialVersionUID = 3949640748689103253L;

    @Enumerated(EnumType.ORDINAL)
    private Berechtigung berechtigung;
    
    @Column(length = 12)
    @Size(max = 12)
    private String sozialversicherungsnr;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mitarbeiter")
    private Set<Transaktion> mtransaktionen = new HashSet<Transaktion>();

    public Berechtigung getBerechtigung() {
        return this.berechtigung;
    }

    public void setBerechtigung(Berechtigung berechtigung) {
        this.berechtigung = berechtigung;
    }

    public String getSozialversicherungsnr() {
        return sozialversicherungsnr;
    }

    public void setSozialversicherungsnr(String sozialversicherungsnr) {
        this.sozialversicherungsnr = sozialversicherungsnr;
    }

    public Set<Transaktion> getTransaktionen() {
        return this.mtransaktionen;
    }

    public void setTransaktionen(Set<Transaktion> transaktionen) {
        this.mtransaktionen = transaktionen;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mitarbeiter [");
        if (this.berechtigung != null) {
            builder.append("berechtigung=").append(this.berechtigung).append(
                    ", ");
        }
        builder.append("]");
        return builder.toString();
    }

}
