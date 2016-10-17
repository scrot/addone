package nl.rdewildt.addone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by roydewildt on 17/10/2016.
 */

public class GoalsAdapter extends ArrayAdapter<Goal> {
    public GoalsAdapter(Context context, List<Goal> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Goal goal = getItem(position);
        if(goal == null){
            goal = new Goal();
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.goal_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.goaltitle);
        title.setText(goal.getName());

        TextView summary = (TextView) convertView.findViewById(R.id.goalsummary);
        summary.setText(goal.getSummary());

        TextView points = (TextView) convertView.findViewById(R.id.goalpoints);
        points.setText(Integer.toString(goal.getRequiredPoints()));

        return convertView;

    }
}
