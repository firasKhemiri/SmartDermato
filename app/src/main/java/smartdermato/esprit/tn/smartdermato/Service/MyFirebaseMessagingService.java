package smartdermato.esprit.tn.smartdermato.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;

import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import smartdermato.esprit.tn.smartdermato.Activities.MessageActivity;
import smartdermato.esprit.tn.smartdermato.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String NOTIFICATION_CHANNEL_ID2 = "EDMTDev";
    NotificationManager notificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null)
        {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                sendNotificationAPI26(remoteMessage);
//            else
                sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String,String> data = remoteMessage.getData();
        String  title = data.get("title");
        String content = data.get("content");
        String receiver = data.get("user");

        System.out.println("receiverrrrrrrrrrr:  "+receiver);


         notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Smart Dermato";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notification",NotificationManager.IMPORTANCE_MAX);

            notificationChannel.setDescription("Smart Dermato channel app test FCM");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);

        }
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, MessageActivity.class);
        resultIntent.putExtra("user_firebase_id",Integer.valueOf(receiver));

// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.userprofile);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setTicker("Hearty365")
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(icon)
                .setContentIntent(resultPendingIntent)
                .setContentInfo("info");
//        for (int i = 0;i<5;i++){
//            SystemClock.sleep(2000);
//            notificationManager.notify(i,notificationBuilder.build());
//        }
        notificationManager.notify(1,notificationBuilder.build());

    }
    public void sendOnChannel2(View v){
        Notification notification = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID2)
                .setSmallIcon(R.drawable.smart_demato_logo_mini)
                .setContentTitle("Title")
                .setContentText("Message")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        for (int i = 0;i<5;i++){
            SystemClock.sleep(2000);
            notificationManager.notify(i,notification);
        }
    }
}
