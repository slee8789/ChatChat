package leesc.chatchat.utils;

import android.content.Context;

public class ConfigSettingPreferences {

    private static final String PREFS_CONFIG = "prefs_config";

    private static final String PREFS_KEY_NOTICE_VIBRATE = "notice_vibrate";
    private static final String PREFS_KEY_NOTICE_NOTIFICATION = "notice_notification";
    private static final String PREFS_KEY_NOTICE_MESSAGE = "notice_message";
    private static final String PREFS_KEY_BACKGROUND_URI = "background_uri";
    private static final String PREFS_KEY_TEXT_SIZE_INDEX = "text_size_index";
    private static final String PREFS_KEY_TEXT_SIZE_NAME = "text_size_name";
    private static final String PREFS_KEY_TEXT_SIZE = "text_size";
//    private static final String PREFS_KEY_NOTI_SOUND_INDEX = "noti_sound_index";
//    private static final String PREFS_KEY_NOTI_SOUND_NAME = "noti_sound_name";
    private static final String PREFS_KEY_NOTI_SOUND = "noti_sound";

    private static Context sContext;
    private static ConfigSettingPreferences sInstance = new ConfigSettingPreferences();

    private ConfigSettingPreferences() {

    }

    public static ConfigSettingPreferences getInstance(Context context) {
        sContext = context;
        return sInstance;
    }

    public void setPrefNoticeVibrate(boolean isVibrate) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_VIBRATE, isVibrate);
    }

    public boolean getPrefNoticeVibrate() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_VIBRATE, true);
    }
    
    public void setPrefNoticeNotification(boolean isNoti) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_NOTIFICATION, isNoti);
    }

    public boolean getPrefNoticeNotification() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_NOTIFICATION, true);
    }

    public void setPrefNoticeMessage(boolean isMessage) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_MESSAGE, isMessage);
    }

    public boolean getPrefNoticeMessage() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_MESSAGE, true);
    }
    
    public void setPrefBackgroundUri(String uri) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_BACKGROUND_URI, uri);
    }

    public String getPrefBackgroundUri() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_BACKGROUND_URI, null);
    }
    
    public void setPrefTextSizeIndex(int position) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_TEXT_SIZE_INDEX, position);
    }

    public int getPrefTextSizeIndex() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_TEXT_SIZE_INDEX, 2);
    }
    
    public void setPrefTextSizeName(String sizeName) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_TEXT_SIZE_NAME, sizeName);
    }

    public String getPrefTextSizeName() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_TEXT_SIZE_NAME, "보통");
    }
    
    public void setPrefTextSize(int size) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_TEXT_SIZE, size);
    }

    public int getPrefTextSize() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_TEXT_SIZE, 16);
    }
    
    public void setPrefNotiSound(boolean isSoundOn) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND, isSoundOn);
    }

    public boolean getPrefNotiSound() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND, true);
    }
    
//    public void setPrefNotiSoundIndex(int position) {
//        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND_INDEX, position);
//    }
//
//    public int getPrefNotiSoundIndex() {
//        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND_INDEX, 1);
//    }
//    
//    public void setPrefNotiSoundName(String soundName) {
//        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND_NAME, soundName);
//    }
//
//    public String getPrefNotiSoundName() {
//        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND_NAME, "Beep once");
//    }

}
