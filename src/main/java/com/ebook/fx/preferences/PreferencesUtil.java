package com.ebook.fx.preferences;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.inject.Singleton;

/**
 *
 * @author maykoone
 */
@Singleton
public class PreferencesUtil {
    private final Preferences preferences;
    public static final String LAST_OPEN_DIRECTORY_PREF = "last_open_directory";

    {
        preferences = Preferences.userRoot().node("/com/ebookfx/preferences");
    }

    public void set(String key, String value){
        try {
            preferences.put(key, value);
            preferences.flush();
        } catch (BackingStoreException bse) {
        }
    }

    public String get(String key, String defaultValue){
        return preferences.get(key, defaultValue);
    }
}