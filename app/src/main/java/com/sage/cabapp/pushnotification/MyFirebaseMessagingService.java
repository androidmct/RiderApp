package com.sage.cabapp.pushnotification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sage.cabapp.R;
import com.sage.cabapp.SageApp;
import com.sage.cabapp.ui.addriverrating.AddDriverRatingActivity;
import com.sage.cabapp.ui.chatmodulenew.ChatModuleNewActivity;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenDatum;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenMainRequest;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenRequestData;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenResponse;
import com.sage.cabapp.ui.requestaccepted.RequestAcceptedActivity;
import com.sage.cabapp.ui.splash.SplashActivity;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;
import com.twilio.chat.ChatClient;
import com.twilio.chat.NotificationPayload;

import java.util.Random;
import java.util.StringTokenizer;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedData.saveString(getApplicationContext(), Constant.REFRESHED_TOKEN, s);

        if (NetworkAvailability.checkNetworkStatus(this)) {
            updateDeviceTokenWS(this);
        }
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {

            String message = remoteMessage.getData().get("message");
            String title = remoteMessage.getData().get("title");

            String type = remoteMessage.getData().get("type");
            String requestId = remoteMessage.getData().get("requestId");
            String userid = SharedData.getString(this, Constant.USERID);

            //requestAccepted
            if (type != null && type.equalsIgnoreCase("rideStart")) {

                if (userid != null && !userid.equalsIgnoreCase("")) {
                    SharedData.saveString(this, Constant.PERM_REQUEST_ID, requestId);
                    SharedData.saveBool(this, Constant.RIDE_STARTED, true);

                    sendNotificationAcceptRequest(message, title);

                    Intent intent = new Intent(this, RequestAcceptedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            } else if (type != null && type.equalsIgnoreCase("rideStop")) {

                if (userid != null && !userid.equalsIgnoreCase("")) {
                    SharedData.saveString(this, Constant.PERM_REQUEST_ID, requestId);
                    SharedData.saveBool(this, Constant.RIDE_STARTED, false);
                    sendNotification(message, title);

                    Intent intent = new Intent(this, AddDriverRatingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

            } else if (type != null && type.equalsIgnoreCase("requestAccepted")) {

                if (userid != null && !userid.equalsIgnoreCase("")) {
                    SharedData.saveString(this, Constant.PERM_REQUEST_ID, requestId);
                    SharedData.saveBool(this, Constant.RIDE_STARTED, false);
                    sendNotification(message, title);

                    Intent intent = new Intent(this, RequestAcceptedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            } else if (type != null && type.equalsIgnoreCase("requestcancel")) {

                if (userid != null && !userid.equalsIgnoreCase("")) {
                    SharedData.saveString(this, Constant.PERM_REQUEST_ID, "");
                    SharedData.saveString(this, Constant.TEMP_REQUEST_ID, "");
                    SharedData.saveBool(this, Constant.RIDE_STARTED, false);

                    sendNotification(message, title);

                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            } else {

                if (userid != null && !userid.equalsIgnoreCase("")) {
                    NotificationPayload payload = new NotificationPayload(remoteMessage.getData());

                    ChatClient client = SageApp.get().getChatClientManager().getChatClient();
                    if (client != null) {
                        client.handleNotification(payload);
                    }

                    NotificationPayload.Type notifType = payload.getType();

                    if (notifType == NotificationPayload.Type.UNKNOWN) {
                        sendNotification(message, title);
                        return; // Ignore everything we don't support
                    }

                    title = "Chat Notification";

                    if (notifType == NotificationPayload.Type.NEW_MESSAGE)
                        message = payload.getBody();

                    if (message != null && !message.equalsIgnoreCase("")) {
                        try {
                            StringTokenizer st = new StringTokenizer(message, ":");
                            String lat = st.nextToken();
                            String msg = st.nextToken();

                            sendNotificationTwilio(msg, title, payload.getChannelTitle());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        } else if (remoteMessage.getNotification() != null) {
            // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getTitle() != null && !remoteMessage.getNotification().getTitle().equalsIgnoreCase("")) {
                sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
            }

        } else {
            sendNotification("Hello Buddy", getResources().getString(R.string.app_name));
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String title) {
        Intent intent = null;

        intent = new Intent(this, SplashActivity.class);

        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setAutoCancel(true)
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

       /* Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;*/
        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    private void sendNotificationAcceptRequest(String messageBody, String title) {
        Intent intent = null;

        intent = new Intent(this, RequestAcceptedActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.sage_driver_tone);  //Here is FILE_NAME is the name of file that you want to play


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setAutoCancel(true)
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setSound(defaultSoundUri, attributes);
                notificationManager.createNotificationChannel(channel);
            }
        }

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        if (notificationManager != null) {
            notificationManager.notify(m, notificationBuilder.build());
        }
    }

    void updateDeviceTokenWS(Context context) {

        String userid = SharedData.getString(context, Constant.USERID);
        String deviceToken = SharedData.getString(context, Constant.REFRESHED_TOKEN);

        if (deviceToken != null && !deviceToken.equalsIgnoreCase("")) {

            UpdateDeviceTokenMainRequest updateDeviceTokenMainRequest = new UpdateDeviceTokenMainRequest();
            UpdateDeviceTokenRequestData updateDeviceTokenRequestData = new UpdateDeviceTokenRequestData();
            UpdateDeviceTokenDatum updateDeviceTokenDatum = new UpdateDeviceTokenDatum();

            updateDeviceTokenDatum.setUserid(userid);
            updateDeviceTokenDatum.setDevicetype("Android");
            updateDeviceTokenDatum.setDevicetoken(deviceToken);

            updateDeviceTokenRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
            updateDeviceTokenRequestData.setRequestType("updateDeviceToken");
            updateDeviceTokenRequestData.setData(updateDeviceTokenDatum);

            updateDeviceTokenMainRequest.setRequestData(updateDeviceTokenRequestData);

            AndroidNetworking.post(ApiConstants.UserRegistration)
                    .addApplicationJsonBody(updateDeviceTokenMainRequest)
                    .setTag("userRegistration")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateDeviceTokenResponse.class, new ParsedRequestListener<UpdateDeviceTokenResponse>() {

                        @Override
                        public void onResponse(UpdateDeviceTokenResponse response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }

    }

    private void sendNotificationTwilio(String messageBody, String title, String cSid) {
        Intent intent = null;

        intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (!"".contentEquals(cSid)) {
            intent.putExtra("REQUEST_ID", cSid);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setAutoCancel(true)
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

       /* Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;*/
        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}