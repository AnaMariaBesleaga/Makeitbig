package com.suspedeal.makeitbig;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {

    private ArrayList<String> textList;
    private OnTextClickListener listener;

    public TextAdapter(OnTextClickListener listener) {
        this.listener = listener;
        textList = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView delete;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.firstLine);
            delete = view.findViewById(R.id.deleteItem);
        }
    }

    public void add(String text) {
        textList.add(text);
    }

    @Override
    public TextAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_main_single, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TextAdapter.MyViewHolder holder, final int position) {
        final String text = textList.get(position);
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
                textList.remove(position);
                listener.OnItemDeleted();
            }
        });

    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public String getTextListPosition(int position ){
        return textList.get(position);
    }
}
