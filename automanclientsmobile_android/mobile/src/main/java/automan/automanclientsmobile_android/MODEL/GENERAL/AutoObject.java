package automan.automanclientsmobile_android.MODEL.GENERAL;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;

/**
 * Created by F on 9/15/2017.
 */
public class AutoObject extends Object {
    protected String id = "";
    protected JSONObject security = new JSONObject(), json = new JSONObject();
    protected Date creAt = new Date();

    public AutoObject() {

    }

    public AutoObject(String id, JSONObject security, Date creAt, JSONObject json) {
        if (id.length() > 0) {
            this.id = id;
            this.security = security;
            this.creAt = creAt;
            this.json = json;
        }
    }

    public Object get(String prop){
        // THIS METHOD IS TO IMPROVE THE USAGE OF GENERICS IN APP
        return null;
    }

    public String id() {
        return this.id;
    }
    public void id(String x) {
        this.id = x;
    }

    public JSONObject security() {
        return this.security;
    }
    public void security(JSONObject x) {
        this.security = x;
    }

    public Date creAt() {
        return this.creAt;
    }
    public void creAt(Date x) {
        this.creAt = x;
    }

    public JSONObject json() {
        return this.json;
    }
    public void json(JSONObject x) {
        this.json = x;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////   SETTERS
    public JSONObject toJSON(Boolean edit, Bitmap img) {
        JSONObject json = null; img = null;
        try {
            json = new JSONObject();
            if (edit) {
                json.put("_id", this.id + "");
            }
            json.put("datasecurity", this.security);

            if (img != null) { // IMAGE DATA (ALSO ADD ALL OTHER FILE DATA eg. AUDIO/VIDEO/DOCUMENT)
                json.put("image_data", AutomanApp.getApp().base64Encode(img));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    /////////////////////////////////////////////////////////////////////////////////
    //      EXTRA FUNCTIONS FOR POLYMORPHISM TO ENHANCE GENERICS CODING
    public String details(){
        return ""; // OR MAYBE YOU SHOULD TRY MAKING IT AN ABSTRACT METHOD
    }
}
