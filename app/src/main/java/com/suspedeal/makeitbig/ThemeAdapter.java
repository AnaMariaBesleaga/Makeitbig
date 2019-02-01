package com.suspedeal.makeitbig;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suspedeal.makeitbig.model.BigText;

import java.util.ArrayList;
import java.util.Collection;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private ArrayList<BigText> themeList;
    private OnThemeClickedListener listener;

    public ThemeAdapter(OnThemeClickedListener listener) {
        this.listener = listener;
        themeList = new ArrayList<>();
    }

    public ArrayList<BigText> getThemes() {
        return themeList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView selected;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.theme_name);
            selected = view.findViewById(R.id.isSelectedTheme);
        }
    }

    public void add(BigText theme) {
        themeList.add(theme);
    }

    @NonNull
    @Override
    public ThemeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_theme_single, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ThemeAdapter.MyViewHolder holder, final int position) {
        final BigText theme = themeList.get(position);
        holder.text.setText(theme.getName());
        holder.text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.selected.setVisibility(View.VISIBLE);
                listener.onThemeClicked(theme);
            }
        });
    }

    @Override
    public int getItemCount() {
        return themeList.size();
    }

    public BigText getThemeListPosition(int position ){
        return themeList.get(position);
    }
}
