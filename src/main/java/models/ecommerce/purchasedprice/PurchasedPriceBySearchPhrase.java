package models.ecommerce.purchasedprice;

import models.Webpage;
import models.sources.SearchPhrase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "pricesearchphrase",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"webpage_id", "phrase_id", "date"})}
)
public class PurchasedPriceBySearchPhrase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    private SearchPhrase searchPhrase;

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

    public SearchPhrase getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(SearchPhrase searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public Webpage getWebpage() {
        return webpage;
    }

    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }
}
