package com.example.kuba.keepitfoody;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Custom RecyclerView.Adapter used to properly display
 * a list of Recipe type objects.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;
    private OnItemClickListener listener;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    /**
     * Public interface used to handle Recipe type item onClick events.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewTime;
        private TextView textViewDifficulty;
        private ImageView recipeImage;
        private ImageView authorImage;
        private TextView authorFirstName;

        private ImageView optionMenu;

        public RecipeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.name);
            textViewTime = itemView.findViewById(R.id.minutes);
            textViewDifficulty = itemView.findViewById(R.id.textViewDifficulty);
            recipeImage = itemView.findViewById(R.id.image);
            authorImage = itemView.findViewById(R.id.authorImage);
            authorFirstName = itemView.findViewById(R.id.authorName);
            optionMenu = itemView.findViewById(R.id.optionMenu);



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
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_recipe, null);
        return new RecipeAdapter.RecipeViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.textViewName.setText(recipe.getName());
        holder.textViewTime.setText(String.valueOf(recipe.getPreparationTime()));
        holder.authorFirstName.setText(recipe.getAuthorFirstName());

        String imageUrl = "https://192.168.134.62/api" + recipe.getImage();
        Picasso.with(context).load(imageUrl).fit().centerInside().into(holder.recipeImage);

        String authorImageUrl = "https://192.168.134.62/api" + recipe.getAuthorImage();
        Picasso.with(context).load(authorImageUrl).fit().centerInside().into(holder.authorImage);

        switch (recipe.getDifficulty()) {
            case 0:
            case 1:
                holder.textViewDifficulty.setText("Bardzo łatwy");
                break;
            case 2:
                holder.textViewDifficulty.setText("Łatwy");
                break;
            case 3:
                holder.textViewDifficulty.setText("Średni");
                break;
            case 4:
                holder.textViewDifficulty.setText("Trudny");
                break;
            default:
                holder.textViewDifficulty.setText("Bardzo trudny");
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}
