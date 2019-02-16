package automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.USER_DATA.User;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA.GeneralAddEdit;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.users;

public class AddEditUser extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText username = null, password = null, repass = null, firstname = null, lastname = null, othernames = null,
            details = null, age = null, haddress = null, paddress = null, phone = null, email = null;
    private TextView genderview = null, cmethview = null;
    private Spinner gender = null, cmeth = null;
    private Button savebutton = null, cancelbutton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        this.setUpDetails();
    }


    private User obj = null;
    private Boolean add = true;
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

        this.obj = (User) GeneralAddEdit.get().genericSetUp(users, (Activity) this);


//        this.password = (EditText) findViewById(R.id.password);
//        this.repass = (EditText) findViewById(R.id.repass);
//        //
//        this.password.setText("Set new password");
//        this.repass.setText("Confirm new password");

        switch (getIntent().getExtras().getString("addoredit")) {
            case "add":
                this.add = true;
                this.obj = new User();
                break;
            case "edit":
                this.add = false;

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


                ///////////////////////////////////////////////////////
                ///     IMAGE VIEW
                /// NO NEED TO CHECK IF IMAGE FROM PREVIOUS ACTIVITY (DETAILS / HOME PAGE) FIRST
//                GeneralAddEdit.get().genericGetImage(this.obj.img(), this.imgview);

                break;
        }

    }

    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.cancelbutton:
                    finish();
                    break;
                case R.id.savebutton: // PERFORM ADD/EDIT
                    if (this.validate()) {
                        if (add) {
                            (new AutomanApp.Add(this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(users);
                        } else {
                            (new AutomanApp.Edit(this.obj.id() + "", this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(users);
                        }
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

    private Boolean validate() {
        try {
            if (this.username.getText().toString().isEmpty() || this.firstname.getText().toString().isEmpty() ||
                    this.lastname.getText().toString().isEmpty() || ((String) this.gender.getSelectedItem()).isEmpty() ||
                    this.age.getText().toString().isEmpty() || this.details.getText().toString().isEmpty() ||
                    this.email.getText().toString().isEmpty() || this.phone.getText().toString().isEmpty() ||
                    this.haddress.getText().toString().isEmpty() || this.paddress.getText().toString().isEmpty()) {
                AutomanApp.getApp().showToast("Please fill form");
                return false;
            }

            try { // CHECK DATA SECURITY SETTINGS
//                JSONObject res = AutomanApp.getApp().validateDataSecurity
//                        (users, add ? "add" : "edit", this.obj);
//                if(!res.optBoolean("success", false)){
//                    AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Data Security");
//                    return false;
//                }
//                res = AutomanApp.getApp().validateSecurityLogin
//                        (users, add ? "add" : "edit", this.obj);
//                if(!res.optBoolean("success", false)){
//                    AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Security");
//                    return false;
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //
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

}
