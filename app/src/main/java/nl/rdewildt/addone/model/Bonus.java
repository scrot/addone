package nl.rdewildt.addone.model;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by roydewildt on 06/10/2016.
 */

public class Bonus implements Comparable<Bonus> {
    private String name;
    private Integer points;
    private Integer reward;

    public Bonus() {
        this.name = "Default";
        this.points = 1;
        this.reward = 1;
    }

    public Bonus(String name, Integer points, Integer reward) {
        this.name = name;
        this.points = points;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    @Override
    public int hashCode() {
        int result = 101;
        result = result * 31 + getName().hashCode();
        result = result * 31 + getPoints().hashCode();
        result = result * 31 + getReward().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Bonus){
            Bonus y = (Bonus) obj;
            return y.getName().equals(this.getName()) && y.getPoints().equals(this.getPoints()) && y.getReward().equals(this.getReward());
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Bonus y) {
        if(this.getPoints() < y.getPoints()){
            return -1;
        }
        else if(Objects.equals(this.getPoints(), y.getPoints())){
            return 0;
        }
        else{
            return 1;
        }
    }
}
