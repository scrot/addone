package nl.rdewildt.addone.conditions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 28/08/16.
 */
public class OncePerWeek extends Condition {
    public OncePerWeek(Stats stats) {
        super(stats);
    }

    @Override
    public boolean isTrue() {
        Integer thisWeek = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
        Integer lastWeek = this.getStats().getLastUpdated().get(Calendar.WEEK_OF_YEAR);
        return thisWeek > lastWeek;
    }
}
