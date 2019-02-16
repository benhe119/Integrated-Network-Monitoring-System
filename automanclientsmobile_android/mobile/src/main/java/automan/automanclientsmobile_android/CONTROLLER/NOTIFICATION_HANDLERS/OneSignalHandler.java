package automan.automanclientsmobile_android.CONTROLLER.NOTIFICATION_HANDLERS;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSNotificationPayload;
import com.onesignal.OneSignal;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.CONTROLLER.NOTIFICATION_HANDLERS.OneSignalHandler;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.Home;
import automan.automanclientsmobile_android.VIEW.STARTUP_VIEWS.Login;

import org.json.JSONObject;

/**
 * Created by F on 7/30/2017.
 */
public class OneSignalHandler {
    private String title, message = "";
    private Intent i = null;
    private Context context = null;

    private static OneSignalHandler oneSignal = new OneSignalHandler();

    public static OneSignalHandler get() {
        return oneSignal;
    }

    public void setupOneSignal(Context c) {
        this.context = c;
        OneSignal.startInit(this.context)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                // NOT SURE IF THIS ONE'S EVEN NECESSARY
//                .setNotificationExtenderService(new ExampleNotificationExtenderService())
                .autoPromptLocation(true)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        this.sendTag();
    }

    public void sendTag() {
        OneSignal.setSubscription(true); // ALLOW YOUR USERS TO RECEIVE NOTIFICATIONS WHENEVER POSSIBLE
        if (AutomanApp.getApp().getUser() != null) {
            // AT THIS STAGE, USER MIGHT NOT BE INITIALIZED YET
            Log.e("SENDING ONESIGNAL TAG", AutomanApp.getApp().getUser().id() + "");
            OneSignal.sendTag("user._id", AutomanApp.getApp().getUser().id() + "");
            // Call syncHashedEmail anywhere in your app if you have the user's email.
            // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
            OneSignal.syncHashedEmail(AutomanApp.getApp().getUser().email());
            Log.e("OneSignal Handler -> ", "User Object is not null, ID tag sent ...");
        } else Log.e("OneSignal Handler -> ", "User Object is null, ID cannot be sent ...");
    }

