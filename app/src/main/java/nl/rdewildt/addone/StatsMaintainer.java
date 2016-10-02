package nl.rdewildt.addone;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Path;

import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

import static nl.rdewildt.addone.StatsIO.readStats;
import static nl.rdewildt.addone.StatsIO.writeStats;

/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsMaintainer {
    private File statsFile;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public StatsMaintainer(File statsFile){
        this.statsFile = statsFile;

        if(readStats(statsFile) == null){
            writeStats(new Stats(), statsFile);
        }

        this.counterUpdater = new WeeklyCounterUpdater();
    }

    public void increaseCounter(Integer i){
        counterUpdater.increaseCounter(getStats(), i);
        writeStats(getStats(), statsFile);
        triggerCounterListener();
    }

    public void decreaseCounter(){
        counterUpdater.decreaseCounter(getStats(), 1);
        writeStats(getStats(), statsFile);
        triggerCounterListener();
    }

    public Stats getStats() {
        return StatsIO.readStats(statsFile);
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public void triggerCounterListener(){
        if(counterListener != null) {
            counterListener.onChanged(getStats().getCounter());
        }
    }
}
