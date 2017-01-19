package nl.rdewildt.addone;

import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.rdewildt.addone.model.Goal;

/**
 * Created by roydewildt on 17/10/2016.
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {
    private List<Goal> goals;

    private MultiSelector multiSelector;

    private List<GoalViewOnClickListener> goalViewOnClickListeners;
    public interface GoalViewOnClickListener {
        void onClick(GoalViewHolder goalViewHolder);
        void onLongClick(GoalViewHolder goalViewHolder);
    }

    public GoalsAdapter(List<Goal> goals) {
        this.goals = goals;
        this.multiSelector =  new MultiSelector();
        this.goalViewOnClickListeners = new ArrayList<>();
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public MultiSelector getMultiSelector() {
        return multiSelector;
    }


    public void addGoalViewOnClickListener(GoalViewOnClickListener f){
        goalViewOnClickListeners.add(f);
    }

    public void notifyGoalViewClicked(GoalViewHolder goalViewHolder){
        for(GoalViewOnClickListener f : goalViewOnClickListeners){
            f.onClick(goalViewHolder);
        }
    }

    public void notifyGoalViewLongClicked(GoalViewHolder goalViewHolder){
        for(GoalViewOnClickListener f : goalViewOnClickListeners){
            f.onLongClick(goalViewHolder);
        }
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        GoalViewHolder goalViewHolder =  new GoalViewHolder(view, multiSelector);
        goalViewHolder.addGoalViewOnClickListener(new GoalViewOnClickListener() {
            @Override
            public void onClick(GoalViewHolder goalViewHolder) {
                notifyGoalViewClicked(goalViewHolder);
            }

            @Override
            public void onLongClick(GoalViewHolder goalViewHolder) {
                notifyGoalViewLongClicked(goalViewHolder);
            }
        });
        return goalViewHolder;
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.getTitle().setText(goal.getName());
        holder.getSummary().setText(goal.getSummary());
        holder.getGoal().setText(String.valueOf(goal.getRequiredPoints()));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class GoalViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private TextView summary;
        private TextView goal;

        private MultiSelector multiSelector;

        private List<GoalViewOnClickListener> goalViewOnClickListeners;

        public GoalViewHolder(View v, MultiSelector multiSelector) {
            super(v, multiSelector);
            this.title = (TextView) v.findViewById(R.id.goaltitle);
            this.summary = (TextView) v.findViewById(R.id.goalsummary);
            this.goal = (TextView) v.findViewById(R.id.goalpoints);

            this.goalViewOnClickListeners = new ArrayList<>();

            //Bind multiselector to click events
            this.multiSelector = multiSelector;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            //Disable Animations and shadows
            setDefaultModeStateListAnimator(new StateListAnimator());
            setSelectionModeStateListAnimator(new StateListAnimator());
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

        public void addGoalViewOnClickListener(GoalViewOnClickListener f){
            goalViewOnClickListeners.add(f);
        }

        public void notifyGoalViewClicked(){
            for(GoalViewOnClickListener f : goalViewOnClickListeners){
                f.onClick(this);
            }
        }

        public void notifyGoalViewLongClicked(){
            for(GoalViewOnClickListener f : goalViewOnClickListeners){
                f.onLongClick(this);
            }
        }

        @Override
        public void onClick(View view) {
            if(multiSelector.tapSelection(this)){
                if(multiSelector.isSelected(this.getAdapterPosition(), 0)) {
                    multiSelector.setSelected(this, true);
                } else {
                    multiSelector.setSelected(this, false);
                }
            }
            notifyGoalViewClicked();
        }

        @Override
        public boolean onLongClick(View view) {
            if(!multiSelector.isSelectable()){
                multiSelector.setSelectable(true);
                multiSelector.setSelected(this, true);
                notifyGoalViewLongClicked();
                return true;
            }
            notifyGoalViewLongClicked();
            return false;
        }
    }

}
