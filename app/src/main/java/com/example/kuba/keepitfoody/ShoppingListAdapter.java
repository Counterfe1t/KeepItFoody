package com.example.kuba.keepitfoody;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Custom RecyclerView.Adapter used to properly display
 * a list of shopping list item type objects.
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private Context context;
    private ArrayList<Ingredient> shoppingList;
    private ShoppingListAdapter.OnItemClickListener listener;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public ShoppingListAdapter(Context context, ArrayList<Ingredient> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;
    }

    /**
     * Public interface used to handle ShoppingList type item onClick events.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ShoppingListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewQuantity;
        private CheckBox checkBox;
        private ImageView icon;

        public ShoppingListViewHolder(@NonNull View itemView, final ShoppingListAdapter.OnItemClickListener listener) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            icon = itemView.findViewById(R.id.icon);
            checkBox = itemView.findViewById(R.id.checkbox);

            itemView.setOnClickListener(v -> {
                /*if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }*/

                if (checkBox.isChecked())
                    checkBox.setChecked(false);
                else
                    checkBox.setChecked(true);
            });

        }
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_shopping_item, null);
        return new ShoppingListAdapter.ShoppingListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        Ingredient item = shoppingList.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewQuantity.setText(item.getQuantity() + " g");
        holder.icon.setImageDrawable(context.getResources().getDrawable((item.getIcon())));

        holder.checkBox.setChecked(false);
        holder.checkBox.setOnCheckedChangeListener((buttonView, status) -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
