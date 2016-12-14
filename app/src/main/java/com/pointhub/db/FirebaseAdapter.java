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
 * Created by Lenovo1 on 13-12-2016.
 */

public class FirebaseAdapter extends RecyclerView.Adapter<FirebaseAdapter.ViewHolder> {

    Context context;
    private ArrayList<PointsBO> listitems;
    private int itemLayout;

    public FirebaseAdapter(Context context, ArrayList<PointsBO> listitems) {
        this.context = context;
        this.listitems = listitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayoutfirebse, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PointsBO point = listitems.get(position);
        holder.storeName.setText(point.getStoreName());
        holder.points.setText(point.getPoints());
        holder.datemod.setText(point.getBillAmount());

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