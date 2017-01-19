package nl.rdewildt.addone;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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

    private Stack<Goal> relevantGoals;
    private Stack<Bonus> releventBonuses;

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
        void onBonusAdded(Integer position);
        void onBonusRemoved(Integer position);
        void onBonusChanged();
    }

    public StatsController(File statspath){
        this(statspath, new WeeklyCounterUpdater());
    }

    public StatsController(File statspath, CounterUpdater counterUpdater){
        this.statspath = statspath;
        this.stats = new Stats(statspath);

        this.counterUpdater = counterUpdater;

        this.counterChangedListeners = new ArrayList<>();
        this.goalsChangedListeners  = new ArrayList<>();
        this.bonusesChangedListeners = new ArrayList<>();

        this.relevantGoals = buildRelevantGoals();
        addGoalsChangedListeners(new GoalsListener() {
            @Override
            public void onGoalAdded(Integer position) {
                buildRelevantBonuses();
            }

            @Override
            public void onGoalRemoved(Integer position) {
                buildRelevantBonuses();
            }

            @Override
            public void onGoalsChanged() {
                buildRelevantBonuses();
            }
        });

        this.releventBonuses = buildRelevantBonuses();
        addBonusesChangedListener(new BonusesListener() {
            @Override
            public void onBonusAdded(Integer position) {

            }

            @Override
            public void onBonusRemoved(Integer position) {

            }

            @Override
            public void onBonusChanged() {
                releventBonuses = buildRelevantBonuses();
            }
        });
    }

    /*
     *  Counter Actions
     */

    public void increaseCounter() {
        counterUpdater.increaseCounter(getCounter(), getRelevantBonus());
        syncRelevantBonuses();
        writeStats();
        notifyCounterChanged();
    }

    public void decreaseCounter() {
        counterUpdater.decreaseCounter(getCounter());
        writeStats();
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
            counterListener.onSubValueChanged(getCounter().getSubValue());
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
        writeStats();
    }

    public void removeGoal(Goal goal) {
        if(getGoals().contains(goal)){
            int position = getGoals().indexOf(goal);
            getGoals().remove(position);
            notifyGoalRemoved(position);
            writeStats();
        }
    }

    public List<Goal> getGoals() {
        return stats.getGoals();
    }

    public void notifyGoalAdded(Integer position) {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalAdded(position);
            goalsListener.onGoalsChanged();
        }
    }

    public void notifyGoalRemoved(Integer position) {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalRemoved(position);
            goalsListener.onGoalsChanged();
        }
    }

    public void notifyGoalsChanged() {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalsChanged();
        }
    }

    public void addGoalsChangedListeners(GoalsListener f){
        goalsChangedListeners.add(f);
    }

    private Stack<Goal> buildRelevantGoals(){
        Stack<Goal> stack = new Stack<>();
        List<Goal> goals = getGoals();
        Collections.sort(goals, (x, y) -> y.compareTo(x));
        for(Goal goal : goals){
            if(goal.getRequiredPoints() > getCounter().getValue()){
                stack.push(goal);
            }
        }
        return stack;
    }

    private void syncRelevantGoals(){
        if(!buildRelevantGoals().empty() && getCounter().getValue() > buildRelevantGoals().peek().getRequiredPoints()){
            buildRelevantGoals().pop();
        }
    }

    /*
     *  Bonus Actions
     */

    public void addBonus(Bonus bonus){
        if(!getBonuses().contains(bonus)) {
            getBonuses().add(bonus);
            notifyBonusesChanged();
            writeStats();
        }
    }

    public void removeBonus(Bonus bonus){
        if(getBonuses().contains(bonus)){
            getBonuses().remove(bonus);
            notifyBonusesChanged();
            writeStats();
        }
    }

    public void setBonuses(List<Bonus> bonuses){
        this.stats.setBonuses(bonuses);
        notifyBonusesChanged();
        writeStats();
    }


    public void notifyBonusAdded(Integer position) {
        for(BonusesListener bonusesListener : bonusesChangedListeners) {
            bonusesListener.onBonusAdded(position);
            bonusesListener.onBonusChanged();
        }
    }

    public void notifyBonusRemoved(Integer position) {
        for(BonusesListener bonusesListener : bonusesChangedListeners){
            bonusesListener.onBonusRemoved(position);
            bonusesListener.onBonusChanged();
        }
    }

    public void notifyBonusesChanged() {
        for(BonusesListener bonusesListener : bonusesChangedListeners){
            bonusesListener.onBonusChanged();
        }
    }

    public void addBonusesChangedListener(BonusesListener f){
        bonusesChangedListeners.add(f);
    }

    public List<Bonus> getBonuses(){
        return stats.getBonuses();
    }

    public Bonus getRelevantBonus(){
        return releventBonuses.empty() ? null : releventBonuses.peek();
    }

    private Stack<Bonus> buildRelevantBonuses(){
        Stack<Bonus> stack = new Stack<>();
        List<Bonus> bonuses = getBonuses();
        Collections.sort(bonuses, (x, y) -> y.compareTo(x));
        for(Bonus bonus : bonuses){
            if(stack.empty() || bonus.getPoints() > getCounter().getSubValue()){
                stack.push(bonus);
            }
        }
        return stack;
    }

    private void syncRelevantBonuses(){
        if(releventBonuses.size() > 1) {
            int sub = getCounter().getSubValue();
            Bonus bonus = getRelevantBonus();
            if (sub >= bonus.getPoints()) {
                releventBonuses.pop();
            }
        }
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
        writeStats();
    }

    public Boolean isNewCycle() {
        return counterUpdater.isNewCycle(getCounter());
    }

    public int cycleDiff(){
        return counterUpdater.cycleDiff(getCounter());
    }

    /*
     *  Stats Actions
     */

    public void resetStats(){
        stats.resetStats();
        notifyCounterChanged();
        notifyGoalsChanged();
        notifyBonusesChanged();
    }

    public void reloadStats(){
        stats.syncStats();
        notifyCounterChanged();
        notifyGoalsChanged();
        notifyBonusesChanged();
    }

    public void writeStats(){
        stats.writeStats();
    }
}
