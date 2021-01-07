package models.views;

import models.Goal;
import models.Webpage;
import models.sources.TrafficSource;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "viewstrafficsource",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"webpage_id", "source_id", "date"})}
)
public class ViewsByTrafficSource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private TrafficSource trafficSource;

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

    public TrafficSource getTrafficSource() {
        return trafficSource;
    }

    public void setTrafficSource(TrafficSource trafficSource) {
        this.trafficSource = trafficSource;
    }

    public Webpage getWebpage() {
        return webpage;
    }

    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }
}
