package nl.rdewildt.addone.updater;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 29/08/16.
 */
public interface CounterUpdater {
    Stats increaseCounter(Stats stats, Integer i);
    Stats decreaseCounter(Stats stats);
}
