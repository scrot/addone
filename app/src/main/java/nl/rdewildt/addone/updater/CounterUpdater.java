package nl.rdewildt.addone.updater;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 29/08/16.
 */
public interface CounterUpdater {
    void increaseCounter(Stats stats, Integer i);
    void decreaseCounter(Stats stats, Integer i);
}
