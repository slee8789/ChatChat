/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package leesc.chatchat.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import leesc.chatchat.MainActivity;
import leesc.chatchat.MessageActivity;
import leesc.chatchat.R;
import leesc.chatchat.db.MessageDB;
import leesc.chatchat.utils.CommonUtils;
import leesc.chatchat.utils.ConfigSettingPreferences;
import leesc.chatchat.utils.PhoneUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private NotificationManager mNotificationManager;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String number = remoteMessage.getData().get("number");
        String sender = remoteMessage.getData().get("sender");
        String message = remoteMessage.getData().get("message");

        MessageDB.getInstance().storeMessage(MyFirebaseMessagingService.this, CommonUtils.MESSAGE, number, sender, message, MessageDB.RECEIVE_TYPE);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        if(ConfigSettingPreferences.getInstance(getApplicationContext()).getPrefNoticeNotification()) {
            setPushNotification(getApplicationContext(), 0, message, number, sender);
        }

        int unreadCount = MessageDB.getInstance().queryForUnreadCount(MyFirebaseMessagingService.this);
        CommonUtils.updateIconBadge(MyFirebaseMessagingService.this, unreadCount);

        if (!PhoneUtils.isScreenOn(getApplicationContext())
                && ConfigSettingPreferences.getInstance(getApplicationContext()).getPrefNoticeMessage()) {
            // TODO :: FCM 도착 시 화면 꺼짐을 체크하여 메시지 알림 액티비티 생성
//            Intent i = new Intent(MyFirebaseMessagingService.this, PopupActivity.class);
//            i.putExtra("type", type);
//            i.putExtra("message", message);
//            i.putExtra("senderName", senderName);
//            i.putExtra("senderNumber", senderNumber);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.contact)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void setPushNotification(Context context, int id, String msg, String number, String name) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(name).setContentText(msg)
                .setTicker(msg).setAutoCancel(true);

        boolean isVibrate = ConfigSettingPreferences.getInstance(getApplicationContext()).getPrefNoticeVibrate();
        boolean isSound = ConfigSettingPreferences.getInstance(getApplicationContext()).getPrefNotiSound();
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

        final long threadId = MessageDB.getInstance().getThreadId(getApplicationContext(), number);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MessageActivity.THREAD_ID, threadId);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }

    private void releaseNotification(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }
}
