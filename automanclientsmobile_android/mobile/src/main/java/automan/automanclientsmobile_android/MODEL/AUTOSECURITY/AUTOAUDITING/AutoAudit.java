package automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING;

import android.graphics.Bitmap;
import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by F on 6/4/2017.
 */

public class AutoAudit extends AutoObject {
    protected String name = "", details = "", type = "", sourceType = "", source = "", autoaudit = "";
    protected JSONObject data = new JSONObject();

    public AutoAudit() {
        super();
    }

    public AutoAudit(String id, String name, String details, String type, String sourceType, String source, String autoaudit, 
        JSONObject data, JSONObject security, Date creAt, JSONObject json) {
        super(id, security, creAt, json);
        if (id.length() > 0) {
            //
            this.name = name;
            this.details = details;
            this.type = type;
            this.sourceType = sourceType;
            this.source = source;
            this.autoaudit = autoaudit;
            this.data = data;
        }
    }

    public String name() {
        return this.name;
    }
    public void name(String x) {
        this.name = x;
    }

    public String details() {
        return this.details;
    }
    public void details(String x) {
        this.details = x;
    }

    public String type() {
        return this.type;
    }
    public void type(String x) {
        this.type = x;
    }

    public String sourceType() {
        return this.sourceType;
    }
    public void sourceType(String x) {
        this.sourceType = x;
    }

    public String source() {
        return this.source;
    }
    public void source(String x) {
        this.source = x;
    }

    public String autoaudit() {
        return this.autoaudit;
    }
    public void autoaudit(String x) {
        this.autoaudit = x;
    }

    public JSONObject data() {
        return this.data;
    }
    public void data(JSONObject x) {
        this.data = x;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////   SETTERS
    public JSONObject toJSON(Boolean edit, Bitmap img) {
        JSONObject json = null; img = null;
        try {
            json = super.toJSON(edit, img);
            //
            json.put("name", this.name);
            json.put("details", this.details);
            json.put("type", this.type);
            json.put("source_type", this.sourceType);
            json.put("source", this.source);
            json.put("autoaudit", this.autoaudit);
            json.put("data", this.data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
