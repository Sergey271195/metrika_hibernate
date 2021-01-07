package models.ecommerce.purchasedprice;

import models.Webpage;
import models.sources.AdvEngine;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "priceadvengine",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"webpage_id", "adv_id", "date"})}
)
public class PurchasedPriceByAdvEngine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "adv_id")
    private AdvEngine advEngine;

    @ManyToOne
    @JoinColumn(name = "webpage_id")
    private Webpage webpage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getReaches() {
        return reaches;
    }

    public void setReaches(double reaches) {
        this.reaches = reaches;
    }

    public AdvEngine getAdvEngine() {
        return advEngine;
    }

    public void setAdvEngine(AdvEngine advEngine) {
        this.advEngine = advEngine;
    }

    public Webpage getWebpage() {
        return webpage;
    }

    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }
}
