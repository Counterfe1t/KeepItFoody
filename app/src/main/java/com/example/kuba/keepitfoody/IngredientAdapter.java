package com.example.kuba.keepitfoody;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Custom RecyclerView.Adapter used to properly display
 * a list of Ingredient type objects.
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredients;
    private int flag;
    private OnItemClickListener listener;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients, int flag) {
        this.context = context;
        this.ingredients = ingredients;
        this.flag = flag;
    }

    /**
     * Public interface used to handle Ingredient type item onClick events.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView categoryText;
        private ImageView icon;
        private ImageView gluten;
        private ImageView lactose;
        private TextView quantity;

        public IngredientViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            nameText = itemView.findViewById(R.id.labelName);
            categoryText = itemView.findViewById(R.id.labelCategory);
            icon = itemView.findViewById(R.id.icon);
            gluten = itemView.findViewById(R.id.gluten);
            lactose = itemView.findViewById(R.id.lactose);
            quantity = itemView.findViewById(R.id.quantity);

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
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_ingredient, null);
        return new IngredientViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameText.setText(ingredient.getName());
        holder.categoryText.setText(ingredient.getCategory());
        holder.icon.setImageDrawable(context.getResources().getDrawable((ingredient.getIcon())));

        if (flag == 1) {
            if (ingredient.isGluten()) {
                holder.gluten.setVisibility(View.VISIBLE);
                holder.gluten.setOnClickListener(v -> Toast.makeText(context, "Produkt zawiera gluten.", Toast.LENGTH_SHORT).show());
            } else {
                holder.gluten.setVisibility(View.INVISIBLE);
            }

            if (ingredient.isLactose()) {
                holder.lactose.setVisibility(View.VISIBLE);
                holder.lactose.setOnClickListener(v -> Toast.makeText(context, "Produkt zawiera laktozę.", Toast.LENGTH_SHORT).show());
            } else {
                holder.lactose.setVisibility(View.INVISIBLE);
            }
        } else if (flag == 2) {
            holder.quantity.setVisibility(View.VISIBLE);
            holder.quantity.setText(ingredient.getQuantity() + " g");
        } else if (flag == 3) {
            holder.quantity.setVisibility(View.VISIBLE);
            holder.quantity.setText(ingredient.getQuantity() + " g");
        }


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}