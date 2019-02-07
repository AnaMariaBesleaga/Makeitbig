package com.suspedeal.makeitbig.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suspedeal.makeitbig.R;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<String> mHistoryListArray;
    private OnHistoryTextClickListener listener;

    public HistoryAdapter(OnHistoryTextClickListener listener) {
        this.listener = listener;
        mHistoryListArray = new ArrayList<>();
    }

    public void reverse() {
        Collections.reverse(mHistoryListArray);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView delete;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.theme_name);
            delete = view.findViewById(R.id.deleteHistoryItem);
        }
    }

    public void add(String text) {
        mHistoryListArray.add(0, text);
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_main_single, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.MyViewHolder holder, final int position) {
        final String text = mHistoryListArray.get(position);
        holder.text.setText(text);
        holder.text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnTextClicked(text);
            }
        });

        holder.delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistoryListArray.remove(position);
                listener.OnItemDeleted();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHistoryListArray.size();
    }

    public String getTextListPosition(int position ){
        return mHistoryListArray.get(position);
    }
}
