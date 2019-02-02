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

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private ArrayList<BigText> mThemeList;
    private OnThemeClickedListener listener;

    public ThemeAdapter(OnThemeClickedListener listener) {
        this.listener = listener;
        mThemeList = new ArrayList<>();
    }

    public ArrayList<BigText> getThemes() {
        return mThemeList;
    }

    public void setFirstThemeAsDefault() {
        mThemeList.get(0).setSelected(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView selected;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.theme_name);
            selected = view.findViewById(R.id.isSelectedTheme);
            selected.setVisibility(View.GONE);
        }
    }

    public void add(BigText theme) {

        mThemeList.add(theme);
    }

    @NonNull
    @Override
    public ThemeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_theme_single, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ThemeAdapter.MyViewHolder holder, final int position) {
        final BigText theme = mThemeList.get(position);

        if(theme.isSelected()){
            holder.selected.setVisibility(View.VISIBLE);
        }else{
            holder.selected.setVisibility(View.GONE);
        }

        holder.text.setText(theme.getName());
        holder.text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //set selected for current item in list
                mThemeList.get(position).setSelected(true);
                holder.selected.setVisibility(View.VISIBLE);

                //set selected to false for all other items
                for(BigText bigTextSelected : mThemeList){
                    //set selected to false for all other entries in the list
                    if(!(bigTextSelected == mThemeList.get(position))){
                        bigTextSelected.setSelected(false);
                    }
                }

                listener.onThemeClicked(theme);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThemeList.size();
    }

    public BigText getThemeListPosition(int position ){
        return mThemeList.get(position);
    }
}
