package nl.rdewildt.addone;

/**
 * Created by roydewildt on 06/10/2016.
 */

public class Goal {
    private String name;
    private String summary;
    private Integer pointsNeeded;

    public Goal(String name, String summary, Integer pointsNeeded) {
        this.name = name;
        this.summary = summary;
        this.pointsNeeded = pointsNeeded;
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

    public Integer getPointsNeeded() {
        return pointsNeeded;
    }

    public void setPointsNeeded(Integer pointsNeeded) {
        this.pointsNeeded = pointsNeeded;
    }
}
