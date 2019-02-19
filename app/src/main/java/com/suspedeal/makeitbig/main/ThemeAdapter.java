package com.suspedeal.makeitbig.main;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.suspedeal.makeitbig.R;
import com.suspedeal.makeitbig.model.BigText;

import java.util.ArrayList;
import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private List<BigText> mThemeList;
    private OnThemeClickedListener listener;
    private Context mContext;

    public ThemeAdapter(OnThemeClickedListener listener, Context context) {
        this.listener = listener;
        mThemeList = new ArrayList<>();
        mContext = context;
    }

    public List<BigText> getThemes() {
        return mThemeList;
    }

    public int getPositionBasedOnThemeId(String themeId){

        for (int i = 0; i < mThemeList.size(); i++)
        {
            BigText bigText = mThemeList.get(i);
            if (bigText.getUid().equals(themeId))
            {
                return mThemeList.indexOf(bigText);
            }
        }

        return -1;

    }

    public void clear() {
        mThemeList = new ArrayList<>();
    }

    public void setThemes(List<BigText> themes) {
        mThemeList = themes;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView background;
        RelativeLayout rootLayout;
        TextView freePaid;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.theme_name);
            background = view.findViewById(R.id.single_theme_background);
            rootLayout = view.findViewById(R.id.theme_root_layout);
            freePaid = view.findViewById(R.id.free_paid_text);
        }
    }

    public void addToStart(BigText theme) {
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

        //this meand that themes have been fetched
        if(!theme.getBackgroundUrl().equals("")){
            //we need this only for the black on white theme because it has borders and we need to show those
            if(theme.getName().equals("Black on white")){
                holder.background.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            Glide.with(mContext).load(theme.getBackgroundUrl()).into(holder.background);
        }

        holder.freePaid.setText(mapBooleanToPrettyText(theme.isFree()));
        holder.text.setTextColor(Color.parseColor(theme.getTextColour()));
        holder.text.setText(theme.getName());
        holder.rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
