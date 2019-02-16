package automan.automanclientsmobile_android.MODEL;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;

/**
 * Created by mr.amo-addai on 3/26/18.
 */

public class Stock extends AutoObject {

    private String name = "", details = "", stockName = "";
    private Double price = null;
    private JSONObject data = new JSONObject();

    public Stock() {
        super();
    }

    public Stock(String id, String name, String details, String stockName, Double price,
                     JSONObject data, JSONObject security, Date creAt, JSONObject json) {
        super(id, security, creAt, json);
        if (id.length() > 0) {
            //
            this.name = name;
            this.details = details;
            this.stockName = stockName;
            this.price = price;
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

    public String stockName() {
        return this.stockName;
    }
    public void stockName(String x) {
        this.stockName = x;
    }

    public Double price() {
        return this.price;
    }
    public void price(Double x) {
        this.price = x;
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
            json.put("stock_name", this.stockName);
            json.put("price", this.price);
            json.put("data", this.data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
