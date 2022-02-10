package com.example.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.modelViewHolder> {

    private Context context;
    private String[] name;
    private int[] modelID;
    private ItemTabListenerRec itemTabListenerRec;


    public RecycleAdapter(Context context, String[] name, int[] modelID, ItemTabListenerRec itemTabListenerRec) {
        this.context = context;
        this.name = name;
        this.modelID = modelID;
        this.itemTabListenerRec = itemTabListenerRec;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.model_view_layout, parent, false);

        return new modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int position) {

        String title = name[position];
        holder.textTitle.setText(title);
        int iD = modelID[position];
        //holder.imgIcon.setImageResource(R.drawable.ic_launcher_background);
        holder.imgIcon.setImageResource(iD);

        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemTabListenerRec.getitemNameonTabListener(title);

            }
        });

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        CircleImageView imgIcon;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }
}
