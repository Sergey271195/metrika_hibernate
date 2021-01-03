package models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"goal_id", "date"})})
public class GoalReachesTrafficSource {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="webpage_id")
    private Webpage webpage;

    @ManyToOne
    @JoinColumn(name="goal_id")
    private Goal goal;

    private LocalDate date;
    private double organic;
    private double direct;
    private double ad;
    private double referral;
    private double internal;
    private double social;
    private double recommend;
    private double total;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Webpage getWebpage() {
        return webpage;
    }

    public void setWebpage(Webpage webpage) {
        this.webpage = webpage;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getOrganic() {
        return organic;
    }

    public void setOrganic(double organic) {
        this.organic = organic;
    }

    public double getDirect() {
        return direct;
    }

    public void setDirect(double direct) {
        this.direct = direct;
    }

    public double getAd() {
        return ad;
    }

    public void setAd(double ad) {
        this.ad = ad;
    }

    public double getReferral() {
        return referral;
    }

    public void setReferral(double referral) {
        this.referral = referral;
    }

    public double getInternal() {
        return internal;
    }

    public void setInternal(double internal) {
        this.internal = internal;
    }

    public double getSocial() {
        return social;
    }

    public void setSocial(double social) {
        this.social = social;
    }

    public double getRecommend() {
        return recommend;
    }

    public void setRecommend(double recommend) {
        this.recommend = recommend;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
