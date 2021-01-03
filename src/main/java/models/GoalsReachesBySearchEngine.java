package models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "goalssearchengine",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"goal_id", "engine_id", "date"})}
        )
public class GoalsReachesBySearchEngine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDate date;
    private double reaches;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private SearchEngine engine;

    @ManyToOne
    @JoinColumn(name = "webpage_id")
    private Webpage webpage;

}
