package nl.rdewildt.addone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by roydewildt on 17/10/2016.
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {
    private List<Goal> goals;

    public GoalsAdapter(List<Goal> goals) {
        this.goals = goals;
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView summary;
        private TextView goal;

        public GoalViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.goaltitle);
            this.summary = (TextView) v.findViewById(R.id.goalsummary);
            this.goal = (TextView) v.findViewById(R.id.goalpoints);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getSummary() {
            return summary;
        }

        public TextView getGoal() {
            return goal;
        }
    }


    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.getTitle().setText(goal.getName());
        holder.getSummary().setText(goal.getSummary());
        holder.getGoal().setText(Integer.toString(goal.getRequiredPoints()));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }
}
