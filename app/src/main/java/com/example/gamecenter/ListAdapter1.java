package com.example.gamecenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListAdapter1 extends RecyclerView.Adapter<ListAdapter1.ViewHolder> {


    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView scoreItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            scoreItemView = (TextView) itemView.findViewById(R.id.stats);
        }
    }
    private final LayoutInflater mInflater;
    Context mContext;
    ListOpenHelper mDB;

    public ListAdapter1(Context context, ListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.ranking_list2048, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stats current = mDB.query2048(position);
        holder.scoreItemView.setText(current.getName() + " (" + current.getBoard() +"x" +current.getBoard()+ ") " + current.getScore() + "  " + current.getTime());

    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }
}


