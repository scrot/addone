package nl.rdewildt.addone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roydewildt on 20/12/2016.
 */

public class Stats {
    private Counter counter;
    private List<Goal> goals;
    private List<Bonus> bonuses;

    public Stats() {
        this.counter = new Counter();
        this.goals = new ArrayList<>();
        this.bonuses = new ArrayList<>();
    }

    public Stats(Counter counter, List<Goal> goals, List<Bonus> bonuses) {
        this.counter = counter;
        this.goals = goals;
        this.bonuses = bonuses;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<Bonus> bonuses) {
        this.bonuses = bonuses;
    }
}
