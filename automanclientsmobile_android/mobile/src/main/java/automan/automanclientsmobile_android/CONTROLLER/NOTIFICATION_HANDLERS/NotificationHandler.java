package automan.automanclientsmobile_android.CONTROLLER.NOTIFICATION_HANDLERS;

import android.R;
import android.app.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
//import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.MeetingDetails;
//import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.ProjectDetails;
//import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.TaskDetails;
//import automan.automanclientsmobile_android.VIEW.HOME.Home;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.meetings;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.projects;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.tasks;

/**
 * Created by F on 4/26/2017.
 */
/*
            OBJECTS THAT HAVE TO DO WITH PERSONAL NOTIFICATIONS:
          PROJECT - startd, deadline
          TASK - startd, deadline
          MEETING - startd, endd

     */
//class NotificationObject {
//    public enum ObjectType {
//        projectStart, projectEnd,
//        taskStart, taskEnd,
//        meetingStart, meetingEnd,
//        productRelease;
//
//        public String toString(ObjectType sth) {
//            switch (sth) {
//                case projectStart: return "Project starts ";
//                case projectEnd: return "Project ends ";
//                case taskStart: return "Task starts ";
//                case taskEnd: return "Task ends ";
//                case meetingStart: return "Meeting starts ";
//                case meetingEnd: return "Meeting ends ";
//                default: return "";
//            }
//        }
//    }
//
//    public ObjectType type;
//    public int id = 0;
//    public Date dueDate = null;
//    private Object obj = null;
//
//    public Object obj() {
//        return this.obj;
//    }
//
//    public NotificationObject(ObjectType type, int id, Date dueDate, Object obj) {
//        this.type = type;
//        this.id = id;
//        this.dueDate = dueDate;
//        this.obj = obj;
//    }
//}
//// SAMPLE INITIALIZATION - new NotificationObject(NotificationObject.ObjectType.projectStart, id, dueDate, obj);
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////  THIS SERVICE CALLS THE BROADCAST RECEIVER TO FIRE NOTIFICATION
//class NotificationService extends Service {
//
//    private final int UPDATE_INTERVAL = 60 * 1000; // EVERY 24 HOURS
//    private Timer timer = new Timer();
//    private static final int NOTIFICATION_EX = 1;
//
//    public NotificationService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        // code to execute when the service is first created
//        super.onCreate();
//        Log.i("MyService", "Service Started.");
//        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>12) {
//            (new NotificationHandler(getApplicationContext())).dailyNotificationCheckUP();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        if (timer != null) {
//            timer.cancel();
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startid){
//        return START_STICKY;
//    }
//
//    private void stopService() {
//        if (timer != null) timer.cancel();
//    }
//}
//  SAMPLE USAGE -> startService(new Intent(getApplicationContext(), NotificationService.class));

public class NotificationHandler {

    private Context context = null;
//    private Intent i = null;
//    private PendingIntent pi = null;
//    private static final String VOICE_REPLY_CONSTANT = "VOICE INPUT";
//
//    private TaskStackBuilder stackBuilder = null;
//    private Notification noti = null;
//    private NotificationManager notiMAN = null;
//    private NotificationCompat.BigTextStyle notiStyle = null;
//    private NotificationCompat.Builder notiBuilder = null;
//    private NotificationCompat.WearableExtender notiWearExtender = null;

    public NotificationHandler(Context c) {
        this.context = c;
//        this.setupPersonalNotificationHandler();
        this.setupOneSignal();
    }

    public void setupOneSignal(){
        OneSignalHandler.get().setupOneSignal(this.context);
    }
    public void sendTagToOneSignal(){
        OneSignalHandler.get().sendTag();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    ////        PERSONAL NOTIFICATIONS - TRIGGERED WITH MOBILE APP ITSELF
    ////        NOTIFICATIONS APPEAR BOTH IN MOBILE & WATCH APPS

//    private void addActionButtons(PendingIntent pi){
//
//        RemoteInput remoteInput = null; // FOR VOICE ACTION INPUT
//        remoteInput = new RemoteInput.Builder(VOICE_REPLY_CONSTANT)
//                .setLabel("Speak now!")
//                .build();
//        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
//                R.drawable.ic_menu_notification, "Action text", pi
//        ).addRemoteInput(remoteInput).build();
//        this.notiWearExtender.addAction(action);
//        /* ADDING ACTION INT THE WATCH APP, BUT FIRST YOU MUST TURN OFF THE
//         .setContentIntent(this.pi) TO REPLACE THAT FUNCTIONALITY WITH THIS ONE!
//          & IN THE DESTINATION ACTIVITY, YOU CAN'T GET BUNDLE (EXTRAS) DATA, THROUGH THE NORMAL
//         WAY (COZ YOU USED A REMOTE INPUT)! THEREFORE USE THIS METHOD OVER THERE:
//        Bundle ri = RemoteInput.getResultsFromIntent(getIntent());
//        if(ri != null){ // THIS WILL RETURN A STRING VALUE OF THE VOICE INPUT
//            String voiceinput = ri.getCharSequence(NotificationHandler.VOICE_REPLY_CONSTANT).toString();
//        } */
//
//        this.notiBuilder.addAction("icon INTEGER id", "Title", pi);
//    }
//
//    private void addWearableFeatures(){
//        // SETTING BACKGROUND OF WATCH
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
//                getResources().getIdentifier("name of img", "drawable",
//                        getPackageName()));
//        this.notiWearExtender.setBackground(bmp);
//
//        // ADD MULTIPLE PAGES TO THE WATCH NOTIFICATION
//        List<Notification> notis = new List<Notification>();
//        this.notiWearExtender.addPages(notis); // OR CALL .addPage
//    }
//
//    private void setupPersonalNotificationHandler() {
//        try {
//            this.notiMAN = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
//            this.noti = new Notification();
//            this.notiStyle = new NotificationCompat.BigTextStyle();
//            this.notiWearExtender = new NotificationCompat.WearableExtender();
//            this.notiBuilder = new NotificationCompat.Builder(this.context)
//                    // SET THESE OPTIONS BASED ON MOBILE APP'S PERSONAL SETTINGS
//                    .setWhen(System.currentTimeMillis())
//                    // SET WHEN NOTIFICATION HAS OCCURED, THEY'RE SORTED BY THIS TIME
//                    .setShowWhen(true)
//                    .setAutoCancel(true)
//                    .setOnlyAlertOnce((Boolean)AutomanApp.getApp().getSettingsValue("galertOnce"))
//                    .setSound((Boolean)AutomanApp.getApp().getSettingsValue("gsound"))
//                    .setVibrate((Boolean)AutomanApp.getApp().getSettingsValue("gvibrate"))
//                    .setLights((Boolean)AutomanApp.getApp().getSettingsValue("glights"))
//                    .setColor()
//                    .setVisibility(Notification.VISIBILITY_PUBLIC)
//                    // BACKGROUND OF THIS ICON BECOMES BACKGROUND OF THE WATCH APP
//                    .setSmallIcon(R.drawable.ic_menu_notification)
//                    .setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.mipmap.ic_launcher))
//                    .extend(this.notiWearExtender)
//            // THESE ARE COMMENTED COZ THEY'RE ALREADY HANDLED DOWN THERE (WITH SETTINGS FUNCTIONALITY)
////                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE |
////                            Notification.DEFAULT_SOUND | Notification.FLAG_ONLY_ALERT_ONCE)
//            ;
//
//            // The stack builder object will contain an artificial back stack for the
//            // started Activity.
//            // This ensures that navigating backward from the Activity leads out of
//            // your application to the Home screen.
//            this.stackBuilder = TaskStackBuilder.create(this.context);
//
//
//            // FINNALLY, START THE NotificationService
//            this.context.startService(new Intent(this.context, NotificationService.class));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void dailyNotificationCheckUP(){    // CALL THIS FUNCTION EVERY 24 HOURS
//        (new DailyNotificationCheckUP()).execute(projects);
//        (new DailyNotificationCheckUP()).execute(tasks);
//        (new DailyNotificationCheckUP()).execute(meetings);
//    }
//
//    public class DailyNotificationCheckUP extends AsyncTask<AutomanEnumerations, Integer, Boolean> {
//
//        public DailyNotificationCheckUP() {}
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(AutomanEnumerations... params) {
//            try {
//                switch(params[0]){
//                    case projects:
//                        for (int i = 0; i < AutomanApp.getApp().projects().size(); i++) {
//                            NotificationHandler.this.prepareNotification(new NotificationObject(NotificationObject.ObjectType.projectStart,
//                                    AutomanApp.getApp().projects().get(i).id(),
//                                    AutomanApp.getApp().projects().get(i).startd(),
//                                    AutomanApp.getApp().projects().get(i)));
//
//                            NotificationHandler.this.prepareNotification(new NotificationObject(NotificationObject.ObjectType.projectEnd,
//                                    AutomanApp.getApp().projects().get(i).id(),
//                                    AutomanApp.getApp().projects().get(i).deadline(),
//                                    AutomanApp.getApp().projects().get(i)));
//                        }
//                        break;
//                    case tasks:
//                        for (int i = 0; i < AutomanApp.getApp().tasks().size(); i++) {
//                            NotificationHandler.this.prepareNotification(new NotificationObject(NotificationObject.ObjectType.taskStart,
//                                    AutomanApp.getApp().tasks().get(i).id(),
//                                    AutomanApp.getApp().tasks().get(i).startd(),
//                                    AutomanApp.getApp().tasks().get(i)));
//
//                            NotificationHandler.this.prepareNotification(new NotificationObject(NotificationObject.ObjectType.taskEnd,
//                                    AutomanApp.getApp().tasks().get(i).id(),
//                                    AutomanApp.getApp().tasks().get(i).deadline(),
//                                    AutomanApp.getApp().tasks().get(i)));
//                        }
//                    case meetings:
//                        for (int i = 0; i < AutomanApp.getApp().meetings().size(); i++) {
//                            NotificationHandler.this.prepareNotification(new NotificationObject(NotificationObject.ObjectType.meetingStart,
//                                    AutomanApp.getApp().meetings().get(i).id(),
//                                    AutomanApp.getApp().meetings().get(i).startd(),
//                                    AutomanApp.getApp().meetings().get(i)));
//
//                            NotificationHandler.this.prepareNotification(new NotificationObject(NotificationObject.ObjectType.meetingEnd,
//                                    AutomanApp.getApp().meetings().get(i).id(),
//                                    AutomanApp.getApp().meetings().get(i).endd(),
//                                    AutomanApp.getApp().meetings().get(i)));
//                        }
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean res) {
//            super.onPostExecute(res);
//        }
//    }
//
//    //  THESE PROPERTIES WILL BE USED FOR THE NOTIFICATION'S PROPERTIES
//    private String groupKey = "group key depends on no. of days until due date",
//            ticker = "", contentTitle = "", contentText = "", subText ="", contentInfo = "", // notiBuilder PROPERTIES
//            summaryText = "", bigContextTitle = "", bigText = ""; // notiStyle PROPERTIES
//
//    private int daysToDueDate(Date dueDate){
//        Date now = new Date();
//        long difference = dueDate.getTime() - now.getTime();
//        if(difference < 0)
//            return -1;
//        else if(difference >= 0 && difference < 1)
//            return 0;
//        else return (int) difference / 1; // = difference / number of milliseconds that make a day :)
//    }
//
//    private Boolean handleDaysToDueDate(String info, Date date){
//        try {
//            int days = this.daysToDueDate(date);
//            groupKey = ""+days; // NO. OF DAYS UNTIL DUE DATE
//            switch(days){
//                // OVER HERE USE "interval" FROM NOTIFICATION SETTINGS OBJECT
//                case 0: // " today"
//                    // FIGURE OUT THE SPECIFIC PROPERTIES YOU NEED, AND DROP THE OTHERS
//                    ticker = info + "today";
//                    contentTitle = info + "today";
//                    contentText = info + "today";
//                    subText = info + "today";
//                    contentInfo = info + "today";
//                    summaryText = info + " today";
//                    bigContextTitle = info + "today";
//                    bigText = info + "today";
//                    break;
//                case 1: // " tomorrow"
//                    if(!((JSONObject)AutomanApp.getApp().getSettingsValue("ginterval")).getBoolean("1day")) return false;
//                    break;
//                case 2: // " next 2 days" or " in 2 days"
//                    break;
//                case 5: // " next 5 days" or " in 5 days"
//                    break;
//                case 7: // " next week" or " in a week"
//                    if(!((JSONObject)AutomanApp.getApp().getSettingsValue("ginterval")).getBoolean("1week")) return false;
//                    break;
//                case 14: // " next 2 weeks" or " in 2 weeks"
//                    break;
//                case 30: // " next month" or " in a month"
//                    if(!((JSONObject)AutomanApp.getApp().getSettingsValue("ginterval")).getBoolean("1month")) return false;
//                    break;
//                default:
//                    if(days < 0) { // PASSED DATE, THEREFORE USE " days ago"
//
//                    }
//                    return false;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    private void prepareNotification(NotificationObject o) {
//
//        // CHECK NotificationObject OBJECT'S OBJECT TYPE AND REFERENCE DATE,
//        // THEN SET Notification OBJECT'S PROPERTIES BASED ON DATA
//        // YOU AN USE A GENERIC FUNCTION TO ACQUIRE ANY DETAILS CLASS YOU WANT!
//        if(o.type.toString(o.type).contains("Project"))
//            this.i = new Intent(this.context, ProjectDetails.class);
//        else if(o.type.toString(o.type).contains("Task"))
//            this.i = new Intent(this.context, TaskDetails.class);
//        else if(o.type.toString(o.type).contains("Meeting"))
//            this.i = new Intent(this.context, MeetingDetails.class);
//        else if(o.type.toString(o.type).contains("AutoLog"))
//            this.i = new Intent(this.context, AutoLogDetails.class);
//        else if(o.type.toString(o.type).contains("AutoEvent"))
//            this.i = new Intent(this.context, AutoEventDetails.class);
//        else this.i = new Intent(this.context, Home.class);
//
//        if(this.handleDaysToDueDate(o.type.toString(o.type), o.dueDate)){
//            // SET THESE IN SPECIFIC FUNCTION CREATING THE SPECIFIC NOTIFICATION
//            this.notiStyle.setSummaryText(summaryText)
//                    .setBigContentTitle(bigContextTitle)
//                    .bigText(bigText);
//
//            this.notiBuilder
//                    .setTicker(ticker)
//                    .setContentTitle(contentTitle)
//                    .setContentText(contentText)
//                    .setSubText(subText)
//                    .setContentInfo(contentInfo)
//                    .setGroup(groupKey)
//                    .setOngoing(false) //   IF true , NOTIFICATION WON'T BE ABLE TO BE CANCELLED
//            ;
//            this.i.putExtra("id", o.id+""); // PUT IN THE ID OF THE OBJECT
//            this.firePersonalNotification(o.id); // OR INVOKE THE BROADCAST RECEVER
//        }
//    }
//
//    public void firePersonalNotification(int notiId) {
//
//        // Adds the back stack for the Intent (but not the Intent itself)
//        this.stackBuilder.addParentStack(Home.class);
//        // Adds the Intent that starts the Activity to the top of the stack
//        this.stackBuilder.addNextIntent(this.i);
//        this.pi = this.stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        this.notiBuilder.setContentIntent(this.pi)
//                .setStyle(this.notiStyle)
//                // Add an Action Button below Notification
//                .addAction(R.drawable.ic_launcher, "View Details", this.pi);
//        // Creating a notification from the notification builder
//        this.noti = this.notiBuilder.build();// RECEIVE WHATEVER NOTIFICATION THAT IS TO BE FIRED
//
//        /////////////////////////////////////////////////////////////////
//        // SET THESE OPTIONS BASED ON MOBILE APP'S PERSONAL SETTINGS
//        if((Boolean)AutomanApp.getApp().getSettingsValue("glights"))
//            this.noti.defaults |= Notification.DEFAULT_LIGHTS;
//        if((Boolean)AutomanApp.getApp().getSettingsValue("gvibrate"))
//            this.noti.defaults |= Notification.DEFAULT_VIBRATE;
//        if((Boolean)AutomanApp.getApp().getSettingsValue("gsound"))
//            this.noti.defaults |= Notification.DEFAULT_SOUND;
//        if((Boolean)AutomanApp.getApp().getSettingsValue("galertOnce"))
//            this.noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
//
//        // Fire notification
//        if((Boolean)AutomanApp.getApp().getSettingsValue("gnotifications"))
//            this.notiMAN.notify(notiId, this.noti);
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    ////        EXTRA METHODS WHICH CAN BE USED LATER ON!!!

    /*
    //////////////////////////////////////////////////////////////////////////////////
    ////        NOTIFICATION BROADCAST RECEIVER CLASS
    //  USE THIS TO START UP THE BROADCAST RECEIVER
    Intent intent = new Intent(getApplicationContext() , NotificationBroadcastReceiver.class);
    //  OR
    Intent switchIntent = new Intent(BROADCAST_ACTION);

    PendingIntent pendingIntent  = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    start(intent);

     // DO THIS IN MANIFEST FILE
    <receiver android:name=".NotificationBroadcastReceiver" android:enabled="true" android:exported="false" >
        <intent-filter>
            <action android:name="your package.ANY_NAME" />
        </intent-filter>
    </receiver>

    public class NotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(getClass().getSimpleName(), "in receiver");
            if (intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false)) {
                Log.d(getClass().getSimpleName(), "entering receiver");
            } else {
                Log.d(getClass().getSimpleName(), "exiting receiver");
            }

            if (intent.getAction().equals("your package.ANY_NAME")) {
                //your code here
            }

            // U CAN CALL this.firePersonalNotification(); HERE
        }
    }

    private PackageManager pm = null;
    private ComponentName cn = null;

    public void switchOnBroadcastReceiver() {
        pm = this.context.getPackageManager();
        cn = new ComponentName(this.context, NotificationBroadcastReceiver.class);
        pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public void switchOffBroadCastReceiver() {
        pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
    */
}
