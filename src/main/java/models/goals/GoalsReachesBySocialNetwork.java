package models.goals;

import models.Goal;
import models.sources.SocialNetwork;
import models.Webpage;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "goalssocialnetwork",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"goal_id", "network_id", "date"})}
)
public class GoalsReachesBySocialNetwork {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "network_id")
    private SocialNetwork network;

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

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public SocialNetwork getNetwork() {
        return network;
    }

    public void setNetwork(SocialNetwork network) {
        this.network = network;
    }

    public Webpage getWebpage() {
        return webpage;
    }

    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }
}
