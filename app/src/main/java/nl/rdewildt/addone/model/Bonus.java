package nl.rdewildt.addone.model;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by roydewildt on 06/10/2016.
 */

public class Bonus implements Comparable<Bonus> {
    private Integer points;
    private Integer reward;

    public Bonus() {
        this.points = 1;
        this.reward = 1;
    }

    public Bonus(Integer points, Integer reward) {
        this.points = points;
        this.reward = reward;
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
