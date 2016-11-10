package nl.rdewildt.addone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.rdewildt.addone.updater.CounterUpdater;


/**
 * Created by roydewildt on 28/08/16.
 */
public class CounterMaintainer {
    private Counter counter;
    private File counterPath;
    private CounterUpdater counterUpdater;

    private List<CounterListener> counterChangedListeners;

    public interface CounterListener {
        void onValueChanged(Integer value);
        void onGoalsChanged(List<Goal> goals);
    }


    public CounterMaintainer(File counterPath, CounterUpdater counterUpdater){
        this.counterPath = counterPath;

        if(!isValidCounterFile(this.counterPath)){
            counter = new Counter();
            writeCounter();
        }
        syncCounter();

        this.counterUpdater = counterUpdater;
        counterChangedListeners = new ArrayList<>();
    }

    public void increaseCounter() {
        counterUpdater.increaseCounter(counter, counter.getIncreaseRate());
        writeCounter();
        triggerValueChangedListeners();
    }

    public void decreaseCounter() {
        counterUpdater.decreaseCounter(counter, counter.getDecreaseRate());
        writeCounter();
        triggerValueChangedListeners();
    }

    public void resetCounter() {
        counter.reset();
        writeCounter();
        triggerValueChangedListeners();
        triggerGoalsChangedListeners();
    }

    public Counter getCounter() {
        return counter;
    }

    public void addGoal(Goal goal) {
        counter.getGoals().add(goal);
        writeCounter();
        triggerGoalsChangedListeners();
    }

    public void removeGoal(Goal goal) {
        List<Goal> goals = counter.getGoals();
        if(goals.contains(goal)){
            goals.remove(goal);
        }
        writeCounter();
        triggerGoalsChangedListeners();
    }

    public List<Goal> getGoals() {
        return getCounter().getGoals();
    }

    public void initNewCycle(Boolean lastCycleForgotten) {
        decreaseCounter();

        Counter counter = getCounter();
        if(lastCycleForgotten) {
            increaseCounter();
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
        writeCounter();
    }

    public Boolean isNewCycle() {
        return counterUpdater.isNewCycle(counter);
    }

    public Boolean noUpdateLastCycle(){
        return counterUpdater.noUpdateLastCycle(counter);
    }

    public void setCounterChangedListeners(CounterListener f){
        counterChangedListeners.add(f);
    }

    public void triggerValueChangedListeners() {
        for(CounterListener counterListener : counterChangedListeners){
            counterListener.onValueChanged(counter.getValue());
        }
    }

    public void triggerGoalsChangedListeners() {
        for(CounterListener counterListener : counterChangedListeners){
            counterListener.onGoalsChanged(counter.getGoals());
        }
    }

    public void syncCounter() {
        counter = readCounter();
    }

    public Counter readCounter() {
        File file = counterPath;
        Counter counter;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            counter = getCounterGson().fromJson(fileReader, Counter.class);
        } catch (IOException e) {
            e.printStackTrace();
            counter = null;
        }
        return counter;
    }

    public void writeCounter() {
        File file = counterPath;
        String counterJson = getCounterGson().toJson(counter);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(counterJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        syncCounter();
    }

    public Boolean isValidCounterFile(File path) {
        File file = counterPath;
        Collection<String> counterFileKeys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> counterFileKeySet;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
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

    private Gson getCounterGson(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer()).create();
    }
}
