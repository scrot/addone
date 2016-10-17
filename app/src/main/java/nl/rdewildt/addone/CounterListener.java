package nl.rdewildt.addone;

import java.util.List;

/**
 * Created by roy on 7/11/16.
 */
public interface CounterListener {
    void onValueChanged(Integer value);
    void onGoalsChanged(List<Goal> goals);
}
