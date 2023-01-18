package com.alhasawi.acekuwait.ui.main.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pushwoosh.firebase.PushwooshFcmHelper;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Push";

    @Override
    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.e("onNewToken", s);
//        Freshchat.getInstance(this).setPushRegistrationToken(s);
        PushwooshFcmHelper.onTokenRefresh(s);
        sendRegistrationToServer(s);
        sendTokenToServer(s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "msg" + new Gson().toJson(remoteMessage));
//        PushwooshFcmHelper.onMessageReceived(getApplicationContext(), remoteMessage);
//        sendNotification(getApplicationContext(), remoteMessage.getData().get("title"));
        try {
            if (PushwooshFcmHelper.isPushwooshMessage(remoteMessage)) {
                //this is a Pushwoosh push, SDK will handle it automatically
                PushwooshFcmHelper.onMessageReceived(this, remoteMessage);
//                sendNotification(getApplicationContext(), remoteMessage.getData().get("title"));
            } else {
//                if (remoteMessage.getData() != null && remoteMessage.getData().containsKey("source") && remoteMessage.getData().get("source").equals("Insider")) {
//                    InAppEvents.handleFCMNotification(getApplicationContext(), remoteMessage);
////                    Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().toString());
//                }
//                if (Freshchat.isFreshchatNotification(remoteMessage)) {
//                    Freshchat.handleFcmMessage(this, remoteMessage);
//                }
                // Check if message contains a data payload.
                if (remoteMessage.getData().size() > 0) {
                    sendNotification(getApplicationContext(), remoteMessage.getNotification().getBody());

                }
            }

        } catch (Exception e) {
        }
        //


//        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Context context, String messageBody) {

        String channelId = "CHANNEL_ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ace_logo)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setColor(context.getResources().getColor(R.color.ace_theme_color))
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "ACE KUWAIT ONLINE",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendRegistrationToServer(String token) {
        // send token to web service ??
        String user_id = "", device_id = "";
        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        user_id = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        device_id = InAppEvents.getDeviceId();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReferenceFromUrl("https://ace-kuwait-e1356-default-rtdb.firebaseio.com/");
        ref.child("users").child("user_id").setValue(user_id);
        ref.child("users").child("device_id").setValue(device_id);
        ref.child("users").child("token").setValue(token);

        // then store your token ID
        ref.push().setValue(token);
    }

    private void sendTokenToServer(String token) {
        // Access a Cloud Firestore instance from your Activity
        String user_id = "", device_id = "";
        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        user_id = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        device_id = InAppEvents.getDeviceId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", user_id);
        user.put("device_id", device_id);
        user.put("token", token);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
