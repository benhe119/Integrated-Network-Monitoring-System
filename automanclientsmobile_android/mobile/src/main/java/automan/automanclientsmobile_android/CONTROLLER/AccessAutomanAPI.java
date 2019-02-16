package automan.automanclientsmobile_android.CONTROLLER;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import automan.automanclientsmobile_android.MODEL.*;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.USER_DATA.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import automan.automanclientsmobile_android.AutomanApp;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.*;

/**
 * Created by F on 10/13/2016.
 */
public class AccessAutomanAPI {

    private JSONObject json = null;
    private final String url = AutomanApp.getApp().url();
    private final String imgurl = AutomanApp.getApp().imgurl();
    private String result = "";
    private HttpURLConnection conn = null;
    private Context appContext = null;
    private ProgressDialog pd = null;
    private DateFormat df = AutomanApp.getApp().getDF();

    private JSONObject cond = null;

    public AccessAutomanAPI(Context c) {
        this.appContext = c;
    }

    public void showProgress(Boolean b) {
        Log.e("Showing Progress -> ", b ? "true" : "false");
        if (b) {
            if (AutomanApp.getApp().getCurrentActivity() != null) {
                Context context = AutomanApp.getApp().getCurrentActivity();
                Log.e("IS CONTEXT NULL? ->", (context == null) ? "true" : "false " + context.toString());
                if (pd == null && context != null) { // DON'T USE this.context, COZ IT'S THE APPLICATION'S CONTEXT
                    pd = new ProgressDialog(context); // U MUST USE THE CONTEXT CURRENT VIEW/ACTIVITY
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setMessage("Please wait...");
                    pd.setMax(100);
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    pd.show();
                } else {
                    Log.e("Error->", "Context object is null");
                }
            }
        } else {
            if (pd != null) {
                pd.hide();
            }
        }
        Log.e("", "Done with showProgress()");
    }

    private String checkResponseData(Boolean success) {
        try {
            String line;
            String data = "";
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = null;

            if (conn.getContentLength() != 0) {
                Log.e("response test->", "a");
                InputStream stream = null;
                try {
                    stream = success ? conn.getInputStream() : conn.getErrorStream();
                } catch (Exception e) {
                    e.printStackTrace();
                    stream = success ? conn.getErrorStream() : conn.getInputStream();
                } // DO THIS UP THERE TO MAKE SURE THAT AN INPUT/ERROR STREAM IS RETRIEVED NO MATTER WHAT
                reader = new BufferedReader(new InputStreamReader(stream));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                data = builder.toString();
                Log.e("checking response", data);
                try {
                    JSONObject sth = (new JSONObject(data));
                    if (sth.has("success") && (sth.getBoolean("success") == false)) {
                        if (sth.has("message") && !(sth.getString("message").isEmpty())) {
                            Log.e("error msg->", sth.getString("message"));
                            AutomanApp.getApp().showToast(sth.getString("message"));
                            data = "";
                        }
                    }
                } catch (JSONException e) {
                    Log.e("JSON RESPONSE ERROR", e.toString());
                }
                return data;
            } else {
                Log.e("response test->", "b");
                return "";
            }
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            return "";
        }
    }

