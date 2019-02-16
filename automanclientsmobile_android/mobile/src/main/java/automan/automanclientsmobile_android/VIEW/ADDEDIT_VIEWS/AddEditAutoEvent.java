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

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA.GeneralAddEdit;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.Home;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autoevents;

public class AddEditAutoEvent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText name = null, details = null, data = null;
    private TextView typeview = null, sourceTypeview = null, sourceview = null, autoauditview = null,
            autoeventview = null, emergencyLvlview = null, autologsview = null;
    private Spinner type = null, sourceType = null, source = null, autoaudit = null,
            autoevent = null, emergencyLvl = null, autologs = null;
    private Button savebutton = null, cancelbutton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_auto_event);

        this.setUpDetails();
    }

    private AutoEvent obj = null;
    private Boolean add = true;
    private ImageView imgview = null;

    private void setUpDetails() {
        // IMAGEVIEW & BUTTONS
        // this.imgview = (ImageView) findViewById(R.id.imgview);
        // this.imgview.setOnClickListener(this);

        this.name = (EditText) findViewById(R.id.name);
        this.details = (EditText) findViewById(R.id.details);
        this.data = (EditText) findViewById(R.id.data);

        this.type = (Spinner) findViewById(R.id.type);
        this.type.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditTypeOptions")));
        this.type.setOnItemSelectedListener(this);
        //
        this.sourceType = (Spinner) findViewById(R.id.sourceType);
        this.sourceType.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditSourceTypeOptions")));
        this.sourceType.setOnItemSelectedListener(this);
        //
        this.source = (Spinner) findViewById(R.id.source);
        this.source.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditSourceOptions")));
        this.source.setOnItemSelectedListener(this);
        //
        this.autoaudit = (Spinner) findViewById(R.id.autoaudit);
        this.autoaudit.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditOptions")));
        this.autoaudit.setOnItemSelectedListener(this);
        //
        this.autoevent = (Spinner) findViewById(R.id.autoevent);
        // YOU'LL SET THE POSSIBLE OPTIONS FOR THIS PROP, LATER, WHEN this.obj HAS BEEN INITIALIZED ...
        this.autoevent.setOnItemSelectedListener(this);
        //
        this.emergencyLvl = (Spinner) findViewById(R.id.emergencyLvl);
        this.emergencyLvl.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditEmergencyLevelOptions")));
        this.emergencyLvl.setOnItemSelectedListener(this);
        //


        // OBJECTS -

        // ARRAYS - AUTOLOGS
        this.autologsview = (TextView) findViewById(R.id.autologsview);
        this.autologs = (Spinner) findViewById(R.id.autologs);
        Home.gAudit().setupSpinnerView(AutomanEnumerations.autologs, this.autologs);
        this.autologs.setOnItemSelectedListener(this);


        this.savebutton = (Button) findViewById(R.id.savebutton);
        this.savebutton.setOnClickListener(this);
        this.cancelbutton = (Button) findViewById(R.id.cancelbutton);
        this.cancelbutton.setOnClickListener(this);

        this.obj = (AutoEvent) GeneralAddEdit.get().genericSetUp(autoevents, (Activity) this);

        switch (getIntent().getExtras().getString("addoredit")) {
            case "add":
                this.add = true;
                this.obj = new AutoEvent();
                break;
            case "edit":
                this.add = false;

                this.name.setText(obj.name());
                this.details.setText(obj.details());
                this.data.setText(obj.data() != null ? obj.data().toString() : "");

                this.type.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditTypeOptions").indexOf(this.obj.type()));
                this.sourceType.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditSourceTypeOptions").indexOf(this.obj.sourceType()));
                this.source.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditSourceOptions").indexOf(this.obj.source()));
                this.autoaudit.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditOptions").indexOf(this.obj.autoaudit()));
                this.autoevent.setSelection(AutomanApp.getApp().settingsHandler().getAutoAuditAutoEventOptions(this.obj.autoaudit()).indexOf(this.obj.autoevent()));
                this.emergencyLvl.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditEmergencyLevelOptions").indexOf(this.obj.emergencyLvl()));

                ////////////////////////////////////////////////////////////////////////////////
                ///     OBJECTS - JUST SET SELECTED ITEM WITHIN THE SPINNER'S LIST
//                try {
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                /////////////////////////////////////////////////////
                ///     ARRAYS
                ///     AUTOLOGS FIELDS
                ///     JUST SET ALL THE "CHECKED" STATUSES OF THE LIST ITEMS OF this.obj TO TRUE!!!
                this.autologsview.append(" - " + this.obj.autologs().length() + " selected");
                // FIND A WAY TO ALSO SHOW THE ITEMS THAT HAVE BEEN SELECTED FORM THE MULTI-SELECT SPINNER


                ///////////////////////////////////////////////////////
                ///     IMAGE VIEW
                ///     GeneralAddEdit.get().genericGetImage(this.obj.img(), this.imgview);
                break;
        }

        this.autoevent.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().getAutoAuditAutoEventOptions(this.obj.autoaudit() != null ? this.obj.autoaudit() : "")));

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
                            (new AutomanApp.Add(this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(autoevents);
                        } else {
                            (new AutomanApp.Edit(this.obj.id() + "", this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(autoevents);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        try {
            switch (parent.getId()) {
                case R.id.type:
                    this.obj.type(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditTypeOptions").get(pos));
                    Log.e("type", " SELECTED!!! " + this.obj.type());
                    break;
                case R.id.sourceType:
                    this.obj.sourceType(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditSourceTypeOptions").get(pos));
                    Log.e("sourceType", " SELECTED!!! " + this.obj.sourceType());
                    break;
                case R.id.source:
                    this.obj.source(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditSourceOptions").get(pos));
                    Log.e("source", " SELECTED!!! " + this.obj.source());
                    break;
                case R.id.autoaudit:
                    this.obj.autoaudit(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditOptions").get(pos));
                    // NOW, RESET THE POSSIBLE OPTIONS FOR autoevent, COZ autoaudit HAS BEEN RESET
                    this.obj.autoevent("");
                    this.autoevent.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                            AutomanApp.getApp().settingsHandler().getAutoAuditAutoEventOptions(this.obj.autoaudit() != null ? this.obj.autoaudit() : "")));

                    Log.e("autoaudit", " SELECTED!!! " + this.obj.autoaudit() + " : autoevent -> " + this.obj.autoevent());
                    break;
                case R.id.autoevent:
                    this.obj.autoevent(AutomanApp.getApp().settingsHandler().getAutoAuditAutoEventOptions(this.obj.autoaudit()).get(pos));
                    Log.e("autoevent", " SELECTED!!! " + this.obj.autoevent());
                    break;
                case R.id.emergencyLvl:
                    this.obj.emergencyLvl(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("autoauditEmergencyLevelOptions").get(pos));
                    Log.e("emergencyLvl", " SELECTED!!! " + this.obj.emergencyLvl());
                    break;
//                case R.id.security:
//                    this.obj.security(AutomanApp.getApp().datasecurities().get(pos).toJSON(true, null));
//                    break;
                case R.id.autologs:
                    Boolean contains = false;
                    for (int i = 0; i < this.obj.autologs().length(); i++) {
                        if (this.obj.autologs().getString(i).equalsIgnoreCase(AutomanApp.getApp().autologs().get(pos).id())) {
                            contains = true;
                            Log.e("NOTICE", "THIS OBJECT ALREADY CONTAINS THIS ID");
                            break;
                        }
                    }
                    if (!contains) {
                        this.obj.autologs().put(AutomanApp.getApp().autologs().get(pos).id());
                        String str = "Select Log(s) - " + this.obj.autologs().length() + " selected";
                        this.autologsview.setText(str);
                    }
                    Log.e("autologs", " SELECTED!!! " + this.obj.autologs().toString());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e("NOTICE", "ON NOTHING SELECTED!!!");
        switch (parent.getId()) {
            case R.id.type:
                this.obj.type("");
                break;
            case R.id.sourceType:
                this.obj.sourceType("");
                break;
            case R.id.source:
                this.obj.source("");
                break;
            case R.id.autoaudit:
                this.obj.autoaudit("");
                break;
            case R.id.autoevent:
                this.obj.autoevent("");
                break;
            case R.id.emergencyLvl:
                this.obj.emergencyLvl("");
                break;
//            case R.id.security:
//                this.obj.security(null);
//                break;
            case R.id.autologs:
                this.obj.autologs();
                break;
        }
    }

    private Boolean validate() {
        if (this.name.getText().toString().isEmpty() ||
                this.details.getText().toString().isEmpty() ||
//                this.data.getText().toString().isEmpty() ||
                this.type.getSelectedItem().toString().isEmpty() ||
                this.sourceType.getSelectedItem().toString().isEmpty() ||
                this.source.getSelectedItem().toString().isEmpty() ||
                this.autoaudit.getSelectedItem().toString().isEmpty() ||
                this.autoevent.getSelectedItem().toString().isEmpty() ||
                this.emergencyLvl.getSelectedItem().toString().isEmpty()) {
            AutomanApp.getApp().showToast("Please fill form");
            return false;
        }
        try { // CHECK DATA SECURITY SETTINGS
            if(this.data.getText().toString().length() > 0) this.obj.data(new JSONObject(this.data.getText().toString()));

//            JSONObject res = AutomanApp.getApp().validateDataSecurity
//                    (autoevents, add ? "add" : "edit", this.obj);
//            if (!res.optBoolean("success", false)) {
//                AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Data Security");
//                return false;
//            }
        } catch (JSONException e) {
            AutomanApp.getApp().showToast("Data field should be in JSON format");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.obj.name(this.name.getText().toString());
        this.obj.details(this.details.getText().toString());

        this.obj.type((String) this.type.getSelectedItem());
        this.obj.sourceType((String) this.sourceType.getSelectedItem());
        this.obj.source((String) this.source.getSelectedItem());
        this.obj.autoaudit((String) this.autoaudit.getSelectedItem());
        this.obj.autoevent((String) this.autoevent.getSelectedItem());
        this.obj.emergencyLvl((String) this.emergencyLvl.getSelectedItem());

        ///////////////////////////////////////////////////////////
        //      OBJECTS - NOT SURE IF THIS IS EVEN NECESSARY
//        this.obj.security(((DataSecurity) this.security.getSelectedItem()).toJSON(true, null));

        ///////////////////////////////////////////////////////////
        //      ARRAYS
//        this.obj.autologs(((AutoLog) this.autologs.getSelectedItems()).toJSON(true, null));

        Log.e("VALIDATED", this.obj.toJSON(true, null).toString());
        return true;
    }
}