    private void handleNotificationAdditionalData(JSONObject data) {
        try {
            // PROCESS JSON DATA
            OneSignalHandler.this.title = data.optString("title", "");
            OneSignalHandler.this.message = data.optString("message", "");

            Log.e("ONESIGNAL", "NOTIFICATION OPENED HANDLER FUNCTION!!!");
            // CHECK FOR ACCESS_TOKEN AND USER OBJECT
            if (AutomanApp.getApp().isLoggedIn()) {
                Log.e("ONESIGNAL", "USER ALREADY LOGGED IN!!!");
                AutomanApp.getApp().getData("myData");
                Class gotoActivity = Home.class; // FIRST SET INTENT UP FOR HOME ACTIVITY
                OneSignalHandler.this.i = new Intent(OneSignalHandler.this.context, gotoActivity);
                if (data.has("object")) {
                    String str = data.getJSONObject("object").optString("autoEnum", "");
                    if (str.length() > 0) {
                        Log.e("ONESIGNAL", "NOTIFICATION DATA HAS AUTO-ENUMERATION!!!");
                        AutomanEnumerations autoEnum = AutomanEnumerations.toAutoEnum(str);
                        if (autoEnum != null) {
                            str = autoEnum.toString() + "Details";
                            Log.e("ONESIGNAL", "NOTIFICATION HEADING INTO '" + str + "' !!!");
                            gotoActivity = Class.forName("automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS." + str);
                            if (gotoActivity != null) {
                                Log.e("ONESIGNAL", "NOTIFICATION STARTING DETAILS ACTIVITY NOW!!!");
                                OneSignalHandler.this.i = null; // NOW SET INTENT UP FOR DETAILS ACTIVITY OF AUTO-ENUMERATION
                                OneSignalHandler.this.i = new Intent(OneSignalHandler.this.context, gotoActivity);
                                OneSignalHandler.this.i.putExtra("id", data.getJSONObject("object").getString("id"));
                            }
                        }
                    }
                }
            } else OneSignalHandler.this.i = new Intent(OneSignalHandler.this.context, Login.class);

            // The following can be used to open an Activity of your choice.
            OneSignalHandler.this.i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            OneSignalHandler.this.context.startActivity(OneSignalHandler.this.i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey = "";
            Log.e("ONESIGNAL", "NOTIFICATION OPENED!!!");

            try {
                if (data != null) {
                    customKey = data.optString("customkey", null);
                    if (customKey != null)
                        Log.i("OneSignalExample", "customkey set with value: " + customKey);

                    OneSignalHandler.this.handleNotificationAdditionalData(data); // HANDLE ADDITIONAL DATA

                }

                if (actionType == OSNotificationAction.ActionType.ActionTaken)
                    Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String customKey;

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
        }
    }

//    public class ExampleNotificationExtenderService extends OneSignal.NotificationExtenderService {
//        // YOU CAN USE THIS CLASS TO CONFIGURE THE ONE SIGNAL NOTIFICATIONS THAT COME
//        // AS IF THEY WERE FROM THE USUAL ANDROID NOTIFICATION API
//        private Notification noti;
//        private NotificationManager notiMAN;
//        private NotificationCompat.BigTextStyle notiStyle = null;
//        private NotificationCompat.Builder notiBuilder = null;
//        private TaskStackBuilder stackBuilder = null;
//        private Intent i = null;
//        private PendingIntent pi = null;
//
//        @Override
//        protected boolean onNotificationProcessing(OSNotificationPayload notification) {
//            Log.d(getClass().getName(), "Notification payload " + notification.message);
//            JSONObject data = notification.additionalData;
//            String customKey = "", title = "", message = notification.message;
//
//            try {
//
//                if (data != null) {
//                    customKey = data.optString("customkey", null);
//                    if (customKey != null)
//                        Log.i("OneSignalExample", "customkey set with value: " + customKey);
//
//                    OneSignalHandler.this.handleNotificationAdditionalData(data); // HANDLE ADDITIONAL DATA
//                    this.i = OneSignalHandler.this.i; // NOW COPY MAIN INTENT TO THIS CLASS'S INTENT
//
//                    if (notification != null) {
//                        notiMAN = (NotificationManager) OneSignalHandler.this.context.getSystemService(Context.NOTIFICATION_SERVICE);
//                        noti = new Notification();
//
//                        //
//                        this.notiMAN = (NotificationManager) OneSignalHandler.this.context.getSystemService(Context.NOTIFICATION_SERVICE);
//                        this.noti = new Notification();
//                        this.notiStyle = new NotificationCompat.BigTextStyle();
//                        this.notiStyle.setBigContentTitle(title);
//                        this.notiStyle.setSummaryText(message);
//                        this.notiStyle.bigText(message);
//                        this.notiBuilder = new NotificationCompat.Builder(OneSignalHandler.this.context)
//                                // SET THESE OPTIONS BASED ON MOBILE APP'S PERSONAL SETTINGS
//                                .setWhen(System.currentTimeMillis())
//                                // SET WHEN NOTIFICATION HAS OCCURED, THEY'RE SORTED BY THIS TIME
//                                .setShowWhen(true)
//                                .setAutoCancel(true)
//                                .setOnlyAlertOnce(true)
////                                .setOnlyAlertOnce((Boolean) AutomanApp.getApp().getSettingsValue("galertOnce"))
////                                .setSound((Boolean) AutomanApp.getApp().getSettingsValue("gsound"))
////                                .setVibrate((Boolean) AutomanApp.getApp().getSettingsValue("gvibrate"))
////                                .setLights((Boolean) AutomanApp.getApp().getSettingsValue("glights"))
//                                .setColor(getResources().getColor(R.color.holo_blue_dark))
//                                .setVisibility(Notification.VISIBILITY_PUBLIC)
//                                .setSmallIcon(R.drawable.ic_menu_notification)
//                                .setLargeIcon(BitmapFactory.decodeResource(OneSignalHandler.this.context.getResources(), R.mipmap.ic_launcher))
//                        // THESE ARE COMMENTED COZ THEY'RE ALREADY HANDLED DOWN THERE (WITH SETTINGS FUNCTIONALITY)
////                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE |
////                            Notification.DEFAULT_SOUND | Notification.FLAG_ONLY_ALERT_ONCE)
//                        ;
//
//                        // The stack builder object will contain an artificial back stack for the
//                        // started Activity.
//                        // This ensures that navigating backward from the Activity leads out of
//                        // your application to the Home screen.
//                        this.stackBuilder = TaskStackBuilder.create(OneSignalHandler.this.context);
//
//                        // Adds the back stack for the Intent (but not the Intent itself)
//                        this.stackBuilder.addParentStack(Home.class);
//                        // Adds the Intent that starts the Activity to the top of the stack
//                        this.stackBuilder.addNextIntent(this.i);
//                        this.pi = this.stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        this.notiBuilder.setContentIntent(this.pi)
//                                .setStyle(this.notiStyle)
//                                // Add an Action Button below Notification
//                                .addAction(R.drawable.ic_launcher, "View Details", this.pi);
//                        // Creating a notification from the notification builder
//                        this.noti = this.notiBuilder.build();// RECEIVE WHATEVER NOTIFICATION THAT IS TO BE FIRED
//
//                        /////////////////////////////////////////////////////////////////
//                        // SET THESE OPTIONS BASED ON MOBILE APP'S PERSONAL SETTINGS
//                        this.noti.defaults |= Notification.DEFAULT_LIGHTS;
//                        this.noti.defaults |= Notification.DEFAULT_VIBRATE;
//                        this.noti.defaults |= Notification.DEFAULT_SOUND;
//                        this.noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
////                        if ((Boolean) AutomanApp.getApp().getSettingsValue("glights"))
////                            this.noti.defaults |= Notification.DEFAULT_LIGHTS;
////                        if ((Boolean) AutomanApp.getApp().getSettingsValue("gvibrate"))
////                            this.noti.defaults |= Notification.DEFAULT_VIBRATE;
////                        if ((Boolean) AutomanApp.getApp().getSettingsValue("gsound"))
////                            this.noti.defaults |= Notification.DEFAULT_SOUND;
////                        if ((Boolean) AutomanApp.getApp().getSettingsValue("galertOnce"))
////                            this.noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
//
//                        // Fire notification
////                        if ((Boolean) AutomanApp.getApp().getSettingsValue("gnotifications"))
//                        this.notiMAN.notify(0, this.noti);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//    }

}
