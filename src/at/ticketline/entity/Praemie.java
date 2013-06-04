package at.ticketline.entity;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

/**
 * 
 * Im Kunden werden alle Daten eines Kunden abgelegt. Jeder Kunde ist einem Ort
 * zugeordnet. Ein Kunde kann optional eine Ticketcard besitzen. Er besitzt
 * darueber hinaus ueber einen Web Account, wenn der Username und das Passwort
 * gesetzt sind.
 */

@Entity
@DiscriminatorValue(value = "P")
public class Praemie extends Artikel {

    private static final long serialVersionUID = 2544553240223975569L;

    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private BigDecimal punkte;

    public BigDecimal getPunkte() {
        return this.punkte;
    }

    public void setPunkte(BigDecimal punkte) {
        this.punkte = punkte;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Prämie [punkte=").append(this.punkte).append("]");
        return builder.toString();
    }
}