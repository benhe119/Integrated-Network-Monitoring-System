package automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA.GeneralAddEdit;
import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.EXTRA.GeneralDetails;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.Home;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autologs;

public class AddEditAutoLog extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText name = null, details = null, data = null;
    private TextView typeview = null, sourceTypeview = null, sourceview = null, autoauditview = null, autoeventsview = null;
    private Spinner type = null, sourceType = null, source = null, autoaudit = null, autoevents = null;
    private Button savebutton = null, cancelbutton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_auto_log);

        this.setUpDetails();
    }

    private AutoLog obj = null;
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

        // OBJECTS -

        // ARRAYS - AUTOEVENTS
        this.autoeventsview = (TextView) findViewById(R.id.autoeventsview);
        this.autoevents = (Spinner) findViewById(R.id.autoevents);
        Home.gAudit().setupSpinnerView(AutomanEnumerations.autoevents, this.autoevents);
        this.autoevents.setOnItemSelectedListener(this);


        this.savebutton = (Button) findViewById(R.id.savebutton);
        this.savebutton.setOnClickListener(this);
        this.cancelbutton = (Button) findViewById(R.id.cancelbutton);
        this.cancelbutton.setOnClickListener(this);

        this.obj = (AutoLog) GeneralAddEdit.get().genericSetUp(autologs, (Activity) this);

        switch (getIntent().getExtras().getString("addoredit")) {
            case "add":
                this.add = true;
                this.obj = new AutoLog();
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

                ////////////////////////////////////////////////////////////////////////////////
                ///     OBJECTS - JUST SET SELECTED ITEM WITHIN THE SPINNER'S LIST
//        try {
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

                /////////////////////////////////////////////////////
                ///     ARRAYS
                ///     AUTOEVENTS FIELDS
                ///     JUST SET ALL THE "CHECKED" STATUSES OF THE LIST ITEMS OF this.obj TO TRUE!!!
                this.autoeventsview.append(" - " + this.obj.autoevents().length() + " selected");
                // FIND A WAY TO ALSO SHOW THE ITEMS THAT HAVE BEEN SELECTED FORM THE MULTI-SELECT SPINNER
                break;
        }

        ///////////////////////////////////////////////////////
        ///     IMAGE VIEW
        ///     GeneralAddEdit.get().genericGetImage(this.obj.img(), this.imgview);
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
                            (new AutomanApp.Add(this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(autologs);
                        } else {
                            (new AutomanApp.Edit(this.obj.id() + "", this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(autologs);
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
                    Log.e("autoaudit", " SELECTED!!! " + this.obj.autoaudit());
                    break;
                case R.id.autoevents:
                    Boolean contains = false;
                    for (int i = 0; i < this.obj.autoevents().length(); i++) {
                        if (this.obj.autoevents().getString(i).equalsIgnoreCase(AutomanApp.getApp().autoevents().get(pos).id())) {
                            contains = true;
                            Log.e("NOTICE", "THIS OBJECT ALREADY CONTAINS THIS ID");
                            break;
                        }
                    }
                    if (!contains) {
                        this.obj.autoevents().put(AutomanApp.getApp().autoevents().get(pos).id());
                        String str = "Select Event(s) - " + this.obj.autoevents().length() + " selected";
                        this.autoeventsview.setText(str);
                    }
                    Log.e("autoevents", " SELECTED!!! " + this.obj.autoevents().toString());
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
            case R.id.autoevents:
                this.obj.autoevents();
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
                this.autoaudit.getSelectedItem().toString().isEmpty()) {
            AutomanApp.getApp().showToast("Please fill form");
            return false;
        }
        try { // CHECK DATA SECURITY SETTINGS
            if(this.data.getText().toString().length() > 0) this.obj.data(new JSONObject(this.data.getText().toString()));
//            JSONObject res = AutomanApp.getApp().validateDataSecurity
//                    (autologs, add ? "add" : "edit", this.obj);
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

        ///////////////////////////////////////////////////////////
        //      OBJECTS - NOT SURE IF THIS IS EVEN NECESSARY
//        this.obj.security(((DataSecurity) this.security.getSelectedItem()).toJSON(true, null));

        ///////////////////////////////////////////////////////////
        //      ARRAYS
//        this.obj.autoevents(((AutoEvent) this.autoevents.getSelectedItems()).toJSON(true, null));


        Log.e("VALIDATED", this.obj.toJSON(true, null).toString());
        return true;
    }

    @Override
    protected void onActivityResult(int reqcode, int rescode, Intent data) {
        super.onActivityResult(reqcode, rescode, data);
        if (reqcode == 0 && rescode == RESULT_OK) {
            this.imgview.setImageBitmap((Bitmap) data.getExtras().get("data"));
        } else if (reqcode == 1 && rescode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            InputStream inputStream = null;
            if (ContentResolver.SCHEME_CONTENT.equals(selectedImage.getScheme())) {
                try {
                    inputStream = AutomanApp.getApp().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                if (ContentResolver.SCHEME_FILE.equals(selectedImage.getScheme())) {
                    try {
                        inputStream = new FileInputStream(selectedImage.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            GeneralAddEdit.get().img(BitmapFactory.decodeStream(inputStream));
            imgview.setImageBitmap(GeneralAddEdit.get().img());
        } else {
            AutomanApp.getApp().showToast("Cancelled");
        }
    }
}
