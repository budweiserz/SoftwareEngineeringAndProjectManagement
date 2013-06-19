package at.ticketline.entity;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

/**
 * Ein Merchandise-Artikel ist ein Produkt, das von einem Kunden gegen einen
 * bestimmten Preis gekauft werden kann.
 */

@Entity
@DiscriminatorValue(value = "M")
public class Merchandise extends Artikel {

    private static final long serialVersionUID = -6957934217139879450L;

    @Min(value = 0)
    @Digits(integer = 8, fraction = 2)
    private BigDecimal preis;

    public BigDecimal getPreis() {
        return this.preis;
    }

    /**
     * Preis des Artikels
     * 
     * @param preis
     *            >= 0
     */
    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    @Override
    public BigDecimal getWert() {
        return preis;
    }

    @Override
    public String getEinheit() {
        return "€";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Merchandise [preis=").append(this.preis).append("]");
        return builder.toString();
    }
}