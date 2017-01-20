package nl.rdewildt.addone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nl.rdewildt.addone.model.Bonus;

/**
 * Created by roydewildt on 05/01/2017.
 */

public class BonusesAdapter extends RecyclerView.Adapter<BonusesAdapter.BonusViewHolder> {
    private List<Bonus> bonuses;

    public BonusesAdapter(List<Bonus> bonuses){ this.bonuses = bonuses; }

    @Override
    public BonusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bonus_item, parent, false);
        return new BonusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BonusViewHolder holder, int position) {
        Bonus bonus = bonuses.get(position);
        holder.getTitle().setText(bonus.getName());
        holder.getPoints().setText(String.valueOf(bonus.getPoints()));
        holder.getReward().setText(String.valueOf(bonus.getReward()));
        holder.getRemoveIcon().setOnClickListener(view -> {
            bonuses.remove(bonus);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return bonuses.size();
    }


    public static class BonusViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView points;
        private TextView reward;
        private ImageView rmicon;

        public BonusViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.bonustitle);
            this.points = (TextView) v.findViewById(R.id.bonuspoints);
            this.reward = (TextView) v.findViewById(R.id.bonusreward);
            this.rmicon = (ImageView) v.findViewById(R.id.bonusremove);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getPoints() {
            return points;
        }

        public TextView getReward() {
            return reward;
        }

        public ImageView getRemoveIcon(){ return rmicon; }
    }
}
