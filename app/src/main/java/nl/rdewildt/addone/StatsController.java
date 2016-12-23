package nl.rdewildt.addone;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import nl.rdewildt.addone.helper.BinarySearch;
import nl.rdewildt.addone.model.Bonus;
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
    private List<BonusesListener> bonusesChangedListeners;

    public interface CounterListener {
        void onValueChanged(Integer value);
        void onSubValueChanged(Integer value);
        void onCounterChanged(Counter counter);
    }

    public interface GoalsListener {
        void onGoalAdded(Integer position);
        void onGoalRemoved(Integer position);
        void onGoalsChanged();
    }

    public interface BonusesListener {
        void onBonusChanged();
    }

    public StatsController(File statspath){
        this(statspath, new WeeklyCounterUpdater());
    }

    public StatsController(File statspath, CounterUpdater counterUpdater){
        this.statspath = statspath;
        stats = new Stats(statspath);

        this.counterUpdater = counterUpdater;
        counterUpdater.setCounter(getCounter());

        counterChangedListeners = new ArrayList<>();
        goalsChangedListeners  = new ArrayList<>();
        bonusesChangedListeners = new ArrayList<>();
    }

    /*
     *  Counter Actions
     */

    public void increaseCounter() {
        counterUpdater.increaseCounter(getNextRelevantBonus(getCounter().getSubValue()));
        stats.writeStats();
        notifyCounterValueChanged();
        notifySubCounterValueChanged();
    }

    public void decreaseCounter() {
        counterUpdater.decreaseCounter();
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

    public void notifySubCounterValueChanged() {
        for(CounterListener counterListener : counterChangedListeners){
            counterListener.onSubValueChanged(getCounter().getValue());
        }
    }

    public void notifyCounterChanged() {
        for(CounterListener counterListener : counterChangedListeners){
            counterListener.onCounterChanged(stats.getCounter());
        }
    }

    public void setCounterChangedListeners(CounterListener f){
        counterChangedListeners.add(f);
    }

    /*
     *  Goals Actions
     */

    public void addGoal(Goal goal) {
        int position = BinarySearch.binaryInsertIndex(goal, getGoals());
        getGoals().add(position, goal);
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
     *  Bonus Actions
     */

    public void addBonus(Bonus bonus){
        if(!getBonuses().contains(bonus)) {
            getBonuses().add(bonus);
            notifyBonusChanged();
            stats.writeStats();
        }
    }

    public void removeBonus(Bonus bonus){
        if(getBonuses().contains(bonus)){
            getBonuses().remove(bonus);
            notifyBonusChanged();
            stats.writeStats();
        }
    }

    public Bonus getNextRelevantBonus(Integer key){
        Iterator<Bonus> it = getBonuses().iterator();

        int points = -1;
        Bonus elem = null;
        while(it.hasNext() && key >= points){
            elem = it.next();
            points = elem.getPoints();
        }
        return elem;
    }

    public void notifyBonusChanged() {
        for(BonusesListener bonusesListener : bonusesChangedListeners){
            bonusesListener.onBonusChanged();
        }
    }

    public void addBonusesChangedListener(BonusesListener f){
        bonusesChangedListeners.add(f);
    }

    public SortedSet<Bonus> getBonuses(){
        return stats.getBonuses();
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
        return counterUpdater.isNewCycle();
    }

    public int cycleDiff(){
        return counterUpdater.cycleDiff();
    }

    /*
     *  Stats Actions
     */

    public void resetStats(){
        stats.resetStats();
        notifyCounterChanged();
        notifyGoalsChanged();
        notifyBonusChanged();
    }

    public void reloadStats(){
        stats.syncStats();
        notifyCounterChanged();
        notifyGoalsChanged();
        notifyBonusChanged();
    }
}
