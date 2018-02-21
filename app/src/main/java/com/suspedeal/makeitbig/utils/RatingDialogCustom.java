package com.suspedeal.makeitbig.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RatingBar;

import com.codemybrainsout.ratingdialog.RatingDialog;

/**
 * TODO: Add a class header comment!
 */
public class RatingDialogCustom extends RatingDialog {

    private static final String SHOW_NEVER = "show_never";
    private Context context;
    private String MyPrefs = "RatingDialog";
    private SharedPreferences sharedpreferences;

    public RatingDialogCustom(Context context, Builder builder) {
        super(context, builder);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        super.onRatingChanged(ratingBar, v, b);
        if(ratingBar.getRating() <= 2 ){
            showNeverOff();
        }
    }

    private void showNeverOff() {

        sharedpreferences = context.getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(SHOW_NEVER, false);
        editor.apply();
    }

    public static class BuilderCustom extends RatingDialog.Builder {
        public BuilderCustom(Context context) {
            super(context);
        }
    }
}
