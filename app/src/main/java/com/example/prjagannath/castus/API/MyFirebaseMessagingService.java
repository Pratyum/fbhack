package com.example.prjagannath.castus.API;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.prjagannath.castus.R;
import com.example.prjagannath.castus.CustomUI.NotificationButton;
import com.example.prjagannath.castus.src.PostLoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by Grayson on 9/3/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

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

        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String messageTitle, String messageBody) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent acceptIntent = new Intent(this, PostLoginActivity.class);
        Intent rejectIntent = new Intent(this, NotificationButton.class);
        rejectIntent.setAction("com.fbhack.notifAction");
//        acceptIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        rejectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingAcceptIntent = PendingIntent.getActivity(this, 0 /* Request code */, acceptIntent,
                PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingRejectIntent = PendingIntent.getActivity(this, 0 /* Request code */, rejectIntent,
                0);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notifi = new Notification.Builder(this)
                .setSmallIcon(R.drawable.com_facebook_button_icon_blue)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingAcceptIntent)
                .addAction(R.drawable.ic_reject, "Reject", pendingRejectIntent)
                .addAction(R.drawable.ic_accept, "Accept", pendingAcceptIntent)
                .setDeleteIntent(pendingRejectIntent)
                .build();

        notifi.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notifi);

    }
}