package automan.automanclientsmobile_android.VIEW.SETTINGS_VIEWS;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import automan.automanclientsmobile_android.MODEL.USER_DATA.User;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA.GeneralAddEdit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.R;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.users;

public class UserProfile extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText username = null, password = null, repass = null, firstname = null, lastname = null, othernames = null,
            details = null, age = null, haddress = null, paddress = null, phone = null, email = null;
    private Spinner gender = null, cmeth = null;
    private Button savebutton = null, cancelbutton = null;

    private JSONArray editableProps = null;
    private HashMap<String, View> uiComponents = new HashMap<String, View>();
    private ArrayList<Button> uiButtons = new ArrayList<Button>();

    private Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        //  NOT TOO SURE IS THIS IS EVEN REQUIRED ANYMORE SEF ...
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setUpDetails();

    }

    private User obj = null;
    private Boolean editMode = false;
    private ImageView imgview = null;

    private void setUpDetails() {

        this.username = (EditText) findViewById(R.id.username);
        this.firstname = (EditText) findViewById(R.id.firstname);
        this.lastname = (EditText) findViewById(R.id.lastname);
        this.othernames = (EditText) findViewById(R.id.othernames);
        this.details = (EditText) findViewById(R.id.details);
        this.age = (EditText) findViewById(R.id.age);
        this.email = (EditText) findViewById(R.id.email);
        this.phone = (EditText) findViewById(R.id.phone);
        this.haddress = (EditText) findViewById(R.id.haddress);
        this.paddress = (EditText) findViewById(R.id.paddress);
        //
        this.gender = (Spinner) findViewById(R.id.gender);
        this.gender.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("gender")));
        this.gender.setOnItemSelectedListener(this);
        //
        this.cmeth = (Spinner) findViewById(R.id.cmeth);
        this.cmeth.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("contactMethods")));
        this.cmeth.setOnItemSelectedListener(this);
        //

        // OBJECTS

        // ARRAYS

        // IMAGEVIEW & BUTTONS
//        this.imgview = (ImageView) findViewById(R.id.imgview);
//        this.imgview.setOnClickListener(this);

        this.savebutton = (Button) findViewById(R.id.savebutton);
        this.savebutton.setOnClickListener(this);
        this.cancelbutton = (Button) findViewById(R.id.cancelbutton);
        this.cancelbutton.setOnClickListener(this);

        this.obj = (User) GeneralAddEdit.get().genericSetUp(users, (Activity) this, true);
        if (this.obj == null) this.obj = AutomanApp.getApp().getUser(); // SET this.obj PROPERTY


        this.password = (EditText) findViewById(R.id.password);
        this.repass = (EditText) findViewById(R.id.repass);
        //
        this.password.setHint("Set new password");
        this.repass.setHint("Confirm new password");

        this.username.setText(this.obj.username());
        this.firstname.setText(obj.firstName());
        this.lastname.setText(obj.lastName());
        this.othernames.setText(obj.otherNames());
        this.details.setText(obj.details());
        String str = obj.age() + "";
        this.age.setText(str);
        this.email.setText(obj.email());
        this.phone.setText(obj.phone());
        this.haddress.setText(obj.haddress());
        this.paddress.setText(obj.paddress());

        this.cmeth.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("contactMethods").indexOf(this.obj.cmeth()));
        this.gender.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("gender").indexOf(this.obj.gender()));

        try {
            ////////////////////////////////////////////////////////////////////////////////
            ///     OBJECTS - JUST SET SELECTED ITEM WITHIN THE SPINNER'S LIST


            /////////////////////////////////////////////////////
            ///     ARRAYS
            ///     BANKS FIELDS
            ///     JUST SET ALL THE "CHECKED" STATUSES OF THE LIST ITEMS OF this.obj TO TRUE!!!

        } catch (Exception e) {
            e.printStackTrace();
        }

        // NOW ADD ALL AVAILABLE UI COMPONENTS
//        this.setupUIComponents();
//        this.toggleEditMode(getIntent().getBooleanExtra("editmode", false)); // MAYBE, THIS SHOULD ALWAYS BE FALSE, COZ IT'S THE INITIAL SETUP
        // UNLESS, PERHAPS, THE USER CAN GO STRAIGHT TO EDITING HIS/HER USER PROFILE WITHOUT HAVING TO LOOK AT DISABLED UI COMPONENTS 1ST

        ///////////////////////////////////////////////////////
        ///     IMAGE VIEW
        /// NO NEED TO CHECK IF IMAGE FROM PREVIOUS ACTIVITY (DETAILS / HOME PAGE) FIRST
//        GeneralAddEdit.get().genericGetImage(this.obj.img(), this.imgview);

    }

