package com.suspedeal.makeitbig.addTheme;

/**
 * TODO: Add a class header comment!
 */
public interface OnThemeAddedToDBCallback {
    void onSuccess(String uid);
    void onFailed(String error);
}
