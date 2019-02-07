package com.suspedeal.makeitbig.addTheme;

import com.suspedeal.makeitbig.base.IBaseActivityView;

/**
 * TODO: Add a class header comment!
 */
public interface IAddNewThemeActivityView extends IBaseActivityView {
    void showToast(String text);
    void addUidToEditText(String uid);

    void removeUidFromEditText();
}
