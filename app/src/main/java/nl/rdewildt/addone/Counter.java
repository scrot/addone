package nl.rdewildt.addone;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roy on 7/11/16.
 */
public class Counter {
    private Integer value;
    private DateTime lastUpdated;

    private Integer increaseRate;
    private Integer decreaseRate;

    private List<Bonus> bonuses;
    private List<Goal> goals;

    public Counter() {
        this.value = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);

        this.increaseRate = 1;
        this.decreaseRate = 1;

        this.bonuses = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public Counter(Integer value, DateTime lastUpdated, Integer increaseRate, Integer decreaseRate) {
        this.value = value;
        this.lastUpdated = lastUpdated;

        this.increaseRate = increaseRate;
        this.decreaseRate = decreaseRate;

        this.bonuses = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public void reset(){
        this.value = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);
        this.bonuses.clear();
        this.goals.clear();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getIncreaseRate() {
        return increaseRate;
    }

    public void setIncreaseRate(Integer increaseRate) {
        this.increaseRate = increaseRate;
    }

    public Integer getDecreaseRate() {
        return decreaseRate;
    }

    public void setDecreaseRate(Integer decreaseRate) {
        this.decreaseRate = decreaseRate;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<Bonus> bonuses) {
        this.bonuses = bonuses;
    }

    public void addBonus(Bonus bonus){
        this.bonuses.add(bonus);
    }

    public void removeBonus(Bonus bonus){
        if(this.bonuses.contains(bonus)) {
            this.bonuses.remove(bonus);
        }
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Counter){
            Counter counter = (Counter) obj;
            return this.value.equals(counter.getValue())
                    && lastUpdated.toString().equals(counter.getLastUpdated().toString());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 101;
        result = result * 31 + value.hashCode();
        result = result * 31 + getLastUpdated().hashCode();
        return result;
    }
}
