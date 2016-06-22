package digimatic.shangcommerce.gcm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.AlertActivity;
import digimatic.shangcommerce.activity.AlertDetailActivity;
import digimatic.shangcommerce.activity.TransactionDetailActivity;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.NotificationExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.WishlistExecute;
import digimatic.shangcommerce.daocontroller.AlertNotifiedController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.NotificationController;
import digimatic.shangcommerce.daocontroller.WishlistController;
import greendao.AlertNotified;
import greendao.Customer;
import greendao.Notification;
import greendao.Wishlist;

public class GCMIntentService extends IntentService {
    //    private static final String TAG = GCMIntentService.class.getSimpleName();
    private static final String TAG = "register_notification";

//    Handler han;

//    public static final String INSERT = "insert";
//    public static final String UPDATE = "update";
//    public static final String DELETE = "delete";

    @Override
    public void onCreate() {
        super.onCreate();
//        han = new Handler();
    }

    public GCMIntentService() {

        super(Const.GCM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

        String messageType = googleCloudMessaging.getMessageType(intent);
        Log.e(TAG, messageType);
        Log.e(TAG, extras.getString("message", "no message"));
        Log.e(TAG, extras.getString("data", "no data"));

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                processNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                processNotification(extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                processNotification(extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        sendNotificationSample();
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void processNotification(final Bundle extras) {
        if (CustomerController.isLogin(GCMIntentService.this)) {
            if (CustomerController.getCurrentCustomer(GCMIntentService.this).getIs_notification_sound() == 1) {
                String message = extras.getString("message", "");
                try {
                    JSONObject data = new JSONObject(extras.getString("data"));
                    long type_id = data.getLong("type_id");
                    String type = data.getString("type");
                    if (type.equals("new_order")) {
                        sendNotificationTransactionDetail(GCMIntentService.this, message, type_id, (int) type_id);
                    } else if (type.equals("new_message")) {
                        sendNotificationAlertDetail(GCMIntentService.this, message, type_id, (int) type_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendNotificationTransactionDetail(Context ctx, String message, long type_id, int notifyId) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), TransactionDetailActivity.class);
        notificationIntent.setAction("shang_" + System.currentTimeMillis());
        notificationIntent.putExtra("entity_id", type_id);

//        // Construct a task stack.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
//
//        // Add the main Activity to the task stack as the parent.
//        stackBuilder.addParentStack(DealDetailActivity.class);
//
//        // Push the content Intent onto the stack.
//        stackBuilder.addNextIntent(notificationIntent);
//
//        // Get a PendingIntent containing the entire back stack.
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        // Define the notification settings.
        builder.setContentTitle("Shang").setContentText(message).setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_noti);
            builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_v2));
        } else {
            builder.setSmallIcon(R.drawable.icon_v2);
        }

        builder.setVibrate(new long[]{1000, 500});


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }

    public static void sendNotificationAlertDetail(Context ctx, String message, long type_id, int notifyId) {
        if (AlertNotifiedController.isExist(ctx, type_id)) {
            return;
        }
        AlertNotifiedController.insertOrUpdate(ctx, new AlertNotified(type_id));
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), AlertDetailActivity.class);
        notificationIntent.setAction("shang_" + System.currentTimeMillis());
        notificationIntent.putExtra("type_id", type_id);
        notificationIntent.putExtra("isFromNotification", true);

//        // Construct a task stack.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
//
//        // Add the main Activity to the task stack as the parent.
//        stackBuilder.addParentStack(DealDetailActivity.class);
//
//        // Push the content Intent onto the stack.
//        stackBuilder.addNextIntent(notificationIntent);
//
//        // Get a PendingIntent containing the entire back stack.
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        // Define the notification settings.
        builder.setContentTitle("Shang").setContentText(message).setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_noti);
            builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_v2));
        } else {
            builder.setSmallIcon(R.drawable.icon_v2);
        }

        builder.setVibrate(new long[]{1000, 500});


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }

    private void sendNotificationSample() {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(GCMIntentService.this, AlertActivity.class);

        // Get a PendingIntent containing the entire back stack.
        int i = new Random().nextInt(100);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(GCMIntentService.this, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMIntentService.this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.icon_v2)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                //.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher)).
                .setContentTitle("Shang").setContentText(i + ". This is test mesage from notification").setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        builder.setVibrate(new long[]{0, 2000});

        builder.setContentIntent(notificationPendingIntent);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) GCMIntentService.this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(i, builder.build());
    }
}
