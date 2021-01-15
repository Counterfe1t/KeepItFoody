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
 * a list of Specialist type objects.
 */
public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder> {

    private Context context;
    private ArrayList<Specialist> specialists;
    private SpecialistAdapter.OnItemClickListener listener;

    public SpecialistAdapter(Context context, ArrayList<Specialist> specialists) {
        this.context = context;
        this.specialists = specialists;
    }

    /**
     * Public interface used to handle Specialist type item onClick events.
     */
    public interface OnItemClickListener {
        void onContactClick(int position);
    }

    public void setOnItemClickListener(SpecialistAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class SpecialistViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewSpecialist;
        private ImageView optionMenu;
        private TextView textViewFullName;
        private TextView textViewProfession;
        private TextView textViewSpecialization;

        public SpecialistViewHolder(@NonNull View itemView, final SpecialistAdapter.OnItemClickListener listener) {
            super(itemView);

            imageViewSpecialist = itemView.findViewById(R.id.imageViewSpecialist);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewProfession = itemView.findViewById(R.id.textViewProfession);
            textViewSpecialization = itemView.findViewById(R.id.textViewSpecialization);

        }
    }

    @NonNull
    @Override
    public SpecialistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_specialist, null);
        return new SpecialistAdapter.SpecialistViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialistAdapter.SpecialistViewHolder holder, int position) {
        Specialist specialist = specialists.get(position);
        holder.textViewFullName.setText(specialist.getFirstName() + "\n" + specialist.getLastName());
        holder.textViewProfession.setText(specialist.getProfession());
        holder.textViewSpecialization.setText(specialist.getSpecialization());

        String imageUrl = "https://192.168.134.62/api" + specialist.getPicture();
        Picasso.with(context).load(imageUrl).fit().centerInside().into(holder.imageViewSpecialist);

        holder.optionMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.optionMenu);
            popup.inflate(R.menu.specialist_item_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.contactAction:
                        if (listener != null) {
                            listener.onContactClick(position);
                        }
                        break;
                }
                return false;
            });
            popup.show();

        });
    }

    @Override
    public int getItemCount() {
        return specialists.size();
    }

}
