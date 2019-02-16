package automan.automanclientsmobile_android.CONTROLLER;

import android.content.Context;
import android.media.RingtoneManager;
import android.util.Log;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by F on 6/11/2017.
 */
public class SettingsHandler {

    private Context context = null;

    private JSONObject settings = null;

    public JSONObject settings() {
        return this.settings;
    }

    public void settings(JSONObject u) {
        this.settings = u;
    }

    //
    private JSONObject defaultSettings = null;

    public JSONObject defaultSettings() {
        return this.defaultSettings;
    }

    public void defaultSettings(JSONObject u) {
        this.defaultSettings = u;
    }

    //
    private JSONObject dashboardSettings = null;

    public JSONObject dashboardSettings() {
        return this.dashboardSettings;
    }

    public void dashboardSettings(JSONObject u) {
        this.dashboardSettings = u;
        Log.e("SAVED DASH SETTINGS", this.dashboardSettings.toString());
    }

    //
    private JSONObject clientsSettings = null;

    public JSONObject clientsSettings() {
        return this.clientsSettings;
    }

    public void clientsSettings(JSONObject u) {
        this.clientsSettings = u;
    }

    public SettingsHandler(Context c) {
        this.context = c;
    }

    ///////////////////////////////////////////////////////////////////////////

    public void setUpDashboardSettings(JSONObject y) {
        try {
            this.dashboardSettings(y);

            // FIRST SETUP DEFAULT SETTINGS OBJECT
//            this.clientsSettings(y.getJSONObject("clientsSettings"));
//            //NOW EDIT PERSONAL SETTINGS BASED ON DASHBOARD SETTINGS
//            this.changeSettingsBasedOnDashboardSettings();
//            // NOW WORK WITH CLIENTS' DEFAULT SETTINGS
//            this.defaultSettings(this.clientsSettings().getJSONObject("defaultSettings"));
//            //  NOW SET DEFAULT VALUES FOR MOBILE APP
//            this.defaultSettings.put("gringtone", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//            // SET settings TO SETTINGS PREFS OR SET TO defaultSettings
//            this.settings = new JSONObject(AutomanApp.getApp().prefs().getString("settings", this.defaultSettings.toString()));
//        } catch (JSONException e) {
//            Log.e("ERROR:", "Error obtaining settings from preferences");
        } catch (Exception e) {
            Log.e("ERROR:", "Error obtaining settings from preferences");
        }
    }