//    private void setupUIComponents() {
//
//        this.editableProps = AutomanApp.getApp().getEditableUserProfileData();
//        this.uiComponents.put("image_stub", imgview);
//        this.uiComponents.put("username", username);
//        // FIND OUT HOW YOU CAN WORK WITH EDITING ("CHANGING") OF PASSWORDS
//        // COZ IT MUST BE A LITTLE DIFFERENT FROM THE OTHERS
//        this.uiComponents.put("password", password);
//        this.uiComponents.put("", repass); // DEPENDS ON password
//        this.uiComponents.put("first_name", firstname);
//        this.uiComponents.put("last_name", lastname);
//        this.uiComponents.put("other_names", othernames);
//        this.uiComponents.put("gender", gender);
//        this.uiComponents.put("details", details);
//        this.uiComponents.put("age", age);
//        this.uiComponents.put("phone", phone);
//        this.uiComponents.put("email", email);
//        this.uiComponents.put("contact_method", cmeth);
//
//        this.uiButtons.addAll(Arrays.asList(this.savebutton, this.cancelbutton));
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit: // CHECK USER-EDITING ALLOWANCE, IF TRUE, THEN CHECK shouldRequestBeforeUserProfileEditing TO SHOW DIALOG OR NOT
//                if (true) {
//                    if (false) { // SHOW DIALOG TO INPUT PASSWORD
//                        this.showRequestUserEditDialog();
//                    } else { // THEN JUST GO AHEAD TO TOGGLE EDIT MODE
//                        UserProfile.this.toggleEditMode(true);
//                    }
//                }
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

//    private void toggleUIComponent(String key, Boolean toggle) {
//        this.uiComponents.get(key).setClickable(toggle);
//        this.uiComponents.get(key).setEnabled(toggle);
//        this.uiComponents.get(key).setActivated(toggle);
//        for (Button b : this.uiButtons) {
//            b.setClickable(toggle);
//            b.setEnabled(toggle);
//            b.setActivated(toggle);
//        }
//    }
//
//    public void toggleEditMode(Boolean mode) {
//        try {
//            this.editableProps = AutomanApp.getApp().getEditableUserProfileData();
//            if (mode) { // ENABLE ALL EDITABLE UI COMPONENTS BASED ON SETTINGS
//                this.editMode = true;
//                if (this.editableProps != null) {
//                    for (int i = 0; i < this.editableProps.length(); i++) {
//                        this.toggleUIComponent(this.editableProps.getString(i), true);
//                    }
//                }
//            } else { // DISABLE ALL UI COMPONENTS
//                this.editMode = false;
//                for (String x : this.uiComponents.keySet()) {
//                    this.toggleUIComponent(x, false);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.cancelbutton:
                    finish();
                    break;
                case R.id.savebutton: // PERFORM EDIT
                    if (this.validate()) { // YOU CAN PASS MULTIPLE PARAMS WITHIN .execute() (1ST -> USER & 2ND -> COMPANY)
                        JSONObject json = this.obj.toJSON(true, null);
                        if ((this.password.getText().toString().length() > 0) && (this.repass.getText().toString().length() > 0)) {
                            if (this.password.getText().toString().equals(this.repass.getText().toString())) {
                                json.put("password", this.password.getText().toString());
                            }
                        }
                        (new UserProfile.UserEditAsync()).execute(json);
                    }
                    break;
