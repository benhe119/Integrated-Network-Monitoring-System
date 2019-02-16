package automan.automanclientsmobile_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import automan.automanclientsmobile_android.CONTROLLER.*;
import automan.automanclientsmobile_android.CONTROLLER.NOTIFICATION_HANDLERS.NotificationHandler;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoAudit;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;
import automan.automanclientsmobile_android.MODEL.Stock;
import automan.automanclientsmobile_android.MODEL.USER_DATA.*;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA.GeneralAddEdit;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.Home;
import automan.automanclientsmobile_android.VIEW.STARTUP_VIEWS.Login;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.*;

/**
 * Created by mr.amo-addai on 1/3/18.
 */

public class AutomanApp extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String url = "https://automanghana.herokuapp.com";
    private static final String imgurl = "https://automanghana.herokuapp.com/file";

    public String url() {
        return url;
    }

    public String imgurl() {
        return imgurl;
    }

    private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

    public DateFormat getDF() {
        return df;
    }

    private static NotificationHandler notiHandler = null;

    public NotificationHandler notiHandler() {
        return notiHandler;
    }

    private static SettingsHandler settingsHandler = null;

    public SettingsHandler settingsHandler() {
        return settingsHandler;
    }

    private static AccessAutomanAPI server = null;

    public AccessAutomanAPI server() {
        Log.e("IS SERVER NULL? ->", (server == null) ? "true" : "false" + " " + server.toString());
        return server;
    }

    private static SharedPreferences prefs = null;
    private static SharedPreferences.Editor prefeditor = null;

    public SharedPreferences prefs() {
        return prefs;
    }

    public SharedPreferences.Editor prefeditor() {
        return prefeditor;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      DATA FOR APPLICATION

    // COMPANY INFORMATION - SHOULD BE EDITED ONCE THE SETTINGS HAS BEEN RETRIEVED
    public static String companyId = "",
            companyName = "",
            companyRegisteredName = "AUTOMAN",
            companyDetails = "",
            companyPhoneNumber = "",
            companyEmail = "",
            companyHomeAddress = "",
            companyPostalAddress = "";

    private static User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        user = u;
        Log.e("Set User", ((user != null && user.id() != null && user.id().length() > 0) ? user.id() : "NO USER"));
        this.setupMyData(); // CALL THIS WHENEVER YOU setUser()
        if (notiHandler != null) { // SEND USER_ID TAG TO ONE-SIGNAL TO SET IT UP
            notiHandler.sendTagToOneSignal();
        }
    }

    private static String accessToken = "";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String u) {
        accessToken = u;
        Log.e("Set Access Token", ((accessToken != null && accessToken.length() > 0) ? accessToken : "NO ACCESS TOKEN"));
    }

    private static String userEditAccessToken = "";

    public String getUserEditAccessToken() {
        return userEditAccessToken;
    }

    public void setUserEditAccessToken(String u) {
        userEditAccessToken = u;
        Log.e("Set Access Token", "USER EDIT -> " + ((userEditAccessToken != null && userEditAccessToken.length() > 0) ? userEditAccessToken : "NO USER-EDIT ACCESS TOKEN"));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MY DATA
    private static AutomanEnumerations MYDATA_STATE = MYDATA_USER;

    public AutomanEnumerations MYDATA_STATE() {
        return MYDATA_STATE;
    }

    public void MYDATA_STATE(AutomanEnumerations state) {
        if (!((state != AutomanEnumerations.MYDATA_USER)))
            MYDATA_STATE = state;
    }

    private static HashMap<AutomanEnumerations, ArrayList> userData = new HashMap<AutomanEnumerations, ArrayList>();

    public HashMap<AutomanEnumerations, ArrayList> myData() {
        switch (MYDATA_STATE) {
            case MYDATA_USER:
                return userData;
            default:
                return userData;
        }
    }

    public void myData(HashMap<AutomanEnumerations, ArrayList> x) {
        switch (MYDATA_STATE) {
            case MYDATA_USER:
                userData = x;
                break;
            default:
                userData = x;
                break;
        }
    }

    private Boolean JSONArrayContains(String sth, JSONArray arr) {
        try {
            for (int i = 0; i < arr.length(); i++) if (arr.getString(i).equals(sth)) return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setupMyData() {
        if (userData == null) userData = new HashMap<AutomanEnumerations, ArrayList>();
        if (user == null) {
            userData = null;
            userData = new HashMap<AutomanEnumerations, ArrayList>();
        } else {
            try {
                JSONObject current = null;
                //  NOW SET myData BASED ON USER'S TYPES
                //  USE .is_employee / .is_client / is_stakeholder PROPERTIES TOO
//                switch (user.types().getString(0)) {
//                    default:
//                        this.MYDATA_STATE(MYDATA_USER);
//                        break;
//                }
                this.MYDATA_STATE(MYDATA_USER);
                userData.put(AutomanEnumerations.autologs, this.autologs());
                userData.put(AutomanEnumerations.autoevents, this.autoevents());
                // NOT SURE IF THESE 2 ARE NECESSARY THOUGH
                userData.put(AutomanEnumerations.users, this.users());
                userData.put(AutomanEnumerations.stocks, this.stocks());
//            } catch (JSONException e) {
//                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // COMPANY DATA
    private static ArrayList<User> users = null;
    private static ArrayList<Stock> stocks = null;
    //  AUTO-AUDITING
    private static ArrayList<AutoLog> autologs = null;
    private static ArrayList<AutoEvent> autoevents = null;

    // CACHE DATA - USED WHEN NO COMPANY DATA IS AVAILABLE AND NO NETWORK CONNECTION TO DOWNLOAD COMPANY DATA
    private static HashMap<AutomanEnumerations, ArrayList> cacheData = new HashMap<AutomanEnumerations, ArrayList>();

    public HashMap<AutomanEnumerations, ArrayList> cacheData() {
        return cacheData;
    }

    public void cacheData(HashMap<AutomanEnumerations, ArrayList> x) {
        cacheData = x;
    }

    public void setupCacheData() {
        if (prefs.contains("cacheData") && (!this.prefs.getString("cacheData", "").isEmpty())) {
            // FIND A DIFFERENT WAY TO STORE THE CACHE DATA :'(PREFERENCES CANNOT STORE HASHMAPS) BEFORE YOU CAN GET IT WELL
//            this.cacheData(this.prefs.get("cacheData", ""));
        }
    }

    public void saveCacheData() {
        for (AutomanEnumerations key : cacheData.keySet()) {
            // SAVE CACHE DATA HERE -  FIND A WAY TO STORE ARRAYLISTS WITHIN PREFERENCES
//             this.prefeditor().put(key.toString(), this.cacheData.get(key));
        } // OR YOU CAN STORE THE WHOLE HASHMAP WITHIN THE PREFERENCES - FIND A WAY
//        this.prefeditor().putString("cacheData", this.cacheData.toString());
        this.prefeditor().commit();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static AutomanEnumerations CURRENT_STATE = AutomanEnumerations.DASHBOARD;

    public AutomanEnumerations CURRENT_STATE() {
        return CURRENT_STATE;
    }

    public void CURRENT_STATE(AutomanEnumerations state) {
        if (!((state != MYDATA) && (state != AutomanEnumerations.DASHBOARD)))
            CURRENT_STATE = state;
    }

    private static Boolean DASHBOARD = true;

    //THESE FUNCTIONS RETURN THE CURRENT DATA STRUCTURES BEING USED IN THE APP (SERVER(or CACHE)/DATABASE -> myData/companyData)
    private <T> ArrayList<T> getCurrentStateData(AutomanEnumerations opt, T o) {
        switch (CURRENT_STATE) {
            case DASHBOARD:
                DASHBOARD = true;
                break; // SO IN CASE IT WASN'T TURNED ON, TURN IT ON OVER HERE
            case MYDATA:
                return (ArrayList<T>) this.myData().get(opt);
        }
        return null;
    }

    public ArrayList<User> users() {
        return DASHBOARD ? users : this.getCurrentStateData(AutomanEnumerations.users, new User());
    }

    public ArrayList<Stock> stocks() {
        return DASHBOARD ? stocks : this.getCurrentStateData(AutomanEnumerations.stocks, new Stock());
    }

    //  AUTO-AUDITING

    public ArrayList<AutoLog> autologs() {
        return DASHBOARD ? autologs : this.getCurrentStateData(AutomanEnumerations.autologs, new AutoLog());
    }

    public ArrayList<AutoEvent> autoevents() {
        return DASHBOARD ? autoevents : this.getCurrentStateData(AutomanEnumerations.autoevents, new AutoEvent());
    }

    public <T> ArrayList<T> data(AutomanEnumerations sth) {
        ArrayList<T> arr = null;
        switch (sth) {
            case autologs:
                arr = (ArrayList<T>) this.autologs();
                break;
            case autoevents:
                arr = (ArrayList<T>) this.autoevents();
                break;
            case users:
                arr = (ArrayList<T>) this.users();
                break;
            case stocks:
                arr = (ArrayList<T>) this.stocks();
                break;
        }
        return arr;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static Activity currentActivity = null;

    public Activity getCurrentActivity() {
        Log.e("CURRENT ACTIVITY ->", (currentActivity != null) ? currentActivity.toString() : " IS NULL, SORRY :(");
        return currentActivity;
    }

    private static AutomanApp app = new AutomanApp();

    public static AutomanApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("ONCREATE FUNCTION", "THE APP HAS STARTED ...");

        this.setupPrefs();
        this.setupServer();
        this.setupNotificationHandler();
        this.setupSettingsHandler();
        this.setupCacheData();

        registerActivityLifecycleCallbacks(this);
    }

    public void setupPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefeditor = prefs.edit();
        if ((prefs != null) && (prefeditor != null))
            Log.e("PREFS NOT NULL -> ", prefs.toString() + " " + prefeditor.toString());
    }

    public void setupServer() {
        server = new AccessAutomanAPI(getApplicationContext());
        if (server != null) Log.e("Server not null ->", server.toString());
    }

    public void setupNotificationHandler() {
        notiHandler = new NotificationHandler(this);
    }

    public void setupSettingsHandler() {
        settingsHandler = new SettingsHandler(getApplicationContext());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS CREATED");
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS STARTED");
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS RESUMED");
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS PAUSED");
        currentActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS STOPPED");
        // don't clear current activity because activity may get stopped after
        // the new activity is resumed
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS INTANCE SAVED");
        //  DO NOTHING HERE ...
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("ACTIVITY -> ", activity.getLocalClassName() + " HAS DESTROYED");
        // don't clear current activity because activity may get destroyed after
        // the new activity is resumed
    }

    public void getData() {
        // FIRST SET UP THE PERSONAL DATABASE DATA
//        (new GetAllData("database", false)).execute();
        // IF ONLINE: GET SETTINGS, THEN AUTHORIZE WHETHER TO GET DASHBOARD DATA TOO, THEN GO TO HOME
        // IF NOT ONLINE: GET SETTINGS AND DASHBOARD DATA FROM CACHE
        if (this.isOnline()) // IF NETWORK IS AVAILABLE
            (new GetDashboardSettings(false)).execute(); // GET SETTINGS FIRST WITHOUT REFRESH (THEREFORE WILL GET DASHBOARD DATA TOO)
        else (new GetAllData("cache", false)).execute(); // IF NO NETWORK, GET DATA FROM CACHE
    }

    public void getData(String option) { // THIS IS FOR REFRESHING DATA
        if (!Arrays.asList("settings", "dashboard", "myData").contains(option)) {
            Log.e("error", "incorrect option");
            return;
        }
        // option is "database" / "dashboard" / "settings" / (IT CAN NEVER BE CACHE :')
//        if (option.equalsIgnoreCase("database")) {
//            (new GetAllData(option, true)).execute();
//        } else
        if (option.equalsIgnoreCase("settings")) {
            if (this.isOnline()) (new GetDashboardSettings(true)).execute();
        } else if (option.equalsIgnoreCase("dashboard") || option.equalsIgnoreCase("myData")) {
            if (this.isOnline()) (new GetAllData(option, true)).execute();
        } else Log.e("error", "incorrect input for refresh action");
    }

    //////////////////////////////////////////////////////////////////////////////
    //////////////          ASYNCTASK CLASSES

    public class GetDashboardSettings extends AsyncTask<AutomanEnumerations, Integer, Boolean> {
        private Boolean refresh = false;

        public GetDashboardSettings(Boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AutomanApp.server.showProgress(true);
        }

        @Override
        protected Boolean doInBackground(AutomanEnumerations... params) {
            AutomanApp.server.getDashboardSettings();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            AutomanApp.server.showProgress(false);
            if (!this.refresh) // ONLY GET ACTUAL DATA IF REFRESH IS NOT TRUE
                if (AutomanApp.this.isAuthorized("dashboardLogin")) {
                    (new GetAllData("dashboard", false)).execute();
                    // IF YOU DON'T WANT TO REFRESH SETTINGS, THEN YOU'RE AT THE SPLASH SCREEN
                    // SO THEN OBVIOUSLY, YOU DON'T WANT TO REFRESH THE DASHBOARD DATA TOO
                }
        }
    }


    public class GetAllData extends AsyncTask<AutomanEnumerations, Integer, Boolean> {
        private String option = "";
        private Boolean refresh = false;

        public GetAllData(String option, Boolean refresh) {
            this.option = option;
            this.refresh = refresh;
        }

        private void getCacheData() { //  ACCESS CACHE DATA FOR ALL
            //
            AutomanApp.this.users = AutomanApp.this.cacheData.get(AutomanEnumerations.users);
            AutomanApp.this.stocks = AutomanApp.this.cacheData.get(AutomanEnumerations.stocks);
            //  AUTO-AUDITING
            AutomanApp.this.autologs = AutomanApp.this.cacheData.get(AutomanEnumerations.autologs);
            AutomanApp.this.autoevents = AutomanApp.this.cacheData.get(AutomanEnumerations.autoevents);

            Log.e("CACHE", "DONE GETTING CACHE DATA");
        }

        private void getServerData() {
            //
            AutomanApp.users = AutomanApp.server.get(AutomanEnumerations.users, null);
            AutomanApp.stocks = AutomanApp.server.get(AutomanEnumerations.stocks, null);
            //  AUTO-AUDITING
            AutomanApp.autologs = AutomanApp.server.get(AutomanEnumerations.autologs, null);
            AutomanApp.autoevents = AutomanApp.server.get(AutomanEnumerations.autoevents, null);

            try {
                Log.e("SERVER", "DONE GETTING SERVER DATA");
                Log.e("SERVER", AutomanApp.stocks.size() + " stocks; " + AutomanApp.users.size() + " users; " + AutomanApp.autologs.size() + " autologs; " + AutomanApp.autoevents.size() + " autoevents;");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void getUserData() {
            AutomanApp.server.getUserData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!AutomanApp.getApp().isDBmode() && !Arrays.asList("database", "cache").contains(this.option) && !AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            AutomanApp.server.showProgress(true);
        }

        @Override
        protected Boolean doInBackground(AutomanEnumerations... params) {
            try {
                switch (this.option) {
                    case "database":
//                        this.getDBData();
//                        this.refresh = true;
                        // SET THIS TO TRUE COZ EVEN IF THIS WASN'T A REFRESH OPTION, WE DON'T WANT TO MOVE STRAIGHT TO HOME PAGE,
                        // WE WANT TO FIRST GET THE SETTINGS AND DASHBOARD DATA
                        // WE CANNOT START THE APP WITH JUST THE PERSONAL DATABASE DATA
                        break;
                    case "dashboard":
                        this.getServerData();
                        break;
                    case "cache":
                        this.getCacheData();
                        break;
                    case "myData":
                        this.getUserData();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            AutomanApp.server.showProgress(false);
            if (!this.refresh) { // IF refresh IS TRUE, DON'T DO SHIT (ELSE, GO TO NEXT ACTIVITY)
                Log.e("NOT A REFRESH", "MOVING INTO HOME ACTIVITY");
                Class x = res ? Home.class : Login.class;
                AutomanApp.this.getCurrentActivity().startActivity(new Intent(currentActivity, x));
            } else Log.e("JUST A REFRESH", "STAYING IN CURRENT ACTIVITY");
            // EVEN IF THIS IS INDEED A REFRESH, AND THERE'S SOME KIND OF ERROR,
            // THEN FIND OUT HOW TO KNOW IF ACCESSTOKEN HAS EXPIRED, THEN GO TO LOGIN
            if (!res)
                AutomanApp.this.getCurrentActivity().startActivity(new Intent(currentActivity, Login.class));
        }
    }

    //////////////////////////////////////////////////////
    ////  ADD TO SERVER
    public static class Add extends AsyncTask<AutomanEnumerations, Integer, Boolean> {

        private AutoObject obj;
        private Bitmap img = null;
        private Activity act = null;

        public Add(AutoObject obj, Bitmap img, Activity act) {
            this.obj = obj;
            if (act != null)
                this.act = act;
            if (img != null)
                this.img = img;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!AutomanApp.getApp().isDBmode() && !AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            AutomanApp.server.showProgress(true);
        }

        @Override
        protected Boolean doInBackground(AutomanEnumerations... params) {

            Log.e("ADD", params[0].toSingularUpperCase(params[0]) + " -> " + this.obj.toJSON(true, null).toString());

            try {
                if (AutomanApp.getApp().isDBmode()) {
                    /// FETCH DATA FROM PERSONAL DATABASE
                    switch (params[0]) {
                        default:
                            Log.e("err->", "Incorrect input");
                            break;

                    }
                    return true;
                } else {
                    if (AutomanApp.getApp().isOnline()) {
                        /// ADD DATA TO API DATABASE
                        return AutomanApp.server.add(params[0], (this.obj).toJSON(false, this.img));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                AutomanApp.getApp().showToast("Added");
                if (this.act != null) this.act.finish();
                // AutomanApp.this.getCurrentActivity().finish(); //  OR, YOU CAN DO IT THIS WAY INSTEAD :)
            } else {
                AutomanApp.getApp().showToast("Could not be added");
            }
            AutomanApp.server.showProgress(false);
        }
    }

    //////////////////////////////////////////////////////
    ////  EDIT IN SERVER
    public static class Edit extends AsyncTask<AutomanEnumerations, Integer, Boolean> {

        private String id;
        private AutoObject obj;
        private Bitmap img = null;
        private Activity act = null;

        public Edit(String id, AutoObject obj, Bitmap img, Activity act) {
            this.id = id;
            this.obj = obj;
            if (act != null)
                this.act = act;
            if (img != null)
                this.img = img;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!AutomanApp.getApp().isDBmode() && !AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            AutomanApp.server.showProgress(true);
        }

        @Override
        protected Boolean doInBackground(AutomanEnumerations... params) {

            Log.e("EDIT", params[0].toSingularUpperCase(params[0]) + " (" + this.id + ") -> " + this.obj.toJSON(true, null).toString());

            try {
                if (AutomanApp.getApp().isDBmode()) {
                    /// FETCH DATA FROM PERSONAL DATABASE
                    switch (params[0]) {
                        default:
                            Log.e("err->", "Incorrect input");
                    }
                    return true;
                } else {
                    if (AutomanApp.getApp().isOnline()) {
                        /// ADD DATA TO API DATABASE
                        return AutomanApp.server.edit(params[0], this.id, (this.obj).toJSON(true, this.img));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                AutomanApp.getApp().showToast("Saved");
                if (this.act != null) this.act.finish();
                // AutomanApp.this.getCurrentActivity().finish(); //  OR, YOU CAN DO IT THIS WAY INSTEAD :)
            } else {
                AutomanApp.getApp().showToast("Could not be edited");
            }
            AutomanApp.server.showProgress(false);
        }
    }

    ////  DELETE FROM DB IN SERVER
    public static class Delete extends AsyncTask<AutomanEnumerations, Integer, Boolean> {

        private String id;
        private Activity act = null;

        public Delete(String id, Activity act) {
            this.id = id;
            if (act != null)
                this.act = act;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!AutomanApp.getApp().isDBmode() && !AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            Log.e("DELETE ID", this.id);
            AutomanApp.server.showProgress(true);
        }

        @Override
        protected Boolean doInBackground(AutomanEnumerations... params) {
            try {
                if (AutomanApp.getApp().isDBmode()) {
                    switch (params[0]) {
                        default:
                            Log.e("err->", "Incorrect input");
                    }
                    return true;
                } else {
                    if (AutomanApp.getApp().isOnline())
                        return AutomanApp.server.delete(params[0], this.id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                AutomanApp.getApp().showToast("Deleted");
                if (this.act != null) this.act.finish();
                // AutomanApp.this.getCurrentActivity().finish(); //  OR, YOU CAN DO IT THIS WAY INSTEAD :)
            } else {
                AutomanApp.getApp().showToast("Could not be deleted");
            }
            AutomanApp.server.showProgress(false);
        }
    }

    ////  SPECIAL (SERVER / PERSONAL DATABASE) FUNCTION
    public static class SpecialFunction extends AsyncTask<String, Integer, Boolean> {

        private Activity act = null;
        private AutomanEnumerations sth = null;
        private String id = "", function = "";
        private Boolean isAuth = false;
        private JSONObject json = null;

        public SpecialFunction(AutomanEnumerations sth, String id, String function, JSONObject json, Activity act) {
            this.isAuth = false;
            if (AutomanApp.getApp().isAuthorized(function, sth.toSingularLowerCase(sth))) {
                this.sth = sth;
                this.id = id + "";
                this.function = function;
                if (json != null) this.json = json;
                if (act != null) this.act = act;
                this.isAuth = true;
                Log.e("SPECIAL FUNCTION", this.function + " " + this.sth.toSingularLowerCase(this.sth) + " : " + this.id);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!AutomanApp.getApp().isDBmode() && !AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            AutomanApp.server.showProgress(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if (this.isAuth) {
                    switch (this.function) {
                        case "handle":
                            return this.handleAutoLogOrAutoEvent(this.sth, this.id, this.json);
                        case "option":
                            return this.optionStock(this.sth, this.id, this.json);
                    }
                } else { // NOW CHECK FOR ACTIONS THAT DON'T NEED AUTHORIZATION
                    switch (this.function) {
                        case "userEditing":
                            return this.editUserData();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////  ACTUAL SPECIAL FUNCTIONS

        private Boolean handleAutoLogOrAutoEvent(AutomanEnumerations sth, String id, JSONObject obj) {
            if (AutomanApp.getApp().isDBmode()) {
                return false;
            } else {
                if (AutomanApp.getApp().isOnline())
                    return AutomanApp.server.handleAutoLogOrAutoEvent(sth, id, obj);
            }
            return null;
        }

        private Boolean optionStock(AutomanEnumerations sth, String id, JSONObject obj) {
            if (AutomanApp.getApp().isDBmode()) {
                return false;
            } else {
                if (AutomanApp.getApp().isOnline())
                    return true; // DON'T RUN THIS UNTIL FUNCTIONALITY HAS BEEN IMPLEMENTED WITHIN AUTO-API :)
//                    return AutomanApp.server.optionStock(sth, id, obj);
            }
            return null;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// /////////           PERSONAL DATABASE / DASHBOARD SERVER FUNCTIONS

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// /////////           PERSONAL DATABASE / DASHBOARD SERVER FUNCTIONS REQUIRING NO AUTHENTICATION

        private Boolean editUserData() {
            if (AutomanApp.getApp().isOnline()) return AutomanApp.server.editUserData();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result != null) {
                if (result) {
                    AutomanApp.getApp().showToast("Action completed");
                    if (this.act != null) this.act.finish(); // NO NEED TO CLOSE THE ACTIVITY DOE :)
                    // AutomanApp.this.getCurrentActivity().finish(); //  OR, YOU CAN DO IT THIS WAY INSTEAD :)
                } else {
                    AutomanApp.getApp().showToast("Action could not be performed");
                }
            }
            AutomanApp.server.showProgress(false);
        }
    }

    public static class ShowDialog {

        public static void showSpecialFunctionDialog(AutomanEnumerations sth, String id, String function, Activity act, String... params) {
            if (!Arrays.asList("handle", "option").contains(function))
                (new AutomanApp.SpecialFunction(sth, id, function, null, act)).execute(params);
            else { // SHOW THE REQUIRED DIALOG, COZ function IS WIHIN arr
                JSONObject json = null;

                (new AutomanApp.SpecialFunction(sth, id, function, json, act)).execute(params);
            }
        }

        // FOR ADD
//        public static void showShouldUpdateExtraDialog(final Person obj, final Bitmap img, final Activity act, final AutomanEnumerations... sth) {
//            AlertDialog.Builder d = new AlertDialog.Builder(currentActivity);
//            d.setCancelable(true);
//            d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    obj.shouldUpdateExtra(true);
//                    (new AutomanApp.Add(obj, img, act)).execute(sth);
//                }
//            });
//            d.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    obj.shouldUpdateExtra(false);
//                    (new AutomanApp.Add(obj, img, act)).execute(sth);
//                }
//            });
//            AlertDialog a = d.create();
//            a.setTitle("Do you also want to set this item's user data?");
//            a.show();
//        }
//
//        // FOR EDIT
//        public static void showShouldUpdateExtraDialog(final String id, final Person obj, final Bitmap img, final Activity act, final AutomanEnumerations... sth) {
//            AlertDialog.Builder d = new AlertDialog.Builder(currentActivity);
//            d.setCancelable(true);
//            d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    obj.shouldUpdateExtra(true);
//                    (new AutomanApp.Edit(id, obj, img, act)).execute(sth);
//                }
//            });
//            d.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    obj.shouldUpdateExtra(false);
//                    (new AutomanApp.Edit(id, obj, img, act)).execute(sth);
//                }
//            });
//            AlertDialog a = d.create();
//            a.setTitle("Do you also want to edit this item's user data?");
//            a.show();
//        }

        public static void showDeleteDialog(final AutomanEnumerations sth, final String id, final Activity act) {
            AlertDialog.Builder d = new AlertDialog.Builder(AutomanApp.getApp().getCurrentActivity());
            d.setCancelable(true);
            d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    (new AutomanApp.Delete(id, act)).execute(sth);
                }
            });
            d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog a = d.create();
            a.setTitle("Are you sure?");
            a.show();
        }

        public static void sendMessageDialog(AutomanEnumerations sth, String id, final AutoObject recipient) {
            if (!AutomanApp.getApp().isAuthorized("contact", sth.toSingularLowerCase(sth))) return;

        }

        public static void showLargeData(AutomanEnumerations opt, String title, Object data) {
            // THIS FUNCTION IS MAINLY USED TO SHOW A DIALOG CONTAINING LARGE DETAILS TEXT
        }

        public static void showChartDialog(final AutomanEnumerations sth, final String id, Object data, final Activity act) {

        }

        public static void showMapDialog(final AutomanEnumerations sth, final String id, Object data, final Activity act) {

        }

        public static void showAutoLogOrAutoEventDataDialog(final AutomanEnumerations sth, final String id, AutoAudit data, final Activity act) {
            try {
//                Dialog d = new Dialog();
//                JSONObject json = data.data();
//                switch (data.autoaudit()){
//                    case "Session Tracking":
//                        act.findViewById(R.id.user).setText(json.optString("user", "-"));
//                        // act.findViewById(R.id.user).setText(json.getJSONObject("user").optString("full_name", "-"));
//                        switch (sth) {
//                            case autologs:
//                                act.findViewById(R.id.sessionType).setText(json.optBoolean("login", true) ? "Login" : "Logout");
//                                act.findViewById(R.id.token).setText(json.getJSONObject("access_token").optString("token", "-"));
//                                act.findViewById(R.id.expiresInMinutes).setText(json.getJSONObject("access_token").optInt("expiresInMinutes", "-"));
//                                act.findViewById(R.id.device).setText(json.optString("device", "-"));
//                                act.findViewById(R.id.timestamp).setText(json.optString("timestamp", "-"));
//                                break;
//                            case autoevents:
//                                act.findViewById(R.id.sessionType).setText(json.optInt("logins", 0) + "Logins");
//                                act.findViewById(R.id.token).setText(json.getJSONArray("access_tokens").length + " access tokens");
//                                act.findViewById(R.id.device).setText(json.getJSONArray("devices").length + " devices");
//                                act.findViewById(R.id.timestamp).setText(json.getJSONArray("timestamps").length + " timestamps");
//                                break;
//                            default:
//                                return;
//                        }
//                        break;
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static ImageLoader il = null;

    public void getImage(AutomanEnumerations sth, String img_stub, Activity context, ImageView view) {
//        il = new ImageLoader(sth, context, AutomanApp.this.isDBmode());
//        view.setTag(img_stub != null ? img_stub : "some default img stub comes here");
//        il.displayImage(img_stub, context, view); // DON'T CALL THIS FOR NOW :)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////  FUNCTIONS
    public class LogoutAsync extends AsyncTask<String, Integer, Boolean> {

        private Activity act = null;

        public LogoutAsync(Activity act) {
            this.act = act;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            AutomanApp.getApp().server().showProgress(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return AutomanApp.getApp().server().logout();
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            AutomanApp.getApp().server().showProgress(false);
            if (res) {
                AutomanApp.getApp().showToast("Logging out");
                this.act.finish();
            } else {
                AutomanApp.getApp().showToast("Sorry, Cannot logout");
            }
        }
    }

    public void logout(Activity act) {
        // THIS SHOULD BE PUT WITHIN AN ASYNC-TASK
        (new LogoutAsync(act)).execute();
    }

    public void showToast(String s) {
        try {
            Toast.makeText(currentActivity, s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TOAST ERROR", e.toString());
        }
    }

    public String base64Encode(Bitmap b) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Boolean isOnline() {
        ConnectivityManager cman = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cman.getActiveNetworkInfo() != null && cman.getActiveNetworkInfo().isConnectedOrConnecting()) {
//            NetworkInfo[] info = cman.getAllNetworkInfo();
//            if (info != null){
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            } // OR YOU CAN ALSO JUST RETURN TRUE OVER HERE
            return true;
        }
        AutomanApp.getApp().showToast("No Internet Connection");
        return false;
    }

    public boolean isLoggedIn() {
        try {
            // CHECK FOR ACCESS_TOKEN AND USER & COMPANY OBJECTS
            if (AutomanApp.this.prefs().contains("access_token") && AutomanApp.this.prefs().contains("user") &&
                    AutomanApp.this.prefs().contains("company") &&
                    !(AutomanApp.this.prefs().getString("access_token", "").isEmpty()) &&
                    !(AutomanApp.this.prefs().getString("user", "").isEmpty()) &&
                    !(AutomanApp.this.prefs().getString("company", "").isEmpty())) {

                AutomanApp.this.setAccessToken(AutomanApp.this.prefs().getString("access_token", ""));
                AutomanApp.this.setUser((User) AutomanApp.getApp().jsonToClassObject(AutomanEnumerations.users,
                        new JSONObject(AutomanApp.this.prefs().getString("user", ""))));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isDBmode() {
        return false;
    }

//    public Object getSettingsValue(String key) {
//        Object obj = this.settingsHandler.getValue(key);
//        return obj == null ? this.settingsHandler.getDefaultValue(key) : obj;
//    }
//
//    public Boolean isUserEditingAllowed() {
//        if (this.settingsHandler.getAllowance("userEditing")) return true;
//        else AutomanApp.getApp().showToast("Sorry, you are not allowed to edit information");
//        return false;
//    }
//
//    public Boolean shouldRequestBeforeUserProfileEditing() {
//        return this.settingsHandler.shouldRequestBeforeUserProfileEditing();
//    }
//
//    public JSONArray getEditableUserProfileData() {
//        return this.settingsHandler.getEditableUserProfileData();
//    }
//
//    public JSONObject validateDataSecurity(AutomanEnumerations sth, String addedit, Object data) { // ADD MODE IF add == true
//        return null;
//    }

    public Boolean isAuthorized(String... params) {
        return true;
    }

//    public void contact(AutomanEnumerations type, String numberOrEmail) {
//        return;
//    }

    public <T> ArrayList<T> filter(ArrayList<T> arr, String query) {
        T o;
        ArrayList<T> filtered = new ArrayList<T>();
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            o = arr.get(i);
            if (o instanceof Person)
                str = ((User) o).fullName();
            //  AUTO-AUDITING
            if (o instanceof AutoLog)
                str = ((AutoLog) o).name();
            if (o instanceof AutoEvent)
                str = ((AutoEvent) o).name();
            if (o instanceof Stock)
                str = ((Stock) o).name(); // OR YOU CAN USE .stockName();

            if (str.contains(query))
                filtered.add(o);
        }
        return filtered;
    }

    public <T> T find(AutomanEnumerations option, String id) {
        try { //    INCORRECT find() FUNCTION COZ OF LOOP FOR ONLY .projects()
            ArrayList<AutoObject> arr = AutomanApp.getApp().data(option);


            Log.e(option.toSingularUpperCase(option), "FINDING OBJECT FROM " + arr.size() + " " + option.toString());

            for (AutoObject obj : arr) {
                if ((obj.id() + "").trim().equalsIgnoreCase(id))
                    return (T) obj;
            }
//            switch (option) {
//                case users:
//                    for (int i = 0; i < this.users().size(); i++)
//                        if ((this.users().get(i).id() + "").trim().equalsIgnoreCase(id))
//                            return (T) this.users().get(i);
//                    break;
//                case autologs:
//                    for (int i = 0; i < this.autologs().size(); i++)
//                        if ((this.autologs().get(i).id() + "").trim().equalsIgnoreCase(id))
//                            return (T) this.autologs().get(i);
//                    break;
//                case autoevents:
//                    for (int i = 0; i < this.autoevents().size(); i++)
//                        if ((this.autoevents().get(i).id() + "").trim().equalsIgnoreCase(id))
//                            return (T) this.autoevents().get(i);
//                    break;
//                default:
//                    Log.e("error", "error with find function");
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T jsonToClassObject(AutomanEnumerations type, JSONObject obj) {
        try {
            switch (type) {
                case users:
                    return ((T) (new User(obj.optString("_id", null), obj.optString("full_name", null), obj.optString("first_name", null),
                            obj.optString("last_name", null), obj.optString("other_names", null), obj.optString("title", null), obj.optString("gender", null),
                            obj.optInt("age"), obj.optString("details", null), obj.optString("phone", null), obj.optString("email", null), obj.optString("home_address", null), obj.optString("postal_address", null),
                            obj.optString("contact_method", null), obj.optJSONObject("datasecurity"),
                            obj.optJSONArray("meetings"), obj.optJSONArray("projects"), obj.optJSONArray("tasks"), obj.optJSONArray("accounts"),
                            obj.optJSONArray("banks"), obj.optJSONArray("companies"), df.parse(obj.optString("date_created")), obj.optString("image_stub", null), obj,
                            //
                            obj.optString("username", null), obj.optString("company_email", null), obj.optInt("levelOfSecurity"), obj.optString("userRole", null),
                            obj.optBoolean("available"), obj.optJSONArray("location"), obj.optJSONArray("types"), obj.optBoolean("is_employee"),
                            obj.optBoolean("is_client"), obj.optBoolean("is_stakeholder"), obj.optJSONObject("employee"), obj.optJSONObject("client"),
                            obj.optJSONObject("stakeholder"), obj.optJSONArray("privileges"), obj.optJSONObject("securitylogin"), obj.optBoolean("in_building"),
                            obj.optJSONArray("requests"), obj.optJSONArray("complaints"), obj.optJSONArray("enquiries"))));
                //  AUTO-AUDITING
                case autologs:
                    return ((T) (new AutoLog(obj.optString("_id", null), obj.optString("name", null), obj.optString("details", null),
                            obj.optString("type", null), obj.optString("source_type", null), obj.optString("source", null), obj.optString("autoaudit", null),
                            obj.optJSONObject("data"), obj.optJSONObject("datasecurity"), df.parse(obj.optString("date_created")), obj,
                            obj.optJSONArray("autoevents"))));
                case autoevents:
                    return ((T) (new AutoEvent(obj.optString("_id", null), obj.optString("name", null), obj.optString("details", null),
                            obj.optString("type", null), obj.optString("source_type", null), obj.optString("source", null), obj.optString("autoaudit", null),
                            obj.optJSONObject("data"), obj.optJSONObject("datasecurity"), df.parse(obj.optString("date_created")), obj,
                            obj.optString("autoevent", null), obj.optString("emergency_level", null), obj.optJSONArray("autologs"))));
                case stocks:
                    return ((T) (new Stock(obj.optString("_id", null), obj.optString("name", null), obj.optString("details", null),
                            obj.optString("stock_name", null), obj.optDouble("price", 0),
                            obj.optJSONObject("data"), obj.optJSONObject("datasecurity"), df.parse(obj.optString("date_created")), obj)));
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> ArrayList<T> jsonToClassArray(AutomanEnumerations type, JSONArray arr) {
        ArrayList<T> list = new ArrayList<T>();
        T obj = null;
        JSONObject j = null;
        Boolean contains = false;
        try {
            for (int i = 0; i < arr.length(); i++) {
                j = arr.getJSONObject(i);
                obj = (T) AutomanApp.getApp().jsonToClassObject(type, j);
                for (T o : list) {
                    if (((AutoObject) o).id().equalsIgnoreCase(j.optString("_id", ""))) {
                        Log.e("NOTICE", "OBJECT " + ((AutoObject) o).id() + " IS ALREADY AVAILABLE.");
                        contains = true;
                        break;
                    }
                }
                if (!contains) list.add(obj);
                Log.e(type.toPluralUpperCase(type) + " " + i, ((AutoObject) obj).id());
            }
            Log.e(type.toPluralUpperCase(type), "All " + list.size() + " " + type.toString() + " added to list");
        } catch (JSONException e) {
            Log.e("json error", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
