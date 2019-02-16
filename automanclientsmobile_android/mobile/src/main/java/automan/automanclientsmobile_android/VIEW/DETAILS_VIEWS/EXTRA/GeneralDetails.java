package automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.EXTRA;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoAudit;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;
import automan.automanclientsmobile_android.MODEL.Stock;
import automan.automanclientsmobile_android.MODEL.USER_DATA.Person;
import automan.automanclientsmobile_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.call;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.projects;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.sms;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.users;

/**
 * Created by F on 6/18/2017.
 */
public class GeneralDetails {

    private AutomanEnumerations detailsOpt = null;
    private String detailsId = "";
    private AutoObject detailsObj = null;

    public AutoObject getObj() {
        return this.detailsObj;
    }

    private Activity detailsAct = null;
    private Intent i = null;

    private FragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public ViewPager mViewPager() {
        return this.mViewPager;
    }

    private TabLayout tabLayout;

    public TabLayout tabLayout() {
        return this.tabLayout;
    }

    private ListView listView = null;

    private static GeneralDetails generalDetails = new GeneralDetails();

    public static GeneralDetails get() {
        return generalDetails;
    }

    public AutoObject genericSetUp(AutomanEnumerations opt, String id, Activity act, FragmentPagerAdapter mSectionsPagerAdapter) {
        this.detailsOpt = opt;
        if ((opt == users) && (id.equals("User Profile")))
            this.detailsObj = AutomanApp.getApp().getUser();
        else this.detailsObj = AutomanApp.getApp().find(this.detailsOpt, id);
        this.detailsId = id;
        this.detailsAct = act;

        // SET UP VIEW PAGER AND TABS
        this.mSectionsPagerAdapter = mSectionsPagerAdapter;
        // Set up the ViewPager with the sections adapter.
        this.mViewPager = (ViewPager) act.findViewById(R.id.arrayscontainer);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        // Set up the Tab Layout with the view pager
        this.tabLayout = (TabLayout) act.findViewById(R.id.arrays);
        this.tabLayout.setupWithViewPager(this.mViewPager);

        Log.e(this.detailsOpt.toSingularUpperCase(this.detailsOpt), "DETAILS PAGE -> " + this.detailsObj.toJSON(true, null).toString());

        return this.detailsObj;
    }

    public void genericGetImage(String img, ImageView imgview) {
//        AutomanApp.getApp().getImage(this.detailsOpt, img, this.detailsAct, imgview);
    }

    public boolean genericHandleDetailsOptions(int id) {
        String function = this.getIdAsString(id);
        Log.e(this.detailsOpt.toSingularUpperCase(this.detailsOpt), "OPTION CLICKED -> " + function);
        switch (id) {
            case R.id.details:
                AutomanApp.ShowDialog.showLargeData(this.detailsOpt, "Details", this.detailsObj.details());
                return true;
            case R.id.showAutoLogOrAutoEventData:
                if (this.detailsObj instanceof AutoAudit) {
                    AutomanApp.ShowDialog.showAutoLogOrAutoEventDataDialog(this.detailsOpt, "" + this.detailsId,
                            (AutoAudit) this.detailsObj, this.detailsAct);
                }
                return true;
            case R.id.showStockData:
//                if (this.detailsObj instanceof Stock) {
//                    AutomanApp.ShowDialog.showStockDataDialog(this.detailsOpt, "" + this.detailsId,
//                            (Stock) this.detailsObj, this.detailsAct);
//                }
                return true;
            default:
                try {
                    if (AutomanApp.getApp().isAuthorized(function, detailsOpt.toSingularLowerCase(detailsOpt))) {
                        switch (id) {
                            case R.id.edit:
                                // OPEN EDITING DIALOG
                                Class c = this.genericGetAddEditClass(this.detailsOpt);
                                if (c != null) {
                                    this.i = new Intent(this.detailsAct, c);
                                    this.i.putExtra("addoredit", function); // itemId WILL BE "edit" TRUST
                                    this.i.putExtra("id", this.detailsObj.id());
                                    this.detailsAct.startActivity(this.i);
                                } else AutomanApp.getApp().showToast("Sorry, some error occurred");
                                return true;
                            case R.id.delete:
                                AutomanApp.ShowDialog.showDeleteDialog(this.detailsOpt, "" + this.detailsId, this.detailsAct);
                                return true;
                            default:
                                String params = null;
                                AutomanApp.ShowDialog.showSpecialFunctionDialog(this.detailsOpt, this.detailsId, function,
                                        (!Arrays.asList("fire", "cancel", "delete").contains(function)) ? null : this.detailsAct, params);
                                return true;
                        }
                    }
                } catch (Exception e) {
                    Log.e("NOTICE", "SOME ERROR HAS OCCURED ON A CLICK EVENT");
                    e.printStackTrace();
                }
                return true;
        }
    }

    private Class genericGetAddEditClass(AutomanEnumerations opt) {
        try {
            return Class.forName("automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.AddEdit" + opt.toSingularCamelCase(opt));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getIdAsString(int id) {
        switch (id) {
            case R.id.details:
                return "details";
            case R.id.showAutoLogOrAutoEventData:
                return "showAutoLogOrAutoEventData";
            case R.id.showStockData:
                return "showStockData";
            //
            case R.id.add:
                return "add";
            case R.id.edit:
                return "edit";
            case R.id.delete:
                return "delete";
            case R.id.contact:
                return "contact";
            case R.id.handle:
                return "handle"; // FOR AUTOLOG / AUTOEVENT
            case R.id.option:
                return "option"; // FOR STOCK
            default:
                return "";
        }
    }


}
