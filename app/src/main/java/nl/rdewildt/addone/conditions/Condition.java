package nl.rdewildt.addone.conditions;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 28/08/16.
 */
public abstract class Condition {
    private Stats stats;

    public Condition(Stats stats) {
        this.stats = stats;
    }

    public abstract boolean isTrue();
    public boolean isFalse(){return !this.isTrue();}

    protected Stats getStats() {
        return stats;
    }
}
