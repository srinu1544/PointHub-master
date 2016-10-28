package com.pointhub.db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pointhub.R;

import java.util.ArrayList;

/**
 * Created by venu gopal on 04-10-2016.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    private ArrayList<Points> listitems;
    private int itemLayout;

    public Adapter(Context context, ArrayList<Points> listitems) {
        this.context = context;
        this.listitems = listitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Points point = listitems.get(position);
        holder.storeName.setText(point.getStoreName());
        holder.points.setText(point.getPoints());
        holder.datemod.setText(point.getLastVisited());

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName, points, datemod;

        public ViewHolder(View itemView) {

            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            points = (TextView) itemView.findViewById(R.id.pointssss);
            datemod = (TextView) itemView.findViewById(R.id.datemd);
        }
    }
}