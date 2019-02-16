package automan.automanclientsmobile_android.MODEL.USER_DATA;

import android.graphics.Bitmap;
import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by F on 6/23/2017.
 */
public class Person extends AutoObject {
    protected int age;
    protected String fullName = "", firstName = "", lastName = "", otherNames = "", title = "", gender = "", details = "", phone = "", email = "",
            haddress = "", paddress = "", cmeth = "", img = "";
    protected JSONArray meets = new JSONArray(), projs = new JSONArray(), tasks = new JSONArray(), accounts = new JSONArray(),
            companies = new JSONArray(), banks = new JSONArray();

    public Person(){}

    public Person(String id, String fullName, String firstName, String lastName, String otherNames, String title, String gender,
                  int age, String details, String phone, String email, String haddress, String paddress, String cmeth,
                  JSONObject security, JSONArray meets, JSONArray projs, JSONArray tasks, JSONArray accounts,
                  JSONArray banks, JSONArray companies, Date creAt, String img, JSONObject json) {
        super(id, security, creAt, json);
        if (id.length() > 0) {
            //
            this.fullName = fullName;
            this.firstName = firstName;
            this.lastName = lastName;
            this.otherNames = otherNames;
            this.title = title;
            this.gender = gender;
            this.age = age;
            this.details = details;
            this.phone = phone;
            this.email = email;
            this.haddress = haddress;
            this.paddress = paddress;
            
            this.cmeth = cmeth;

            this.meets = meets;
            this.accounts = accounts;
            this.companies = companies;
            this.projs = projs;
            this.tasks = tasks;
            this.banks = banks;

            this.img = img;
        }
    }

    public String fullName() {
        return this.fullName;
    }
    public void fullName(String x) {
        this.fullName = x;
    }

    public String firstName() {
        return this.firstName;
    }
    public void firstName(String x) {
        this.firstName = x;
    }

    public String lastName() {
        return this.lastName;
    }
    public void lastName(String x) {
        this.lastName = x;
    }

    public String otherNames() {
        return this.otherNames;
    }
    public void otherNames(String x) {
        this.otherNames = x;
    }

    public String title() {
        return this.title;
    }
    public void title(String x) {
        this.title = x;
    }

    public String gender() {
        return this.gender;
    }
    public void gender(String x) {
        this.gender = x;
    }

    public int age() {
        return this.age;
    }
    public void age(int x) {
        this.age = x;
    }

    public String details() {
        return this.details;
    }
    public void details(String x) {
        this.details = x;
    }

    public String phone() {
        return this.phone;
    }
    public void phone(String x) {
        this.phone = x;
    }

    public String email() {
        return this.email;
    }
    public void email(String x) {
        this.email = x;
    }

    public String haddress() {
        return this.haddress;
    }
    public void haddress(String x) {
        this.haddress = x;
    }

    public String paddress() {
        return this.paddress;
    }
    public void paddress(String x) {
        this.paddress = x;
    }

    public String cmeth() {
        return this.cmeth;
    }
    public void cmeth(String x) {
        this.cmeth = x;
    }

    public JSONArray meets() {
        return this.meets;
    }
    public void meets(JSONArray x) {
        this.meets = x;
    }

    public JSONArray accounts() {
        return this.accounts;
    }
    public void accounts(JSONArray x) {
        this.accounts = x;
    }

    public JSONArray companies() {
        return this.companies;
    }
    public void companies(JSONArray x) {
        this.companies = x;
    }

    public JSONArray projs() {
        return this.projs;
    }
    public void projs(JSONArray x) {
        this.projs = x;
    }

    public JSONArray tasks() {
        return this.tasks;
    }
    public void tasks(JSONArray x) {
        this.tasks = x;
    }

    public JSONArray banks() {
        return this.banks;
    }
    public void banks(JSONArray x) {
        this.banks = x;
    }

    public String img() {
        return this.img;
    }
    public void img(String x) {
        this.img = x;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////   SETTERS
    private Boolean updateExtra = false;
    public void shouldUpdateExtra(Boolean x){
        this.updateExtra = x;
    }

    public JSONObject toJSON(Boolean edit, Bitmap img) {
        JSONObject json = null;
        try {
            json = super.toJSON(edit, img);
//            json.put("should_update_extra", this.updateExtra);
            //
            json.put("first_name", this.firstName);
            json.put("last_name", this.lastName);
            json.put("other_names", this.otherNames);
//            json.put("title", this.title);
            json.put("gender", this.gender);
            json.put("age", this.age);
            json.put("details", this.details);
            json.put("phone", this.phone);
            json.put("email", this.email);
            json.put("home_address", this.haddress);
            json.put("postal_address", this.paddress);
            json.put("contact_method", this.cmeth);
//            json.put("meetings", this.meets);
//            json.put("projects", this.projs);
//            json.put("tasks", this.tasks);
//            json.put("accounts", this.accounts);
//            json.put("banks", this.banks);
//            json.put("companies", this.companies);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}