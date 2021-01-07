package models.goals;

import models.Goal;
import models.sources.ReferralSource;
import models.Webpage;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "goalsreferral",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"goal_id", "referral_id", "date"})}
)
public class GoalsReachesByReferralSource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "referral_id")
    private ReferralSource referral;

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

    public ReferralSource getReferal() {
        return referral;
    }

    public void setReferal(ReferralSource referal) {
        this.referral = referal;
    }

    public Webpage getWebpage() {
        return webpage;
    }

    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }
}
