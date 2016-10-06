package nl.rdewildt.addone;

import java.io.File;
import java.io.IOException;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;


/**
 * Created by roydewildt on 28/08/16.
 */
public class CounterMaintainer {
    private File counterFile;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public CounterMaintainer(File counterFile){
        this.counterFile = counterFile;

        try {
            if(!Counter.IsValidCounterFile(counterFile)){
                Counter.writeCounter(new Counter(), counterFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.counterUpdater = new WeeklyCounterUpdater();
    }

    public void increaseCounter() throws IOException {
        Counter counter = getCounter();
        counterUpdater.increaseCounter(counter, counter.getIncreaseRate());
        Counter.writeCounter(counter, counterFile);
        triggerCounterListener();
    }

    public void decreaseCounter() throws IOException {
        Counter counter = getCounter();
        counterUpdater.decreaseCounter(counter, counter.getDecreaseRate());
        Counter.writeCounter(counter, counterFile);
        triggerCounterListener();
    }

    public void resetCounter() throws IOException {
        Counter.writeCounter(new Counter(), counterFile);
        triggerCounterListener();
    }

    public Counter getCounter() throws IOException {
        return Counter.readCounter(counterFile);
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public void triggerCounterListener() throws IOException {
        if(counterListener != null) {
            counterListener.onChanged(getCounter().getCounter());
        }
    }
}
