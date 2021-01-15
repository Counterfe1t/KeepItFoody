package com.example.kuba.keepitfoody;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Custom RecyclerView.Adapter used to properly display
 * a list of Meal type objects.
 */
public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private Context context;
    private ArrayList<Meal> meals;
    private MealAdapter.OnItemClickListener listener;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public MealAdapter(Context context, ArrayList<Meal> meals) {
        this.context = context;
        this.meals = meals;
    }

    /**
     * Public interface used to handle Meal type item onClick events.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onStatusChanged(int position, boolean status);
    }

    public void setOnItemClickListener(MealAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView energyText;
        private TextView textViewTime;
        private ImageView icon;
        private ImageView optionMenu;
        private CheckBox checkBoxStatus;

        public MealViewHolder(@NonNull View itemView, final MealAdapter.OnItemClickListener listener) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name);
            energyText = itemView.findViewById(R.id.energy);
            textViewTime = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.icon);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            checkBoxStatus = itemView.findViewById(R.id.status);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_meal, null);
        return new MealAdapter.MealViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.nameText.setText(meal.getName());
        holder.energyText.setText(String.valueOf(BigDecimal.valueOf(meal.getEnergy()).setScale(0, BigDecimal.ROUND_HALF_UP)));
        holder.icon.setImageDrawable(context.getResources().getDrawable((meal.getIcon())));
        holder.textViewTime.setText(meal.getTime());

        if(meal.isStatus()) {
            holder.checkBoxStatus.setChecked(true);
        } else {
            holder.checkBoxStatus.setChecked(false);
        }

        holder.checkBoxStatus.setOnCheckedChangeListener((buttonView, status) -> {
            if (listener != null) {
                listener.onStatusChanged(position, status);
            }
        });

        holder.optionMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.optionMenu);
            popup.inflate(R.menu.recipe_item_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete:
                        if (listener != null) {
                                listener.onDeleteClick(position);
                        }
                        break;
                }
                return false;
            });
            popup.show();

        });

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    /**
     * Animate item at position in RecyclerView.
     * @param viewToAnimate
     * @param position
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}