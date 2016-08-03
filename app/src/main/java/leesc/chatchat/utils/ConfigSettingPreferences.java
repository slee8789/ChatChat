package leesc.chatchat.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import leesc.chatchat.MainActivity;
import leesc.chatchat.MessageActivity;
import leesc.chatchat.R;
import leesc.chatchat.db.MessageDB;

public class ConfigSettingPreferences {

    private static final String PREFS_CONFIG = "prefs_config";

    private static final String PREFS_KEY_NOTICE_VIBRATE = "notice_vibrate";
    private static final String PREFS_KEY_NOTICE_NOTIFICATION = "notice_notification";
    private static final String PREFS_KEY_NOTICE_MESSAGE = "notice_message";
    private static final String PREFS_KEY_BACKGROUND_URI = "background_uri";
    private static final String PREFS_KEY_TEXT_SIZE_INDEX = "text_size_index";
    private static final String PREFS_KEY_TEXT_SIZE_NAME = "text_size_name";
    private static final String PREFS_KEY_TEXT_SIZE = "text_size";
    private static final String PREFS_KEY_NOTI_SOUND = "noti_sound";

    private static Context sContext;
    private static ConfigSettingPreferences sInstance = new ConfigSettingPreferences();
    private static NotificationManager mNotificationManager;

    private ConfigSettingPreferences() {

    }

    public static ConfigSettingPreferences getInstance(Context context) {
        sContext = context;
        return sInstance;
    }

    public static void setPushNotification(int id, String msg, String number, String name) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(sContext)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(name).setContentText(msg)
                .setTicker(msg).setAutoCancel(true);

        boolean isVibrate = ConfigSettingPreferences.getInstance(sContext).getPrefNoticeVibrate();
        boolean isSound = ConfigSettingPreferences.getInstance(sContext).getPrefNotiSound();
        if (isVibrate) {
            if (isSound) {
                mBuilder.setDefaults(Notification.DEFAULT_ALL);
            } else {
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }
        } else {
            if (isSound) {
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            } else {
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
            }
        }

        final long threadId = MessageDB.getInstance().getThreadId(sContext, number);

        Intent resultIntent = new Intent(sContext, MainActivity.class);
        resultIntent.putExtra(MessageActivity.THREAD_ID, threadId);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(sContext);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) sContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }

    public static void releaseNotification() {
        mNotificationManager = (NotificationManager) sContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    public void setPrefNoticeVibrate(boolean isVibrate) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_VIBRATE, isVibrate);
    }

    public boolean getPrefNoticeVibrate() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTICE_VIBRATE, true);
    }

    public void setPrefNotiSound(boolean isSoundOn) {
        SharedPreferencesHelper.setValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND, isSoundOn);
    }

    public boolean getPrefNotiSound() {
        return SharedPreferencesHelper.getValue(sContext, PREFS_CONFIG, PREFS_KEY_NOTI_SOUND, true);
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

}
