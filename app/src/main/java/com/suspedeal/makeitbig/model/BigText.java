package com.suspedeal.makeitbig.model;

import java.io.Serializable;

/**
 * TODO: Add a class header comment!
 */
public class BigText implements Serializable {
    private String mText;
    private int mTextColour;
    private int mBackgroundColour;

    public BigText(String mText, int mTextColour, int mBackgroundColour) {
        this.mText = mText;
        this.mTextColour = mTextColour;
        this.mBackgroundColour = mBackgroundColour;
    }

    public String getText() {
        return mText;
    }

    public int getTextColour() {
        return mTextColour;
    }

    public int getBackgroundColour() {
        return mBackgroundColour;
    }
}
