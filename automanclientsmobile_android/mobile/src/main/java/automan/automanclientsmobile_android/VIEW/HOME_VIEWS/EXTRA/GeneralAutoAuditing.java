package automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.Stock;
import automan.automanclientsmobile_android.MODEL.USER_DATA.User;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.AddEditAutoEvent;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.AddEditAutoLog;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.AddEditStock;
import automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.AddEditUser;
import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.*;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS.AutoEventAdapter;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS.AutoLogAdapter;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS.StockAdapter;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS.UserAdapter;
import automan.automanclientsmobile_android.VIEW.SETTINGS_VIEWS.UserProfile;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.*;

/**
 * Created by F on 6/15/2017.
 */
public class GeneralAutoAuditing {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private FragmentPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public ViewPager mViewPager() {
        return this.mViewPager;
    }

    private TabLayout tabLayout;

    public TabLayout tabLayout() {
        return this.tabLayout;
    }

    private ListView listView = null;

    private Intent i = null;
    private AppCompatActivity act = null;
    private String title = "";
    private Toolbar toolbar = null;

    public Toolbar getToolbar() {
        return this.toolbar;
    }

    public void setState(AutomanEnumerations state) {
        AutomanApp.getApp().CURRENT_STATE(state); // FIRST SET THE CURRENT STATE OF THE APP
    }

    public GeneralAutoAuditing(AutomanEnumerations state, AppCompatActivity act, String title, FragmentPagerAdapter mSectionsPagerAdapter) {
        this.setState(state); // FIRST SET THE CURRENT STATE OF THE APP
        this.setUpStuff(act, title, mSectionsPagerAdapter);
    }

    public void genericGetImage(String img, ImageView imgview) {
//         AutomanApp.getApp().getImage(this.homeOpt, img, this.act, imgview);
    }

    public void setUpStuff(AppCompatActivity act, String title, FragmentPagerAdapter mSectionsPagerAdapter) {
        this.act = act; // SET UP THE CURRENT ACTIVITY
        this.title = title;

        // SET UP THE TOOLBAR
        this.toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        this.toolbar.setTitle(this.title);
        this.act.setSupportActionBar(this.toolbar);

        // SET UP VIEW PAGER AND TABS
        this.mSectionsPagerAdapter = mSectionsPagerAdapter;
        // Set up the ViewPager with the sections adapter.
        this.mViewPager = (ViewPager) act.findViewById(R.id.container);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        // Set up the Tab Layout with the view pager
        this.tabLayout = (TabLayout) act.findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.mViewPager);