//                 case R.id.imgview: // SET IMAGE VIEW IMAGE (WITH CAMERA / GALLERY)
//                     GeneralAddEdit.get().useCameraOrGallery(this);
//                     break;

                case R.id.context:

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        try {
            switch (parent.getId()) {
                case R.id.cmeth:
                    this.obj.cmeth(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("contactMethods").get(pos));
                    Log.e("cmeth", " SELECTED!!! " + this.obj.cmeth());
                    break;
                case R.id.gender:
                    this.obj.gender(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("gender").get(pos));
                    Log.e("gender", " SELECTED!!! " + this.obj.gender());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e("NOTICE", "ON NOTHING SELECTED!!!");
        try {
            switch (parent.getId()) {
                case R.id.cmeth:
                    this.obj.cmeth("");
                    break;
                case R.id.gender:
                    this.obj.gender("");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void setupEditedUser() {
//        try {
//            JSONObject user = this.obj.toJSON(true, GeneralAddEdit.get().img());
//            this.editedUser = new JSONObject();
//            // EITHER ADD ALL EDITABLE PROPS TO editedUser OR FIND A WAY TO ADD ALL EDITABLE PROPS THAT HAVE ACTUALLY BEEN EDITED
//            if (this.editableProps == null)
//                this.editableProps = AutomanApp.getApp().getEditableUserProfileData();
//            if (this.editableProps != null) {
//                String key = "";
//                View component = null;
//                for (int i = 0; i < this.editableProps.length(); i++) {
//                    key = this.editableProps.getString(i);
//                    this.editedUser.put(key, user.get(key));
//                    // THIS CODE MIGHT ALSO WORK, BUT JUST USE THE ABOVE CODE
//                    // component = this.uiComponents.get(key);
//                    // if(component instanceof EditText) this.editedUser.put(key, ((EditText)component).getText().toString());
//                    // // DECIDE WHETHER SELECTED ITEM SHOULD BE A STRING OR SOME USER-DEFINED CLASS TYPE
//                    // // AND ALSO FIND OUT HOW YOU'RE GOING TO CATER FOR ARRAY PROPERTIES (MULTI-SELECT SPINNERS)
//                    // if(component instanceof Spinner) this.editedUser.put(key, ((Spinner)component).getSelectedItem());
//                    // if(component instanceof ImageView) this.editedUser.put("image_data", /* GeneralAddEdit.get().img() OR ((ImageView)component).getDrawable() */);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private Boolean validate() { // MAKE SURE THAT EDITED COMPONENTS WITHIN editedUser ARE ALL THE EDITABLE PROPERTIES
        try { // NOT TOO SURE IF ALL COMPONENTS' VALIDATIONS ARE EVEN REQUIRED, COZ THIS IS USER PROFILE EDITING
            if (this.username.getText().toString().isEmpty() || this.firstname.getText().toString().isEmpty() ||
                    this.lastname.getText().toString().isEmpty() || ((String) this.gender.getSelectedItem()).isEmpty() ||
                    this.age.getText().toString().isEmpty() || this.details.getText().toString().isEmpty() ||
                    this.email.getText().toString().isEmpty() || this.phone.getText().toString().isEmpty() ||
                    this.haddress.getText().toString().isEmpty() || this.paddress.getText().toString().isEmpty()) {
                AutomanApp.getApp().showToast("Please fill form");
                return false;
            }

            // try { // CHECK DATA SECURITY SETTINGS - COZ THIS IS USER PROFILE, THIS MIGHT NOT BE NECESSARY HERE
            //     JSONObject res = AutomanApp.getApp().validateDataSecurity(users, "edit", this.obj);
            //     if (!res.optBoolean("success", false)) {
            //         AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Data Security");
            //         return false;
            //     }
            //     res = AutomanApp.getApp().validateSecurityLogin(users, "edit", this.obj);
            //     if (!res.optBoolean("success", false)) {
            //         AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Security");
            //         return false;
            //     }
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
            //
            if ((this.password.getText().toString().length() > 0) && (this.repass.getText().toString().length() > 0)) {
                if (!this.password.getText().toString().equals(this.repass.getText().toString())) {
                    AutomanApp.getApp().showToast("Password and confirmation do not match");
                    return false;
                }
            } else {
                AutomanApp.getApp().showToast("Please fill password and confirm it");
                return false;
            }

            this.obj.username(this.username.getText().toString());
            this.obj.firstName(this.firstname.getText().toString());
            this.obj.lastName(this.lastname.getText().toString());
            this.obj.otherNames(this.othernames.getText().toString());
            this.obj.details(this.details.getText().toString());
            this.obj.age(Integer.parseInt(this.age.getText().toString()));
            this.obj.email(this.email.getText().toString());
            this.obj.phone(this.phone.getText().toString());
            this.obj.haddress(this.haddress.getText().toString());
            this.obj.paddress(this.paddress.getText().toString());

            /////////////////////////////////////////////////////////
            //  NOT SURE IF THIS (OBJECTS & ARRAYS) IS EVEN NECESSARY

            this.obj.cmeth((String) this.cmeth.getSelectedItem());
            this.obj.gender((String) this.gender.getSelectedItem());

            /////////////////////////////////////////////////////////
            //      OBJECTS
            ///////////////////////////////////////////////////////////
            //      ARRAYS - FIND A WAY TO GET ALL SELECTED ITEMS PERFECTLY

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("VALIDATED", this.obj.toJSON(true, null).toString());
        return true;
    }

    public void showRequestUserEditDialog() {
        // ASK FOR USER'S PASSWORD, THEN JUST ADD THE USERNAME FROM AutomanApp.getApp().getUser().username();
    }

    //////////////////////////////////////////////////////////////////
    /// AYNCHRONOUS TASKS HERE
//    public class RequestUserEditAsync extends AsyncTask<String, Integer, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            AutomanApp.getApp().server().showProgress(true);
//        }
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//            if (AutomanApp.getApp().isOnline())
//                return AutomanApp.getApp().server().requestUserEdit(params[0], params[1]);
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean res) {
//            super.onPostExecute(res);
//            AutomanApp.getApp().server().showProgress(false);
//            if (!res) AutomanApp.getApp().showToast("INCORRECT USERNAME OR PASSWORD");
//            UserProfile.this.toggleEditMode(res);
//        }
//    }

    public class UserEditAsync extends AsyncTask<JSONObject, Integer, Boolean> {

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
        protected Boolean doInBackground(JSONObject... params) {
            Log.e("SENT DATA", params[0].toString());
            return AutomanApp.getApp().server().editUserData(params);
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            AutomanApp.getApp().server().showProgress(false);
            if (res) {
                AutomanApp.getApp().showToast("User Profile edited successfully");
                UserProfile.this.finish();
            } else AutomanApp.getApp().showToast("User Profile could not be edited");
        }
    }
}
