package automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.support.v7.widget.Toolbar;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.EXTRA.GeneralDetails;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.Home;

import org.json.JSONException;

import java.io.Serializable;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.MYDATA;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autoevents;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autologs;

/**
 * Created by F on 6/4/2017.
 */
public class AutoEventDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView name = null, details = null, type = null, sourceType = null, source = null, autoaudit = null,
            autoevent = null, emergencyLvl = null, data = null, creAt = null, autologs = null;

    /// ADD OTHER SHIT
    // private ImageView imgview = null;

    private AutoEvent obj = null;
    private Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_event_details);

        this.setUpDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setUpDetails();
    }

    private void setUpDetails() {
        try {
            this.obj = (AutoEvent) GeneralDetails.get().genericSetUp(autoevents, getIntent().getExtras().getString("id"),
                    (Activity) this, new SectionsPagerAdapter(getSupportFragmentManager()));

            // IMAGEVIEW & BUTTONS
            // this.imgview = (ImageView) findViewById(R.id.imgview);
            // this.imgview.setOnClickListener(this);
            // GeneralDetails.get().genericGetImage(obj.img(), this.imgview);

            this.name = (TextView) findViewById(R.id.name);
            this.name.setText(obj.name());
            this.details = (TextView) findViewById(R.id.details);
            this.details.setText(obj.details());
            this.details.setOnClickListener(this);
            this.type = (TextView) findViewById(R.id.type);
            this.type.setText(obj.type());
            this.sourceType = (TextView) findViewById(R.id.sourceType);
            this.sourceType.setText(obj.sourceType());
            this.source = (TextView) findViewById(R.id.source);
            this.source.setText(obj.source());
            this.autoaudit = (TextView) findViewById(R.id.autoaudit);
            this.autoaudit.setText(obj.autoaudit());
            this.autoevent = (TextView) findViewById(R.id.autoevent);
            this.autoevent.setText(obj.autoevent());
            this.emergencyLvl = (TextView) findViewById(R.id.emergencyLvl);
            this.emergencyLvl.setText(obj.emergencyLvl());
            this.data = (TextView) findViewById(R.id.data);
            this.data.setText(obj.data() != null ? obj.data().toString() : "-");
            this.data.setOnClickListener(this);
            this.creAt = (TextView) findViewById(R.id.creAt);
            this.creAt.setText(obj.creAt() != null ? obj.creAt().toString() : "-"); // CONVERT DATE TO STRING

            /////////////////////////////////////////////////////
            ///     OBJECTS

            /////////////////////////////////////////////////////
            ///     ARRAYS
            ///     AUTOLOGS FIELDS
//            this.autologs = (TextView) findViewById(R.id.autologs);
//            this.autologs.setText("View autologs (" + obj.autologs().length() + ")");
//            this.autologs.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        Boolean goToActivity = true;
        try {
            switch (v.getId()) {
                case R.id.details:
                    goToActivity = false; // DO THIS SO NO NEW ACTIVITY IS OPENED
                    GeneralDetails.get().genericHandleDetailsOptions(v.getId()); // THIS IS JUST FOR THE details PROP
                    break;
                case R.id.data:
                    goToActivity = false; // DO THIS SO NO NEW ACTIVITY IS OPENED
                    GeneralDetails.get().genericHandleDetailsOptions(R.id.showAutoLogOrAutoEventData); // THIS IS JUST FOR THE details PROP
                    break;
//                case R.id.autologs:
//                    this.i = new Intent(AutoEventDetails.this, InnerAutoLogs.class);
//                    this.i.putExtra("data", (Serializable) obj.autologs());
//                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (goToActivity) {
                this.i.putExtra("name", obj.name());
                startActivity(this.i);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_autoaudit_details, menu);
//        if(AutomanApp.getApp().CURRENT_STATE() != MYDATA){
//        if (true) {
//            MenuItem showdash = menu.add("Show More Options");
//            showdash.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (AutomanApp.getApp().isAuthorized("dashboardLogin")) {
//                        getMenuInflater().inflate(R.menu.dash_menu_autoaudit_details, menu);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return GeneralDetails.get().genericHandleDetailsOptions(item.getItemId()) ? true : super.onOptionsItemSelected(item);
    }

    ///     ARRAYS - AUTOLOGS
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ListView listview;
        private static AutomanEnumerations[] fragments = {AutomanEnumerations.autologs};
        // THESE FRAGMENTS MUST BE IN THE SAME ORDER AS THE TITLES GIVEN TO THE VIEW PAGER
        private static AutomanEnumerations currentFragment;

        public static AutomanEnumerations getCurrentFragment() {
            return getCurrentFragment(GeneralDetails.get().mViewPager().getCurrentItem());
        }

        public static AutomanEnumerations getCurrentFragment(int sectionNumber) {
            currentFragment = fragments[sectionNumber];
            return currentFragment;
        }

        public static ListView getCurrentFragmentListView() {
            return listview;
        }

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            //
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            currentFragment = fragments[sectionNumber];

            // HOWEVER, THE UI COMPONENTS GET SET WITH DATA THE WAY WE WANT IT (REAL-TIME)
            // ACCESS THE CURRENT FRAGMENT BE PASSING THE SECTION NUMBER TO FRAGMENTS ARRAY AS AN INDEX
            listview = (ListView) rootView.findViewById(R.id.listview);
            try {
                switch(currentFragment){
                    case autologs:
                        Home.gAudit().setupSubListView(currentFragment, listview, ((AutoEvent)GeneralDetails.get().getObj()).autologs());
                        break;
                }
                registerForContextMenu(listview);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inf = getMenuInflater();
        Home.gAudit().setupContextMenu(PlaceholderFragment.getCurrentFragment(), inf, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (Home.gAudit().setupOnContextMenuItemSelected(item, info))
            return true;
        else return super.onContextItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }
        @Override
        public int getCount() {
            return PlaceholderFragment.fragments.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String str = "RELATED ", s = "";
            switch (position) {
                case 0:
                    s = "LOGS (" + ((AutoEvent)GeneralDetails.get().getObj()).autologs().length() + ")";
                    break;
            }
            if(s.length() > 0) return str.concat(s);
            return null;
        }
    }
}
