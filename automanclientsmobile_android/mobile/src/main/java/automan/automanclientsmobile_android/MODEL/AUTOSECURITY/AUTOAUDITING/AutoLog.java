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

public class AutoLog extends AutoAudit {
    
    private JSONArray autoevents = new JSONArray();

    public AutoLog() {
        super();
    }

    public AutoLog(String id, String name, String details, String type, String sourceType, String source, String autoaudit,
     JSONObject data, JSONObject security, Date creAt, JSONObject json,
     JSONArray autoevents) {
        super(id, name, details, type, sourceType, source, autoaudit, data, security, creAt, json);
        if (id.length() > 0) {
            //
            this.autoevents = autoevents;
        }
    }

    public JSONArray autoevents() {
        return this.autoevents;
    }
    public void autoevents(JSONArray x) {
        this.autoevents = x;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////   SETTERS
    public JSONObject toJSON(Boolean edit, Bitmap img) {
        JSONObject json = null;
        try {
            json = super.toJSON(edit, img);
            //
            json.put("autoevents", this.autoevents);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
