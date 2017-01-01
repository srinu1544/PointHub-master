package com.pointhub.db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pointhub.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo1 on 15-12-2016.
 */

public class FirebaseAdapter extends RecyclerView.Adapter<FirebaseAdapter.MyViewHolder> {


    Context context;

    private List<FirebaseData> firebaseDatas = new ArrayList<FirebaseData>();

    public FirebaseAdapter(Context context, List<FirebaseData> firebaseDatas) {
        this.context = context;
        this.firebaseDatas = firebaseDatas;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.firebase_row, parent, false);

        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FirebaseData firebaseData = firebaseDatas.get(position);
        holder.storename.setText(firebaseData.getStorename());
        holder.points.setText(firebaseData.getPoints());
        holder.billamount.setText(firebaseData.getBillamount());
    }

    @Override
    public int getItemCount() {
        return firebaseDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView storename, points, billamount;

        public MyViewHolder(View itemView) {
            super(itemView);

            storename = (TextView) itemView.findViewById(R.id.storeName);
            points = (TextView) itemView.findViewById(R.id.points);
            billamount = (TextView) itemView.findViewById(R.id.billamount);
        }
    }
}