    private void changeSettingsBasedOnDashboardSettings() {
        try {
            JSONObject obj = null;
            //COMPANY INFORMATION
//            obj = this.dashboardSettings.getJSONObject("Profiles").getJSONObject("Company Profile");
//            AutomanApp.companyId = obj.getString("_id");
//            AutomanApp.companyName= obj.getString("name");
//            AutomanApp.companyRegisteredName = obj.getString("registered_name");
//
//            // CHECK CLIENTS FUNCTIONALITY, AND SET ALLOWANCE SETTINGS
//            if(!this.clientsSettings.getBoolean("clientsFunctionality")){
//                // SHUTDOWN THE MOBILE APP
//                AutomanApp.getApp().showToast("Sorry, this application's functionality has been turned off");
//                AutomanApp.getApp().showToast("Logging out...");
//                AutomanApp.getApp().server().logout();
//            }
//
//            // ALLOWANCE SETTINGS
//            if (!this.clientsSettings.getJSONObject("allowanceSettings").getBoolean("personalAllowance")) { // IF FALSE (OFF)
//                this.settings.put("personalMode", false);
//                this.settings.put("showLocation", true);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSettings() {
        // FIRST GET ALL SETTINGS PREFERENCES INTO THE this.settings JSONOBJECT
        AutomanApp.getApp().prefeditor().putString("settings", this.settings.toString());
        AutomanApp.getApp().prefeditor().commit();
    }

    public void resetSettings() {
        // FIRST GET ALL SETTINGS PREFERENCES INTO THE this.settings JSONOBJECT
        try {
//            this.settings(null);
//            this.settings = new JSONObject(AutomanApp.getApp().prefs().getString("settings", this.defaultSettings.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  DASHBOARD SETTINGS ARRAYS
    public ArrayList<String> dashboardSettingsArray(String option) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            JSONArray arr = null;
            switch (option) {
                case "gender":
                    for (String str : Arrays.asList("Male", "Female")) {
                        list.add(str);
                    }
                    return list;
                case "contactMethods":
                    arr = (JSONArray) this.getDashboardValue("contactMethodOptions");
                    break;
                case "stockNames":
                    arr = new JSONArray ("[\"AADS\", \"ACCESS\", \"ACI\", \"ADB\", \"AGA\", \"ALW\", \"AYRTN\", \"BOPP\", \"CAL\", \"CLYD\", \"CMLT\", \"CPC\", \"EGH\", \"EGL\", \"ETI\", \"FML\", \"GCB\", \"GGBL\", \"GLD\", \"GOIL\", \"GSR\", \"GWEB\",\n" +
                            "\"HFC\", \"HORDS\", \"IIL\", \"MAC\", \"MLC\", \"MMH\", \"PBC\", \"PKL\", \"PZC\", \"SAMBA\", \"SCB\", \"SCBPREF\", \"SIC\", \"SOGEGH\", \"SPL\", \"SWL\", \"TBL\", \"TLW\", \"TOTAL\", \"TRANSOL\", \"UNIL\"]");
                break;
                    default:
                    arr = (JSONArray) this.getDashboardValue(option);
                    break;
            }
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //      FUNCTIONS TO ACCESS DASHBOARD & CLIENTS SETTINGS

    //  DASHBOARD
    private Object getDashboardValue(String key) {
        return this.getValue(key, this.dashboardSettings);
    }

    public String getDashboardDefaultValue(String option) {
        switch (option) {
            default:
                break;
        }
        return (String) this.getDashboardValue(option.concat("Default"));
    }

    public ArrayList<String> getAutoAuditAutoEventOptions(String autoaudit) {
        /*
        if(angular.isDefined($rootScope.companySetting)){
            var autoaudits = $rootScope.companySetting.AUTO_AUDITINGSettings.autoaudits;
            for(var x of ["Internal", "External"]){
                if(autoaudits[x].hasOwnProperty(autoaudit)){
                    return autoaudits[x][autoaudit].autoeventOptions;
                }
            }
        }
        console.log("COMPANY SETTINGS NOT DEFINED SO CANNOT GET AUTO-EVENT OPTIONS ...")
        return [];
        */
        if ((autoaudit != null) && (autoaudit.length() > 0)) {
            try {
                Log.e("AUTO-AUDIT", "SELECTED -> " + autoaudit);
                JSONObject autoaudits = ((JSONObject) this.getDashboardValue("AUTO_AUDITINGSettings")).getJSONObject("autoaudits");
                List<String> arr = Arrays.asList("Internal", "External");
                JSONArray jsonarr = null;
                ArrayList<String> finalarr = new ArrayList<>();
                for (String str : arr) {
                    if (autoaudits.getJSONObject(str).has(autoaudit)) {
                        jsonarr = autoaudits.getJSONObject(str).getJSONObject(autoaudit).getJSONArray("autoeventOptions");
                        break;
                    }
                }
                if (jsonarr != null) {
                    Log.e("NOTICE", "FOUND AUTO-EVENT OPTIONS -> " + jsonarr.toString());
                    for (int i = 0; i < jsonarr.length(); i++) {
                        finalarr.add(jsonarr.getString(i));
                    }
                    Log.e("NOTICE", "RETURNING FINAL ARRAY -> " + finalarr.size());
                    return finalarr;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<String>();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///   FUNCTIONS TO ACCESS MOBILE APP PERSONAL SETTINGS
    public Object getValue(String key) {
        return this.getValue(key, null);
    }

    public Object getValue(String key, JSONObject json) {
        if (key.length() > 0) {
            try {
                JSONObject nextJson = null;
                String nextKey = "";
                Object value = null;
                if (json == null) json = this.dashboardSettings;
                if (key != null && json != null) {
                    if (json.has(key)) return json.get(key);
                    Iterator<String> i = json.keys();
                    while (i.hasNext()) {
                        try {
                            nextKey = i.next(); // THIS MIGHT END UP SKIPPING THE FIRST KEY
                            nextJson = json.getJSONObject(nextKey); // THIS MIGHT NOT BE A JSONOBJECT, SO BE CAREFUL
                            if (nextJson.has(key)) return nextJson.get(key);
                            // IF NOT THE CALL THIS FUNCTION RECURSIVELY
                            if ((nextJson instanceof JSONObject) && (nextJson != null) &&
                                    (nextJson.length() > 0)
//                                && !(nextJson instanceof JSONArray)
                                    ) {
                                value = getValue(key, nextJson);
                                if (value != null) return value;
                                // IF IT WAS NULL, THEN JUST MOVE ON TO THE NEXT OBJECT
                            }
                        } catch (JSONException e) {
//                        Log.e("NOTICE", nextKey + " CANNOT BE CONVERTED TO A JSON OBJECT");
                            continue; // IF THAT WASN'T A JSONOBJECT, LOOP JUST CONTINUES TO NEXT KEY
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///     FUNCTIONS THAT ACCESS BOTH DASHBOARD, CLIENTS & MOBILE APP'S PERSONAL SETTINGS
}
