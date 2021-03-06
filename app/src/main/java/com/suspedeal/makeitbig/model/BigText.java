package com.suspedeal.makeitbig.model;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class BigText implements Serializable {

    private String text;
    private String textColour;
    private String uid;
    private boolean active;
    private boolean free;
    private boolean isSelected;
    private String backgroundUrl;
    private String name;
    private boolean promoted;

    public BigText() {
    }

    public BigText(String text, String themeName, String textColour, boolean active, boolean free, boolean promoted) {
        this.text = text;
        this.name = themeName;
        this.textColour = textColour;
        this.active = active;
        this.free = free;
        this.backgroundUrl = "";
        this.promoted = promoted;
    }

    @Exclude
    public String getText() {
        return text;
    }

    public String getTextColour() {
        return textColour;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setText(String mText) {
        this.text = mText;
    }

    public void setTextColour(String mTextColour) {
        this.textColour = mTextColour;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    @Exclude
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
