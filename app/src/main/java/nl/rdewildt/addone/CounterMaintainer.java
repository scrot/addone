package nl.rdewildt.addone;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;


/**
 * Created by roydewildt on 28/08/16.
 */
public class CounterMaintainer {
    private File counterPath;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public CounterMaintainer(File fromDir){
        this.counterPath = fromDir;

        try {
            if(!Counter.IsValidCounterFile(counterPath)){
                Counter.writeCounter(new Counter(), counterPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.counterUpdater = new WeeklyCounterUpdater();
    }

    public void increaseCounter() throws IOException {
        Counter counter = getCounter();
        counterUpdater.increaseCounter(counter, counter.getIncreaseRate());
        Counter.writeCounter(counter, counterPath);
        triggerCounterListener();
    }

    public void decreaseCounter() throws IOException {
        Counter counter = getCounter();
        counterUpdater.decreaseCounter(counter, counter.getDecreaseRate());
        Counter.writeCounter(counter, counterPath);
        triggerCounterListener();
    }

    public void resetCounter() throws IOException {
        Counter.writeCounter(new Counter(), counterPath);
        triggerCounterListener();
    }

    public Counter getCounter() throws IOException {
        return Counter.readCounter(counterPath);
    }

    public void initNewCycle(Boolean lastCycleForgotten) throws IOException {
        decreaseCounter();

        Counter counter = getCounter();
        if(lastCycleForgotten) {
            increaseCounter();
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
        Counter.writeCounter(counter, counterPath);
    }

    public Boolean isNewCycle() {
        try {
            return counterUpdater.isNewCycle(getCounter());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean noUpdateLastCycle(){
        try {
            return counterUpdater.noUpdateLastCycle(getCounter());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public void triggerCounterListener() throws IOException {
        if(counterListener != null) {
            counterListener.onValueChanged(getCounter().getValue());
        }
    }
}
