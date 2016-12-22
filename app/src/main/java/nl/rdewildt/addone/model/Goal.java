package nl.rdewildt.addone.model;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by roydewildt on 06/10/2016.
 */

public class Goal implements Comparable {
    private String name;
    private String summary;
    private Integer requiredPoints;

    public Goal(){
        this.name = "";
        this.summary = "";
        this.requiredPoints = 0;
    }

    public Goal(String name, String summary, Integer requiredPoints) {
        this.name = name;
        this.summary = summary;
        this.requiredPoints = requiredPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(o instanceof Goal){
            Goal y = (Goal) o;
            if(this.getRequiredPoints() < y.getRequiredPoints()) {
                return -1;
            }
            else if (Objects.equals(this.getRequiredPoints(), y.getRequiredPoints())){
                return 0;
            }
            else if (this.getRequiredPoints() > y.getRequiredPoints()){
                return 1;
            }
        }
        return -1;
    }
}
