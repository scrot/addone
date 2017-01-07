package nl.rdewildt.addone.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.rdewildt.addone.helper.DateTimeSerializer;

/**
 * Created by roydewildt on 20/12/2016.
 */

public class Stats {
    private File statspath;

    private Counter counter;
    private List<Goal> goals;
    private List<Bonus> bonuses;

    public Stats(File statspath){
        this.statspath = statspath;

        this.counter = new Counter();
        this.goals = new ArrayList<>();
        this.bonuses = new ArrayList<>();

        this.bonuses.add(new Bonus());

        if(!statspath.isFile()) {
            writeStats();
        }
        else {
            syncStats();
        }
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<Bonus> bonuses) {
        this.bonuses = bonuses;
    }

    public void resetStats() {
        counter.reset();

        goals.clear();

        bonuses.clear();
        bonuses.add(new Bonus());

        writeStats();
    }

    public void syncStats() {
        Stats stats = readStats();

        counter = new Counter();
        counter = stats.getCounter();

        goals.clear();
        goals.addAll(stats.getGoals());

        bonuses.clear();
        bonuses.addAll(stats.getBonuses());

    }

    public Stats readStats() {
        Stats out = null;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(statspath))) {
            out = getDateTimeGSON().fromJson(fileReader, Stats.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public void writeStats() {
        String jsonString = getDateTimeGSON().toJson(this);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statspath))) {
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Gson getDateTimeGSON(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer()).create();
    }
}