    private String makeRequest(String url, String requestmethod, String querystr) {
        String data = "";
        try {
            String str = this.url + url;

            if (AutomanApp.getApp().getAccessToken() != null && AutomanApp.getApp().getAccessToken().length() > 0)
                str += "?access_token=" + AutomanApp.getApp().getAccessToken();

            Log.e("Request to url -> ", requestmethod + " " + str);
            conn = (HttpURLConnection) (new URL(str)).openConnection();
            conn.setReadTimeout(20 * 1000 /* milliseconds */);
            conn.setConnectTimeout(20 * 1000 /* milliseconds */);
            conn.setRequestMethod(requestmethod);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/json");
            if (requestmethod.equalsIgnoreCase("POST") || requestmethod.equalsIgnoreCase("PUT")) {
                Log.e(requestmethod, " SETTING DO OUTPUT TO TRUE ...");
                conn.setDoOutput(true);
                if ((querystr != null) && (querystr.length() > 0)) {
                    Log.e("json data->", querystr);
                    conn.getOutputStream().write(querystr.getBytes());
                }
            }
            Log.e("SET Request Method -> ", conn.getRequestMethod());
            conn.connect();
            Log.e("NOW Request Method -> ", conn.getRequestMethod());

            if ((conn.getResponseCode() >= 200) && (conn.getResponseCode() < 300)) {
                Log.e("test->", "a");
                data = this.checkResponseData(true);
            } else if ((conn.getResponseCode() >= 300) && (conn.getResponseCode() <= 500)) {
                Log.e("test->", "b");
                data = this.checkResponseData(false);
            } else if (conn.getResponseCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                Log.e("test->", "c");
                data = "";
            } else {
                Log.e("test->", "d");
                data = "";
            }

        } catch (HttpRetryException e) {
            Log.e("test->", "e: " + e.toString());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.e("test->", "f: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("test->", "g: " + e.toString());
            if (e.toString().contains("Unable to resolve host \"automanghana.herokuapp.com\"")) {
                Log.e("NOTICE", "NO INTERNET CONNECTION WHEN MAKING REQUEST");
                // THIS WON'T WORK, DUE TO NO Looper.prepare() FUNCTION CALLED WITHIN THIS ASYNC-TASK
                AutomanApp.getApp().showToast("No Internet Connection, Please check your network provider");
            }
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("test->", e.toString());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Log.e("response data->", data);
        return data;
    }

    public Boolean login(String username, String password) {
        try {
            if (username == null || password == null) {
                Log.e("ERROR->", "username or password param is null");
                return false;
            }
            // MAKE SURE THIS IS CALLED FIRST SO NO access_token PARAM IS ADDED TO URL WITHIN makeRequest()
            this.saveUserAndCompanyData(null);
            // THIS SHOULD BE A QUERY STRING, LIKE SO -> username=AUTOMAN - CEO&password=AUTOMAN2018&companyRegisteredName=AUTOMAN
            String str = "/auth/local?username=" + URLEncoder.encode(username.trim(), "UTF-8") +
                    "&password=" + URLEncoder.encode(password.trim(), "UTF-8") +
                    "&companyRegisteredName=" + URLEncoder.encode(AutomanApp.companyRegisteredName.trim(), "UTF-8");

//            json = new JSONObject();
//            json.put("username", URLEncoder.encode(username, "UTF-8"));
//            json.put("password", URLEncoder.encode(password, "UTF-8"));
//            json.put("companyRegisteredName", URLEncoder.encode(AutomanApp.companyRegisteredName, "UTF-8"));

            this.result = makeRequest(str, "POST", null);
        } catch (Exception e) {
            Log.e("err->", e.toString());
            return false;
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    Log.e("LOGIN RESULT -> ", this.result);
                    this.saveUserAndCompanyData(new JSONObject(this.result));
                    return true;
                } catch (JSONException e) {
                    Log.e("json err->", e.toString());
                    return false;
                } catch (Exception e) {
                    Log.e("err->", e.toString());
                    return false;
                }
            }
            return false;
        }
    }

