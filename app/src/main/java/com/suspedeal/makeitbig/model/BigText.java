package com.suspedeal.makeitbig.model;

import java.io.Serializable;

/**
 * TODO: Add a class header comment!
 */
public class BigText implements Serializable {

    private String mText;
    private int mTextColour;
    private int mBackgroundColour;
    private String uid;

    public BigText(String mText, int mTextColour, int mBackgroundColour) {
        this.mText = mText;
        this.mTextColour = mTextColour;
        this.mBackgroundColour = mBackgroundColour;
    }

    public BigText() {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public void setTextColour(int mTextColour) {
        this.mTextColour = mTextColour;
    }

    public void setBackgroundColour(int mBackgroundColour) {
        this.mBackgroundColour = mBackgroundColour;
    }
}