        this.mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(this.mViewPager));

    }

    public void gotoAddEdit(AutomanEnumerations sth, String addedit) {
        try {
            Class c = Class.forName("automan.automanclientsmobile_android.VIEW.ADDEDIT_VIEWS.AddEdit" + sth.toSingularCamelCase(sth));
            Log.e(sth.toSingularUpperCase(sth) + " ADDEDIT (" + addedit + ") CLICKED -> ", c.getName() + "; " + c.toString());
            i = new Intent(AutomanApp.getApp().getCurrentActivity(), c);
            i.putExtra("addoredit", addedit);
            AutomanApp.getApp().getCurrentActivity().startActivity(i);
        } catch (ClassNotFoundException e) {
            Log.e("NOTICE", "SOME ERROR HAS OCCURED ON A CLICK EVENT");
            e.printStackTrace();
        }
    }

    public void setupListView(AutomanEnumerations sth, ListView l) {
        switch (sth) {
            case autologs:
                l.setAdapter(new AutoLogAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().autologs()));
                break;
            case autoevents:
                l.setAdapter(new AutoEventAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().autoevents()));
                break;
            case users:
                l.setAdapter(new UserAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().users()));
                break;
            case stocks:
                l.setAdapter(new StockAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().stocks()));
                break;
            default:
        }
    }

    public void setupContextMenu(AutomanEnumerations sth, MenuInflater inf, ContextMenu menu) {
        switch (sth) {
            case autologs:
                inf.inflate(R.menu.autologcontextmenu, menu);
                break;
            case autoevents:
                inf.inflate(R.menu.autoeventcontextmenu, menu);
                break;
            case users:
                inf.inflate(R.menu.usercontextmenu, menu);
                break;
            case stocks:
                inf.inflate(R.menu.stockcontextmenu, menu);
                break;
            default:
        }
    }

    public void setupSubListView(AutomanEnumerations sth, ListView l, JSONArray jsonarr) {
        switch (sth) {
            case autologs:
                final ArrayList<AutoLog> arr1 = AutomanApp.getApp().jsonToClassArray(sth, jsonarr);
                l.setAdapter(new AutoLogAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1, arr1));
                break;
            case autoevents:
                final ArrayList<AutoEvent> arr2 = AutomanApp.getApp().jsonToClassArray(sth, jsonarr);
                l.setAdapter(new AutoEventAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1, arr2));
                break;
            case users:
                final ArrayList<User> arr3 = AutomanApp.getApp().jsonToClassArray(sth, jsonarr);
                l.setAdapter(new UserAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1, arr3));
                break;
            case stocks:
                final ArrayList<Stock> arr4 = AutomanApp.getApp().jsonToClassArray(sth, jsonarr);
                l.setAdapter(new StockAdapter(sth, AutomanApp.getApp().getCurrentActivity(), android.R.layout.simple_list_item_1, arr4));
                break;
            default:
        }
    }

    public void setupSpinnerView(AutomanEnumerations sth, Spinner l) {
        switch (sth) {
            case autologs:
                l.setAdapter(new AutoLogAdapter(sth, this.act, R.layout.spinnerlistitem, R.id.name,
                        AutomanApp.getApp().autologs(), "object"));
                break;
            case autoevents:
                l.setAdapter(new AutoEventAdapter(sth, this.act, R.layout.spinnerlistitem, R.id.name,
                        AutomanApp.getApp().autoevents(), "object"));
                break;
            case users:
                l.setAdapter(new UserAdapter(sth, this.act, R.layout.spinnerlistitem, R.id.name,
                        AutomanApp.getApp().users(), "object"));
                break;
            case stocks:
                l.setAdapter(new StockAdapter(sth, this.act, R.layout.spinnerlistitem, R.id.name,
                        AutomanApp.getApp().stocks(), "object"));
                break;
            default:
        }
    }

    public void genericDoSearch(AutomanEnumerations sth, ListView l, final String query) {
        switch (sth) {
            case autologs:
                l.setAdapter(new AutoLogAdapter(sth, this.act, android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().filter(AutomanApp.getApp().autologs(), query)));
                break;
            case autoevents:
                l.setAdapter(new AutoEventAdapter(sth, this.act, android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().filter(AutomanApp.getApp().autoevents(), query)));
                break;
            case users:
                l.setAdapter(new UserAdapter(sth, this.act, android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().filter(AutomanApp.getApp().users(), query)));
                break;
            case stocks:
                l.setAdapter(new StockAdapter(sth, this.act, android.R.layout.simple_list_item_1,
                        AutomanApp.getApp().filter(AutomanApp.getApp().stocks(), query)));
                break;

            default:
        }
    }

    public Boolean setupOnContextMenuItemSelected(MenuItem item, AdapterView.AdapterContextMenuInfo info) {
        // info.position MIGHT NOT BE THE CORRECT INDEX
        String params = "", function = "";
        switch (item.getItemId()) {
            /// STOCK MENU
            case R.id.stockview:
                i = new Intent(AutomanApp.getApp().getCurrentActivity(), StockDetails.class);
                i.putExtra("id", AutomanApp.getApp().stocks().get(info.position).id() + "");
                act.startActivity(i);
                return true;
            case R.id.editstock:
                if (AutomanApp.getApp().isAuthorized("edit", "stock")) {
                    // OPEN EDITING DIALOG
                    i = new Intent(act, AddEditStock.class);
                    i.putExtra("addoredit", "edit");
                    i.putExtra("id", AutomanApp.getApp().stocks().get(info.position).id());
                    act.startActivity(i);
                }
                return true;
            case R.id.deletestock:
                if (AutomanApp.getApp().isAuthorized("delete", "stock")) {
                    // OPEN DELETING DIALOG
                    AutomanApp.ShowDialog.showDeleteDialog(stocks, "" +
                            AutomanApp.getApp().stocks().get(info.position).id(), null);
                }
                return true;
            case R.id.optionstock:
                params = null; function = "option";
                AutomanApp.ShowDialog.showSpecialFunctionDialog(stocks, AutomanApp.getApp().stocks().get(info.position).id(), function,
                        (!Arrays.asList("fire", "cancel", "delete").contains(function)) ? null : this.act, params);
                return true;
            /// AUTOLOG MENU
            case R.id.autologview:
                i = new Intent(AutomanApp.getApp().getCurrentActivity(), AutoLogDetails.class);
                i.putExtra("id", AutomanApp.getApp().autologs().get(info.position).id() + "");
                act.startActivity(i);
                return true;
            case R.id.editautolog:
                if (AutomanApp.getApp().isAuthorized("edit", "autolog")) {
                    // OPEN EDITING DIALOG
                    i = new Intent(act, AddEditAutoLog.class);
                    i.putExtra("addoredit", "edit");
                    i.putExtra("id", AutomanApp.getApp().autologs().get(info.position).id());
                    act.startActivity(i);
                }
                return true;
            case R.id.deleteautolog:
                if (AutomanApp.getApp().isAuthorized("delete", "autolog")) {
                    // OPEN DELETING DIALOG
                    AutomanApp.ShowDialog.showDeleteDialog(autologs, "" +
                            AutomanApp.getApp().autologs().get(info.position).id(), null);
                }
                return true;
            case R.id.handleautolog:
                params = null; function = "handle";
                AutomanApp.ShowDialog.showSpecialFunctionDialog(autologs, AutomanApp.getApp().autologs().get(info.position).id(), function,
                        (!Arrays.asList("fire", "cancel", "delete").contains(function)) ? null : this.act, params);
                return true;

            /// AUTOEVENT MENU
            case R.id.autoeventview:
                i = new Intent(AutomanApp.getApp().getCurrentActivity(), AutoEventDetails.class);
                i.putExtra("id", AutomanApp.getApp().autoevents().get(info.position).id() + "");
                act.startActivity(i);
                return true;
            case R.id.editautoevent:
                if (AutomanApp.getApp().isAuthorized("edit", "autoevent")) {
                    // OPEN EDITING DIALOG
                    i = new Intent(act, AddEditAutoEvent.class);
                    i.putExtra("addoredit", "edit");
                    this.i.putExtra("id", AutomanApp.getApp().autoevents().get(info.position).id());
                    act.startActivity(i);
                }
                return true;
            case R.id.deleteautoevent:
                if (AutomanApp.getApp().isAuthorized("delete", "autoevent")) {
                    // OPEN DELETING DIALOG
                    AutomanApp.ShowDialog.showDeleteDialog(autoevents, "" + AutomanApp.getApp().autoevents().get(info.position).id(), null);
                }
                return true;
            case R.id.handleautoevent:
                params = null; function = "handle";
                AutomanApp.ShowDialog.showSpecialFunctionDialog(autoevents, AutomanApp.getApp().autoevents().get(info.position).id(), function,
                        (!Arrays.asList("fire", "cancel", "delete").contains(function)) ? null : this.act, params);
                return true;

            /// USER MENU
            case R.id.userview:
                i = new Intent(AutomanApp.getApp().getCurrentActivity(), UserDetails.class);
                i.putExtra("id", AutomanApp.getApp().users().get(info.position).id() + "");
                act.startActivity(i);
                return true;
            case R.id.edituser:
                if (AutomanApp.getApp().isAuthorized("edit", "user")) {
                    // OPEN EDITING DIALOG
                    i = new Intent(act, AddEditUser.class);
                    i.putExtra("addoredit", "edit");
                    this.i.putExtra("id", AutomanApp.getApp().users().get(info.position).id());
                    act.startActivity(i);
                }
                return true;
            case R.id.deleteuser:
                if (AutomanApp.getApp().isAuthorized("delete", "user")) {
                    // OPEN DELETING DIALOG
                    AutomanApp.ShowDialog.showDeleteDialog(users, "" + AutomanApp.getApp().users().get(info.position).id(), null);
                }
                return true;

            default:
                return false;
        }
    }

}
