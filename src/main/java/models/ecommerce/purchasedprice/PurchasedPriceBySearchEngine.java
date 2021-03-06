package models.ecommerce.purchasedprice;

import models.Goal;
import models.Webpage;
import models.sources.SearchEngine;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "pricesearchengine")
public class PurchasedPriceBySearchEngine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private SearchEngine engine;

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

    public SearchEngine getEngine() {
        return engine;
    }

    public void setEngine(SearchEngine engine) {
        this.engine = engine;
    }
}