    public Boolean logout() {
        try {
            json = new JSONObject();
            json.put("user", AutomanApp.getApp().getUser().toJSON(true, null));
            json.put("access_token", AutomanApp.getApp().getAccessToken());
            json.put("device", null);
            this.result = makeRequest("/auth/local/logout", "POST", json.toString());
        } catch (Exception e) {
            Log.e("err->", e.toString());
            return false;
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    Log.e("LOGOUT RESULT -> ", this.result);
                    json = new JSONObject(this.result);
                    if (json.optBoolean("success") && json.getString("message").equalsIgnoreCase("User logged out")) {
                        this.saveUserAndCompanyData(null);
                        return true;
                    } else return false;
                } catch (Exception e) {
                    Log.e("err=>", e.toString());
                    return false;
                }
            }
        }
        return false;
    }

    public void getUserData() {
        try {
            this.result = makeRequest("/users/me/" + AutomanApp.companyId, "GET", null);
            if ((!this.result.isEmpty()) && this.result != null) {
                this.saveUserAndCompanyData(new JSONObject(this.result));
            } else {
                AutomanApp.getApp().showToast("No available Data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserAndCompanyData(JSONObject json) {
        try { // PARSE JSON DATA AND SET ACCESS TOKEN
            Log.e("TEST -> ", "SAVING USER DATA");
            Log.e("USER DATA -> ", json != null ? json.toString() : "NULL");
            AutomanApp.getApp().setAccessToken(json != null ? json.optString("access_token", null) : null);
            AutomanApp.getApp().setUser(json != null ? (User) AutomanApp.getApp().jsonToClassObject(users,
                    json.optJSONObject("user")) : null);
//            AutomanApp.getApp().setCompany(json != null ? (Company)AutomanApp.getApp().jsonToClassObject(companies,
//                    json.getJSONObject("company")) : null);
            AutomanApp.getApp().prefeditor().putString("access_token", json != null ? json.getString("access_token") : null);
            AutomanApp.getApp().prefeditor().putString("user", json != null ? json.getJSONObject("user").toString() : null);
//            AutomanApp.getApp().prefeditor().putString("company", json != null ? json.getJSONObject("company").toString() : null);
            AutomanApp.getApp().prefeditor().commit();
            Log.e("DONE", "PREFERENCES SAVED");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean requestUserEdit(String username, String password) {
        try {
            json = new JSONObject();
            json.put("username", URLEncoder.encode(username, "UTF-8"));
            json.put("password", URLEncoder.encode(password, "UTF-8"));
            json.put("companyRegisteredName", URLEncoder.encode(AutomanApp.companyRegisteredName, "UTF-8"));
            this.result = makeRequest("/auth/local/requestUserEdit", "POST", json.toString());
        } catch (Exception e) {
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    this.saveUserEditAndCompanyData(new JSONObject(this.result));
                    return true;
                } catch (JSONException e) {
                    Log.e("err->", "JSON ERROR");
                    return false;
                } catch (Exception e) {
                    Log.e("err->", "EXCEPTION ERROR");
                    return false;
                }
            }
            return false;
        }
    }

    public Boolean editUserData(JSONObject... edited) {
        try { // FIRST SET UP ALL THE PROFILE DATA
            JSONObject obj = new JSONObject();
            obj.put("user_edit_access_token", AutomanApp.getApp().getUserEditAccessToken());
            if (edited[0] == null) return false;
            obj.put("user", edited[0]);
//            obj.put("company", edited[1] != null ? edited[1] : AutomanApp.getApp().getCompany().toJSON(true, null));

            this.result = makeRequest("/users/me/" + AutomanApp.companyId, "PUT", obj.toString());

            if ((!this.result.isEmpty()) && this.result != null) {
                JSONObject j = (new JSONObject(this.result)); // ASSIGN ALL DATA TO j
                if (j.optJSONObject("user").has("_id")) {
                    json = j.getJSONObject("user"); // ASSIGN USER DATA TO JSON
                    this.renewData(users, json.getString("_id"), "edit");
                }
                if (j.optJSONObject("company").has("_id")) {
                    json = j.getJSONObject("company"); // ASSIGN USER DATA TO JSON
                    this.renewData(companies, json.getString("_id"), "edit");
                }
                AutomanApp.getApp().setUserEditAccessToken("");
                this.saveUserAndCompanyData(j);
                return true;
            } else {
                AutomanApp.getApp().showToast("No available Data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveUserEditAndCompanyData(JSONObject json) {
        try { // PARSE JSON DATA AND SET ACCESS TOKEN
            AutomanApp.getApp().setUserEditAccessToken(json != null ? json.getString("user_edit_access_token") : null);
            AutomanApp.getApp().setUser(json != null ? (User) AutomanApp.getApp().jsonToClassObject(users,
                    json.getJSONObject("user")) : null);
//            AutomanApp.getApp().setCompany(json != null ? (Company)AutomanApp.getApp().jsonToClassObject(companies,
//                    json.getJSONObject("company")) : null);
            AutomanApp.getApp().prefeditor().putString("user_edit_access_token", json != null ? json.getString("user_edit_access_token") : null);
            AutomanApp.getApp().prefeditor().putString("user", json != null ? json.getJSONObject("user").toString() : null);
            AutomanApp.getApp().prefeditor().putString("company", json != null ? json.getJSONObject("company").toString() : null);
            AutomanApp.getApp().prefeditor().commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getAutoAPIRoutes(AutomanEnumerations sth) {
        if (Arrays.asList("settings", "users").contains(sth.toString()))
            return "/api/" + sth.toString() + "/";
        else if (Arrays.asList("autoaudits", "autologs", "autoevents").contains(sth.toString()))
            return "/api/autosecurity/autoauditing/" + sth.toString() + "/";
        else if (Arrays.asList("stocks").contains(sth.toString()))
            return "/public/autoinvestment/" + sth.toString() + "/";
        else return "";
    }

    public void getDashboardSettings() {
        try {
            this.result = makeRequest(this.getAutoAPIRoutes(settings) + "company/", "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    AutomanApp.getApp().settingsHandler().setUpDashboardSettings(new JSONObject(this.result));
                } catch (Exception e) {
                    return;
                }
            } else if (this.result == null) {

                return;
            } else {

                return;
            }
        }
    }

    public <T> ArrayList<T> get(AutomanEnumerations sth, JSONObject condition) {
        ArrayList<T> arr = null;
        try {
            this.result = makeRequest(this.getAutoAPIRoutes(sth), "GET", (condition != null) ? "condition=" + condition.toString() : null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {// FIRST SET THE CACHE DATA
                    AutomanApp.getApp().cacheData().put(sth, AutomanApp.getApp().jsonToClassArray(sth, new JSONArray(this.result)));
                    Log.e(sth.toPluralUpperCase(sth) + " CACHE", "SIZE -> " + AutomanApp.getApp().cacheData().get(sth).size());
                    arr = AutomanApp.getApp().jsonToClassArray(sth, new JSONArray(this.result));
                    Log.e(sth.toPluralUpperCase(sth) + " SERVER", "SIZE -> " + arr.size());
                    return arr;
                } catch (Exception e) {
                    return null;
                }
            } else if (this.result == null) {

                return null;
            } else {
                return null;
            }
        }
    }

    public Boolean add(AutomanEnumerations sth, JSONObject obj) {

        try {
            this.result = makeRequest(this.getAutoAPIRoutes(sth), "POST", (obj != null) ? obj.toString() : null);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    json = new JSONObject(this.result);
                    if (json.has("_id")) {
                        this.renewData(sth, json.getString("_id"), "add");
                        return true;
                    }
                } catch (Exception e) {

                    return false;
                }
            } else if (this.result == null) {

                return false;
            } else {

                return false;
            }
        }
        return false;
    }

    public Boolean edit(AutomanEnumerations sth, String id, JSONObject obj) {

        try {
            this.result = makeRequest(this.getAutoAPIRoutes(sth) + id, "PUT", (obj != null) ? obj.toString() : null);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    json = new JSONObject(this.result);
                    if (json.has("_id")) {
                        this.renewData(sth, json.getString("_id"), "edit");
                        return true;
                    }
                } catch (Exception e) {

                    return false;
                }
            } else if (this.result == null) {

                return false;
            } else {

                return false;
            }
        }
        return false;
    }

    public Boolean delete(AutomanEnumerations sth, String id) {
        try {
            this.result = makeRequest(this.getAutoAPIRoutes(sth) + id, "DELETE", null);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    json = new JSONObject(this.result);
                    if (json.has("success") && json.getBoolean("success")) {
                        this.renewData(sth, id, "delete");
                        return true;
                    }
                } catch (Exception e) {

                    return false;
                }
            } else if (this.result == null) {

                return false;
            } else {

                return false;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                  SPECIAL FUNCTIONS
    public Boolean serverSpecialFunction(AutomanEnumerations sth, String action, String id, JSONObject obj) {
        return this.serverSpecialFunction(sth, action, null, id, obj);
    }

    public Boolean serverSpecialFunction(AutomanEnumerations sth, String action, String extra, String id, JSONObject obj) {
        String genericAction = "edit", reqMethod = "PUT";
        try { // obj DOESN'T HAVE TO HAVE ANY DATA
            if (Arrays.asList("fire", "cancel").contains(action)) {
                genericAction = "delete";
                reqMethod = "DELETE";
            }
            if (action.equals("message")) {
                genericAction = "";
                reqMethod = "POST";
            }
            if ((extra != null) && (extra.length() > 0)) action += "/" + extra;
            Log.e(sth.toSingularUpperCase(sth), "SERVER SPECIAL FUNCTION \"" + action + "\" (" + id + ") -> " + ((obj != null) ? obj.toString() : "null object"));
            this.result = makeRequest(this.getAutoAPIRoutes(sth) + action + "/" + id,
                    reqMethod, ((obj != null) ? obj.toString() : null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((!this.result.isEmpty()) && this.result != null) {
                try {
                    json = new JSONObject(this.result);
                    if ((json.has("_id")) || (json.has("success") && json.getBoolean("success"))) {
                        if ((genericAction.length() > 0) && (json.has("_id")))
                            this.renewData(sth, json.getString("_id"), genericAction);
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            } else if (this.result == null) {
                return false;
            } else {
                return false;
            }
        }
        return false;
    }

    public Boolean handleAutoLogOrAutoEvent(AutomanEnumerations sth, String id, JSONObject obj) {
        return this.serverSpecialFunction(sth, "handle", id, obj);
    }

    public Boolean optionStock(AutomanEnumerations sth, String id, JSONObject obj) {
        if (sth != stocks) sth = stocks;
        return this.serverSpecialFunction(sth, "option", id, obj);
    }

    public Boolean sendMessage(AutomanEnumerations sth, JSONArray cmeths, JSONArray recipients, JSONObject msgdata) {
        try {
            json = new JSONObject();
            json.put(sth.toSingularLowerCase(sth), recipients);
            json.put("contact_methods", cmeths);
            json.put("data", msgdata);
            return this.serverSpecialFunction(sth, "message", null, "", json);
//            this.result = makeRequest(this.getAutoAPIRoutes(sth) + "/message", "POST", (json != null) ? json.toString() : null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            if ((!this.result.isEmpty()) && this.result != null) {
//                try {
//                    json = new JSONObject(this.result);
//                    if (json.has("success") && json.getBoolean("success")) {
//                        return true;
//                    }
//                } catch (Exception e) {
//                    return false;
//                }
//            } else if (this.result == null) {
//                return false;
//            } else {
//                return false;
//            }
        }
        return false;
    }

    private void renewData(AutomanEnumerations sth, String id, String option) {
        Log.e(sth.toSingularUpperCase(sth), option + " -> " + id);
        // IF "add", YOU MUST ADD TO THE BEGINNING OF THE ARRAY, NOT END, COZ IT'S MOST LIKELY SORTED BY { date_created : -1 }
        switch (sth) {
            case users:
                if (option.equalsIgnoreCase("add")) {
                    AutomanApp.getApp().users().add((User) AutomanApp.getApp().jsonToClassObject(users, json));
                } else {
                    for (int i = 0; i < AutomanApp.getApp().users().size(); i++) {
                        if ((AutomanApp.getApp().users().get(i).id() + "").trim().equalsIgnoreCase(id)) {
                            if (option.equalsIgnoreCase("edit"))
                                AutomanApp.getApp().users().set(i, (User) (AutomanApp.getApp().jsonToClassObject(sth, json)));
                            else if (option.equalsIgnoreCase("delete"))
                                AutomanApp.getApp().users().remove((User) (AutomanApp.getApp().users().get(i)));
                        }
                    }
                }
                break;
            case stocks:
                if (option.equalsIgnoreCase("add")) {
                    AutomanApp.getApp().stocks().add((Stock) AutomanApp.getApp().jsonToClassObject(stocks, json));
                } else {
                    for (int i = 0; i < AutomanApp.getApp().stocks().size(); i++) {
                        if ((AutomanApp.getApp().stocks().get(i).id() + "").trim().equalsIgnoreCase(id)) {
                            if (option.equalsIgnoreCase("edit"))
                                AutomanApp.getApp().stocks().set(i, (Stock) (AutomanApp.getApp().jsonToClassObject(sth, json)));
                            else if (option.equalsIgnoreCase("delete"))
                                AutomanApp.getApp().stocks().remove((Stock) (AutomanApp.getApp().stocks().get(i)));
                        }
                    }
                }
                break;
            //  AUTO-AUDITING
            case autologs:
                if (option.equalsIgnoreCase("add")) {
                    AutomanApp.getApp().autologs().add((AutoLog) AutomanApp.getApp().jsonToClassObject(autologs, json));
                } else {
                    for (int i = 0; i < AutomanApp.getApp().autologs().size(); i++) {
                        if ((AutomanApp.getApp().autologs().get(i).id() + "").trim().equalsIgnoreCase(id)) {
                            if (option.equalsIgnoreCase("edit"))
                                AutomanApp.getApp().autologs().set(i, (AutoLog) (AutomanApp.getApp().jsonToClassObject(sth, json)));
                            else if (option.equalsIgnoreCase("delete"))
                                AutomanApp.getApp().autologs().remove((AutoLog) (AutomanApp.getApp().autologs().get(i)));
                        }
                    }
                }
                break;
            case autoevents:
                if (option.equalsIgnoreCase("add")) {
                    AutomanApp.getApp().autoevents().add((AutoEvent) AutomanApp.getApp().jsonToClassObject(autoevents, json));
                } else {
                    for (int i = 0; i < AutomanApp.getApp().autoevents().size(); i++) {
                        if ((AutomanApp.getApp().autoevents().get(i).id() + "").trim().equalsIgnoreCase(id)) {
                            if (option.equalsIgnoreCase("edit"))
                                AutomanApp.getApp().autoevents().set(i, (AutoEvent) (AutomanApp.getApp().jsonToClassObject(sth, json)));
                            else if (option.equalsIgnoreCase("delete"))
                                AutomanApp.getApp().autoevents().remove((AutoEvent) (AutomanApp.getApp().autoevents().get(i)));
                        }
                    }
                }
                break;
        }
    }
}
