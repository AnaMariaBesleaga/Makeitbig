package com.suspedeal.makeitbig;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suspedeal.makeitbig.model.BigText;

import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private ArrayList<BigText> mThemeList;
    private OnThemeClickedListener listener;
    private Bitmap mBackground;

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
        ImageView background;
        RelativeLayout rootLayout;
        TextView freePaid;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.theme_name);
            selected = view.findViewById(R.id.isSelectedTheme);
            background = view.findViewById(R.id.single_theme_background);
            rootLayout = view.findViewById(R.id.theme_root_layout);
            freePaid = view.findViewById(R.id.free_paid_text);
            selected.setVisibility(View.GONE);
        }
    }

    public void add(BigText theme) {
        mThemeList.add(0, theme);
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

        Picasso.get().load(theme.getBackgroundUrl()).into(holder.background);

        holder.freePaid.setText(mapBooleanToPrettyText(theme.isFree()));

        if(theme.isSelected()){
            holder.selected.setVisibility(View.VISIBLE);
        }else{
            holder.selected.setVisibility(View.GONE);
        }

        holder.text.setTextColor(Color.parseColor(theme.getTextColour()));
        holder.text.setText(theme.getName());
        holder.rootLayout.setOnClickListener(new OnClickListener() {
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

    private String mapBooleanToPrettyText(boolean isFree) {

        if(isFree){
            return "Free";
        }

        return "Paid";
    }

    @Override
    public int getItemCount() {
        return mThemeList.size();
    }

    public BigText getThemeListPosition(int position ){
        return mThemeList.get(position);
    }
}
