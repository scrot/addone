package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.model.Bonus;
import nl.rdewildt.addone.model.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public abstract class CounterUpdater {
    private Counter counter;

    public CounterUpdater(){}

    public CounterUpdater(Counter counter){
        this.counter = counter;
    }

    public abstract int cycleDiff();

    public Boolean isNewCycle(){
        return cycleDiff() > 0;
    }

    public void increaseCounter(Bonus bonus){
        if(!counter.getSubValue().equals(bonus.getPoints())){
            counter.setSubValue(counter.getSubValue() + 1);
            if(counter.getSubValue().equals(bonus.getPoints())){
                counter.setValue(counter.getValue() + bonus.getReward());
            }
            counter.setLastUpdated(new DateTime());
        }
    }

    public void decreaseCounter(){
        Integer cycleDiff = cycleDiff();
        if(isNewCycle() && counter.getValue() > 0){
            counter.setValue(counter.getValue() - cycleDiff); //TODO variable decreaseRate?
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

}
