package automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING;

import android.graphics.Bitmap;
import automan.automanclientsmobile_android.AutomanApp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by F on 6/4/2017.
 */

public class AutoEvent extends AutoAudit {

    private String autoevent = "", emergencyLvl = "";
    private JSONArray autologs = new JSONArray();

    public AutoEvent() {
        super();
    }

    public AutoEvent(String id, String name, String details, String type, String sourceType, String source, String autoaudit,
     JSONObject data, JSONObject security, Date creAt, JSONObject json,
     String autoevent, String emergencyLvl, JSONArray autologs) {
        super(id, name, details, type, sourceType, source, autoaudit, data, security, creAt, json);
        if (id.length() > 0) {
            //
            this.autoevent = autoevent;
            this.emergencyLvl = emergencyLvl;
            this.autologs = autologs;
        }
    }

    public String autoevent() {
        return this.autoevent;
    }
    public void autoevent(String x) {
        this.autoevent = x;
    }

    public String emergencyLvl() {
        return this.emergencyLvl;
    }
    public void emergencyLvl(String x) {
        this.emergencyLvl = x;
    }

    public JSONArray autologs() {
        return this.autologs;
    }
    public void autologs(JSONArray x) {
        this.autologs = x;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////   SETTERS
    public JSONObject toJSON(Boolean edit, Bitmap img) {
        JSONObject json = null;
        try {
            json = super.toJSON(edit, img);
            //
            json.put("autoevent", this.autoevent);
            json.put("emergency_level", this.emergencyLvl);
            json.put("autologs", this.autologs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
