package automan.automanclientsmobile_android.MODEL.USER_DATA;

import android.graphics.Bitmap;
import automan.automanclientsmobile_android.AutomanApp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class User extends Person {
    private int LOS;
    private String username = "", cemail = "", role = "";
    private Boolean available = false, inBuilding = false, isEmp = false, isStake = false, isClient = false;
    private JSONObject seclog = new JSONObject(), emp = new JSONObject(), client = new JSONObject(), stakeHolder = new JSONObject();
    private JSONArray location = new JSONArray(), types = new JSONArray(), reqs = new JSONArray(), complaints = new JSONArray(),
            enqs = new JSONArray(), privs = new JSONArray();

    public User(){}

    public User(String id, String fullName, String firstName, String lastName, String otherNames, String title, String gender,
                int age, String details, String phone, String email, String haddress, String paddress, String cmeth,
                JSONObject security, JSONArray meets, JSONArray projs, JSONArray tasks, JSONArray accounts,
                JSONArray banks, JSONArray companies, Date creAt, String img,  JSONObject json,
                // USER FIELDS
                String username, String cemail, int LOS, String role, Boolean available, JSONArray location, JSONArray types,
                Boolean isEmp, Boolean isClient, Boolean isStake,
                JSONObject emp, JSONObject client, JSONObject stakeHolder, JSONArray privs, JSONObject seclog,
                Boolean inBuilding, JSONArray reqs, JSONArray complaints, JSONArray enqs)
    {
        super(id, fullName, firstName, lastName, otherNames, title, gender, age, details, phone, email, haddress, paddress,
                cmeth, security, meets, projs, tasks, accounts, banks, companies, creAt, img, json);
        if (id.length() > 0) {
            //
            this.username = username;
            this.cemail = cemail;
            this.LOS = LOS;
            this.role = role;
            this.available = available;
            this.location = location;
            this.types = types;
            this.isEmp = isEmp;
            this.isStake = isStake;
            this.isClient = isClient;
            //  OBJECTS
            this.emp = emp;
            this.client = client;
            this.stakeHolder = stakeHolder;
            this.privs = privs;
            //  EMBEDDED SYSTEM STUFF
            this.seclog = seclog;
            this.inBuilding = inBuilding;
            //  ARRAYS
            this.reqs = reqs;
            this.complaints = complaints;
            this.enqs = enqs;
        }
    }

    public String username() {
        return this.username;
    }
    public void username(String x) {
        this.username = x;
    }

    public String cemail() {
        return this.cemail;
    }
    public void cemail(String x) {
        this.cemail = x;
    }

    public int LOS() {
        return this.LOS;
    }
    public void LOS(int x) {
        this.LOS = x;
    }

    public String role() {
        return this.role;
    }
    public void role(String x) {
        this.role = x;
    }

    public Boolean available() {
        return this.available;
    }
    public void available(Boolean x) {
        this.available = x;
    }

    public JSONArray location() {
        return this.location;
    }
    public void location(JSONArray x) {
        this.location = x;
    }

    public JSONArray types() {
        return this.types;
    }
    public void types(JSONArray x) {
        this.types = x;
    }
    public JSONObject getUserType(String userType){
        switch(userType){
            case "Employee": return this.emp;
            case "Client": return this.client;
            case "Stake Holder": return this.stakeHolder;
        }
        return null;
    }

    public Boolean is(String userType){
        switch(userType){
            case "Employee": return this.isEmp;
            case "Client": return this.isClient;
            case "Stake Holder": return this.isStake;
        }
        return false;
    }


    public Boolean isEmp() {
        return this.isEmp;
    }
    public void isEmp(Boolean x) {
        this.isEmp = x;
    }

    public Boolean isStake() {
        return this.isStake;
    }
    public void isStake(Boolean x) {
        this.isStake = x;
    }

    public Boolean isClient() {
        return this.isClient;
    }
    public void isClient(Boolean x) {
        this.isClient = x;
    }

    public JSONObject emp() {
        return this.emp;
    }
    public void emp(JSONObject x) {
        this.emp = x;
    }

    public JSONObject stakeHolder() {
        return this.stakeHolder;
    }
    public void stakeHolder(JSONObject x) {
        this.stakeHolder = x;
    }

    public JSONObject client() {
        return this.client;
    }
    public void client(JSONObject x) {
        this.client = x;
    }

    public JSONArray privs() {
        return this.privs;
    }
    public void privs(JSONArray x) {
        this.privs = x;
    }

    public JSONObject seclog() {
        return this.seclog;
    }
    public void seclog(JSONObject x) {
        this.seclog = x;
    }

    public Boolean inBuilding() {
        return this.inBuilding;
    }
    public void inBuilding(Boolean x) {
        this.inBuilding = x;
    }

    public JSONArray reqs() {
        return this.reqs;
    }
    public void reqs(JSONArray x) {
        this.reqs = x;
    }

    public JSONArray complaints() {
        return this.complaints;
    }
    public void complaints(JSONArray x) {
        this.complaints = x;
    }

    public JSONArray enqs() {
        return this.enqs;
    }
    public void enqs(JSONArray x) {
        this.enqs = x;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////   SETTERS
    public JSONObject toJSON(Boolean edit, Bitmap img) {
        JSONObject json = null;
        try {
            json = super.toJSON(edit, img);
            //
            json.put("username", this.username);
//            json.put("company_email", this.cemail);
//            json.put("levelOfSecurity", this.LOS);
//            json.put("userRole", this.role);
//            //  OBJECTS
//            json.put("employee", this.emp);
//            json.put("client", this.client);
//            json.put("stakeholder", this.stakeHolder);
//            json.put("privileges", this.privs);
//            json.put("securitylogin", this.seclog);
//            //  ARRAYS
//            json.put("types", this.types);
//            json.put("requests", this.reqs);
//            json.put("complaints", this.complaints);
//            json.put("enquiries", this.enqs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}