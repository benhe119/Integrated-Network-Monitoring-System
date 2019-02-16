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
import automan.automanclientsmobile_android.MODEL.Stock;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.EXTRA.GeneralAddEdit;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.stocks;

public class AddEditStock extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText name = null, details = null, price = null, data = null;
    private TextView stockNameview = null;
    private Spinner stockName = null;
    private Button savebutton = null, cancelbutton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_stock);

        this.setUpDetails();
    }


    private Stock obj = null;
    private Boolean add = true;
    private ImageView imgview = null;

    private void setUpDetails() {

        this.name = (EditText) findViewById(R.id.name);
        this.details = (EditText) findViewById(R.id.details);
        this.price = (EditText) findViewById(R.id.price);
        this.data = (EditText) findViewById(R.id.data);
        //
        this.stockName = (Spinner) findViewById(R.id.stockName);
        this.stockName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                AutomanApp.getApp().settingsHandler().dashboardSettingsArray("stockNames")));
        this.stockName.setOnItemSelectedListener(this);
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

        this.obj = (Stock) GeneralAddEdit.get().genericSetUp(stocks, (Activity) this);

        switch (getIntent().getExtras().getString("addoredit")) {
            case "add":
                this.add = true;
                this.obj = new Stock();
                break;
            case "edit":
                this.add = false;

                this.name.setText(obj.name());
                this.details.setText(obj.details());
                String str = "" + obj.price();
                this.price.setText(str);
                this.data.setText(obj.data() != null ? obj.data().toString() : "");

                this.stockName.setSelection(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("stockNames").indexOf(this.obj.stockName()));

                try {
                    ////////////////////////////////////////////////////////////////////////////////
                    ///     OBJECTS - JUST SET SELECTED ITEM WITHIN THE SPINNER'S LIST


                    /////////////////////////////////////////////////////
                    ///     ARRAYS
                    ///      FIELDS
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
                            (new AutomanApp.Add(this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(stocks);
                        } else {
                            (new AutomanApp.Edit(this.obj.id() + "", this.obj, GeneralAddEdit.get().img(), (Activity) this)).execute(stocks);
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
                case R.id.stockName:
                    this.obj.stockName(AutomanApp.getApp().settingsHandler().dashboardSettingsArray("stockNames").get(pos));
                    Log.e("stockName", " SELECTED!!! " + this.obj.stockName());
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
                case R.id.stockName:
                    this.obj.stockName("");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean validate() {
        try {
            if (this.name.getText().toString().isEmpty() || this.details.getText().toString().isEmpty() ||
                    this.price.getText().toString().isEmpty() || ((String) this.stockName.getSelectedItem()).isEmpty()) {
                AutomanApp.getApp().showToast("Please fill form");
                return false;
            }

            try { // CHECK DATA SECURITY SETTINGS
                if (this.data.getText().toString().length() > 0)
                    this.obj.data(new JSONObject(this.data.getText().toString()));
//                JSONObject res = AutomanApp.getApp().validateDataSecurity
//                        (stocks, add ? "add" : "edit", this.obj);
//                if(!res.optBoolean("success", false)){
//                    AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Data Security");
//                    return false;
//                }
//                res = AutomanApp.getApp().validateSecurityLogin
//                        (stocks, add ? "add" : "edit", this.obj);
//                if(!res.optBoolean("success", false)){
//                    AutomanApp.getApp().showToast("Incomplete, Please Select " + res.getString("security") + " for Security");
//                    return false;
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            this.obj.name(this.name.getText().toString());
            this.obj.details(this.details.getText().toString());
            this.obj.price(Double.parseDouble(this.price.getText().toString()));

            /////////////////////////////////////////////////////////
            //  NOT SURE IF THIS (OBJECTS & ARRAYS) IS EVEN NECESSARY

            this.obj.stockName((String) this.stockName.getSelectedItem());

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
