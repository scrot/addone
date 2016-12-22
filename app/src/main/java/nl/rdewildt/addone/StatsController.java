package nl.rdewildt.addone;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.rdewildt.addone.model.Counter;
import nl.rdewildt.addone.model.Goal;
import nl.rdewildt.addone.model.Stats;
import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;


/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsController {
    private Stats stats;
    private File statspath;

    private CounterUpdater counterUpdater;

    private List<CounterListener> counterChangedListeners;
    private List<GoalsListener> goalsChangedListeners;

    public interface CounterListener {
        void onValueChanged(Integer value);
    }

    public interface GoalsListener {
        void onGoalAdded(Integer position);
        void onGoalRemoved(Integer position);
        void onGoalsChanged();
    }

    public StatsController(File statspath){
        this(statspath, new WeeklyCounterUpdater());
    }

    public StatsController(File statspath, CounterUpdater counterUpdater){
        this.statspath = statspath;
        stats = new Stats(statspath);

        this.counterUpdater = counterUpdater;

        counterChangedListeners = new ArrayList<>();
        goalsChangedListeners  = new ArrayList<>();
        statsChangedListeners = new ArrayList<>();
    }

    /*
     *  Counter Actions
     */

    public void increaseCounter() {
        counterUpdater.increaseCounter(getCounter(), getCounter().getIncreaseRate());
        stats.writeStats();
        notifyCounterValueChanged();
    }

    public void decreaseCounter() {
        counterUpdater.decreaseCounter(getCounter(), getCounter().getDecreaseRate());
        stats.writeStats();
        notifyCounterValueChanged();
    }

    public Counter getCounter() {
        return stats.getCounter();
    }


    public void notifyCounterValueChanged() {
        for(CounterListener counterListener : counterChangedListeners){
            counterListener.onValueChanged(getCounter().getValue());
        }
    }

    public void setCounterChangedListeners(CounterListener f){
        counterChangedListeners.add(f);
    }

    /*
     *  Goals Actions
     */

    public void addGoal(Goal goal) {
        int position = binaryInsert(goal, getGoals());
        notifyGoalAdded(position);
        stats.writeStats();
    }

    public void removeGoal(Goal goal) {
        if(getGoals().contains(goal)){
            int position = getGoals().indexOf(goal);
            getGoals().remove(position);
            notifyGoalRemoved(position);
            stats.writeStats();
        }
    }

    private <T extends Comparable<T>> int binaryInsert(T x, List<T> xs){
        if(xs.isEmpty()){
            xs.add(x);
            return 0;
        }

        int start = 0;
        int end = xs.size() - 1;

        while(start != end) {
            int middle = (end - start) / 2 + start;
            T y = xs.get(middle);

            if(x.compareTo(y) == 1){
                start = middle + 1;
            }
            else {
                end = middle;
            }
        }

        T y = xs.get(start);
        if(x.compareTo(y) == -1){
            xs.add(start, x);
            return start;
        }
        else {
            xs.add(start + 1, x);
            return start + 1;
        }
    }

    public List<Goal> getGoals() {
        return stats.getGoals();
    }

    public void notifyGoalAdded(Integer position) {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalAdded(position);
        }
    }

    public void notifyGoalRemoved(Integer position) {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalRemoved(position);
        }
    }

    public void notifyGoalsChanged() {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalsChanged();
        }
    }

    public void setGoalsChangedListeners(GoalsListener f){
        goalsChangedListeners.add(f);
    }

    /*
     *  Cycle Logic
     */

    public void initNewCycle(Boolean lastCycleForgotten) {
        decreaseCounter();

        Counter counter = getCounter();
        if(lastCycleForgotten) {
            increaseCounter();
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
        stats.writeStats();
    }

    public Boolean isNewCycle() {
        return counterUpdater.isNewCycle(getCounter());
    }

    public Boolean noUpdateLastCycle(){
        return counterUpdater.noUpdateLastCycle(getCounter());
    }

    /*
     *  Stats Actions
     */
    private List<StatsChangedListener> statsChangedListeners;
    public interface StatsChangedListener{
        void onChanged();
    }

    public void resetStats(){
        stats.resetStats();
        notifyStatsChanged();
    }

    public void reloadStats(){
        stats.syncStats();
        notifyStatsChanged();
    }

    public void addStatsChangedListener(StatsChangedListener f){
        statsChangedListeners.add(f);
    }

    public void notifyStatsChanged(){
        for(StatsChangedListener f : statsChangedListeners){
            f.onChanged();
        }
    }
}
