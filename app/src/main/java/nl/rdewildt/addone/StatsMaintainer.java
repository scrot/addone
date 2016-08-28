package nl.rdewildt.addone;

import java.util.ArrayList;
import java.util.Collection;

import nl.rdewildt.addone.conditions.Condition;

/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsMaintainer {
    private Stats stats;
    private StatsListener statsListener;

    private Collection<Condition> increaseCounterConditions;
    private Collection<Condition> decreaseCounterConditions;

    public StatsMaintainer() {
        this.stats = new Stats();
        this.statsListener = () -> {};
        this.increaseCounterConditions = new ArrayList<>();
        this.decreaseCounterConditions = new ArrayList<>();
    }

    public void resetStats(){
        this.stats = new Stats();
    }

    public void increaseCounter(Integer i){
        if(increaseCounterConditions.isEmpty() ||
                !increaseCounterConditions.stream().anyMatch(Condition::isFalse)){
            stats.setCounter(stats.getCounter() + i);
        }
    }

    public void decreaseCounter(Integer i){
        if(decreaseCounterConditions.isEmpty() ||
                !decreaseCounterConditions.stream().anyMatch(Condition::isFalse)){
            stats.setCounter(stats.getCounter() - i);
        }
    }

    public Stats getStats() {
        return stats;
    }

    public StatsListener getStatsListener() {
        return statsListener;
    }

    public void setStatsListener(StatsListener statsListener) {
        this.statsListener = statsListener;
    }

    public void addIncreaseCounterCondition(Condition condition){
        this.increaseCounterConditions.add(condition);
    }

    public void addDecreaseCounterCondition(Condition condition){
        this.decreaseCounterConditions.add(condition);
    }
}
