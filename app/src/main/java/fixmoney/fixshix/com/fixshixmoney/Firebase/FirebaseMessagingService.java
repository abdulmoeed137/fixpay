package fixmoney.fixshix.com.fixshixmoney.Firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import fixmoney.fixshix.com.fixshixmoney.Activities.DashboardActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.TermsAndConditionsActivity;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;

/**
 * Created by AbdulMoeed on 8/13/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (new SessionManager(this).CheckIfSessionExist())
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {

        Intent i = new Intent(this,DashboardActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FixPay")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_logo)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());

    }
}
