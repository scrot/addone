package nl.rdewildt.addone;

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

import nl.rdewildt.addone.updater.CounterUpdater;


/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsController {
    private Stats stats;
    private File statspath;

    private CounterUpdater counterUpdater;

    private List<CounterListener> counterChangedListeners;
    private List<GoalsListener> goalsChangedListeners;

    public interface CounterListener {
        void onValueChanged(Integer value);
    }

    public interface GoalsListener {
        void onGoalAdded(Integer position);
        void onGoalRemoved(Integer position);
        void onGoalsChanged();
    }


    public StatsController(File statspath, CounterUpdater counterUpdater){
        this.statspath = statspath;
        setupStats(statspath);

        this.counterUpdater = counterUpdater;

        counterChangedListeners = new ArrayList<>();
        goalsChangedListeners  = new ArrayList<>();
    }

    private void setupStats(File statsPath){
        if(!statsPath.isFile()){
            stats = new Stats();
            saveStats();
        }
        syncStats();
    }

    /*
     *  Counter Actions
     */

    public void increaseCounter() {
        counterUpdater.increaseCounter(getCounter(), getCounter().getIncreaseRate());
        saveStats();
        notifyCounterValueChanged();
    }

    public void decreaseCounter() {
        counterUpdater.decreaseCounter(getCounter(), getCounter().getDecreaseRate());
        saveStats();
        notifyCounterValueChanged();
    }

    public void resetCounter() {
        getCounter().reset();
        saveStats();
        notifyCounterValueChanged();
    }

    public Counter getCounter() {
        return stats.getCounter();
    }


    public void notifyCounterValueChanged() {
        for(CounterListener counterListener : counterChangedListeners){
            counterListener.onValueChanged(getCounter().getValue());
        }
    }

    public void setCounterChangedListeners(CounterListener f){
        counterChangedListeners.add(f);
    }

    /*
     *  Goals Actions
     */

    public void addGoal(Goal goal) {
        getGoals().add(goal);
        int position = getGoals().indexOf(goal);
        notifyGoalAdded(position);
        saveStats();
    }

    public void removeGoal(Goal goal) {
        if(getGoals().contains(goal)){
            int position = getGoals().indexOf(goal);
            getGoals().remove(position);
            notifyGoalRemoved(position);
            saveStats();
        }
    }

    public void resetGoals(){
        getGoals().clear();
        notifyGoalsChanged();

    }

    public List<Goal> getGoals() {
        return stats.getGoals();
    }

    public void notifyGoalAdded(Integer position) {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalAdded(position);
        }
    }

    public void notifyGoalRemoved(Integer position) {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalRemoved(position);
        }
    }

    public void notifyGoalsChanged() {
        for(GoalsListener goalsListener : goalsChangedListeners){
            goalsListener.onGoalsChanged();
        }
    }

    public void setGoalsChangedListeners(GoalsListener f){
        goalsChangedListeners.add(f);
    }

    /*
     *  Cycle Logic
     */

    public void initNewCycle(Boolean lastCycleForgotten) {
        decreaseCounter();

        Counter counter = getCounter();
        if(lastCycleForgotten) {
            increaseCounter();
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
        saveStats();
    }

    public Boolean isNewCycle() {
        return counterUpdater.isNewCycle(getCounter());
    }

    public Boolean noUpdateLastCycle(){
        return counterUpdater.noUpdateLastCycle(getCounter());
    }

    /*
     *  Stats Actions
     */

    private void resetStats(){
        stats = new Stats();
    }

    public void syncStats() {
        stats = readStats(statspath);
    }

    public Stats readStats(File path) {
        Stats out = null;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(path))) {
            out = getDateTimeGSON().fromJson(fileReader, Stats.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public void writeStats(File path, Stats stats) {
        String jsonString = getDateTimeGSON().toJson(stats);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStats(){
        writeStats(statspath, stats);
    }

    /*
    public Boolean isValidCounterFile(File path) {
        Collection<String> counterFileKeys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> counterFileKeySet;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(path))) {
            try (JsonReader jsonReader = new JsonReader(fileReader)) {
                JsonElement counterJsonElement = new JsonParser().parse(jsonReader);
                if(counterJsonElement.isJsonNull()){
                    return false;
                }
                else {
                    counterFileKeySet = counterJsonElement
                            .getAsJsonObject()
                            .entrySet();
                }
            }
        } catch (IOException e) {
            return false;
        }

        for(Map.Entry<String, JsonElement> counterFileKey : counterFileKeySet){
            counterFileKeys.add(counterFileKey.getKey());
        }

        Collection<String> counterKeys = new ArrayList<>();
        List<Field> counterKeysSet = Arrays.asList(Counter.class.getDeclaredFields());

        for(Field field : counterKeysSet){
            counterKeys.add(field.getName());
        }

        return counterKeys.size() == counterFileKeys.size() && counterKeys.containsAll(counterFileKeys);
    }
    */


    private Gson getDateTimeGSON(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer()).create();
    }
}
