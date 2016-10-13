package nl.rdewildt.addone.updater;

import nl.rdewildt.addone.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public interface CounterUpdater {
    void increaseCounter(Counter counter, Integer i);
    void decreaseCounter(Counter counter, Integer i);
    Boolean isNewCycle(Counter counter);
}
