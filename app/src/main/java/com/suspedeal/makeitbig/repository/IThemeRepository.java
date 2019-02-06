package com.suspedeal.makeitbig.repository;

import com.suspedeal.makeitbig.addTheme.OnAddUrlToObjectListener;
import com.suspedeal.makeitbig.addTheme.OnThemeAddedToDBCallback;
import com.suspedeal.makeitbig.main.OnGetThemeCallback;
import com.suspedeal.makeitbig.model.BigText;

/**
 * TODO: Add a class header comment!
 */
public interface IThemeRepository {
    void addThemeToDB(OnThemeAddedToDBCallback onThemeAddedToDBCallback, BigText bigText);
    void getDownloadUrl(OnAddUrlToObjectListener listener);
    void getThemesFromDB(OnGetThemeCallback onGetThemeCallback);
}
