package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.model.Bonus;
import nl.rdewildt.addone.model.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public abstract class CounterUpdater {

    public CounterUpdater(){}

    public abstract int cycleDiff(Counter counter);

    public Boolean isNewCycle(Counter counter){
        return cycleDiff(counter) > 0;
    }

    public void increaseCounter(Counter counter, Bonus bonus){
        if(!counter.getSubValue().equals(bonus.getPoints())){
            counter.setSubValue(counter.getSubValue() + 1);
            if(counter.getSubValue().equals(bonus.getPoints())){
                counter.setValue(counter.getValue() + bonus.getReward());
            }
            counter.setLastUpdated(new DateTime());
        }
    }

    public void decreaseCounter(Counter counter){
        Integer cycleDiff = cycleDiff(counter);
        if(isNewCycle(counter) && counter.getValue() > 0){
            counter.setValue(counter.getValue() - cycleDiff); //TODO variable decreaseRate?
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
    }
}
