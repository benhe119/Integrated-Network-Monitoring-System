package automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.USER_DATA.User;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.EXTRA.GeneralDetails;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.Home;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.users;

/**
 * Created by F on 7/29/2017.
 */
public class UserDetails extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgview = null;

    private TextView username = null, fullname = null,
            gender = null, details = null, age = null, haddress = null, paddress = null, phone = null,
            email = null, cmeth = null, creAt = null;

    private User obj = null;
    private Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        this.setUpDetails();

    }

    private void setUpDetails() {
        try {
            this.obj = (User) GeneralDetails.get().genericSetUp(users, getIntent().getExtras().getString("id"),
                    (Activity) this, new SectionsPagerAdapter(getSupportFragmentManager()));

            // IMAGEVIEW & BUTTONS
//            this.imgview = (ImageView) findViewById(R.id.imgview);
//            this.imgview.setOnClickListener(this);
//             GeneralDetails.get().genericGetImage(obj.img(), this.imgview);

            this.username = (TextView) findViewById(R.id.username);
            this.username.setText(obj.username());
            this.fullname = (TextView) findViewById(R.id.fullname);
            this.fullname.setText(obj.fullName());
            this.details = (TextView) findViewById(R.id.details);
            this.details.setText(obj.details());
            this.details.setOnClickListener(this);
            this.gender = (TextView) findViewById(R.id.gender);
            this.gender.setText(obj.gender());
            this.age = (TextView) findViewById(R.id.age);
            String str = obj.age() + "";
            this.age.setText(str);
            this.haddress = (TextView) findViewById(R.id.haddress);
            this.haddress.setText(obj.haddress());
            this.paddress = (TextView) findViewById(R.id.paddress);
            this.paddress.setText(obj.paddress());
            this.phone = (TextView) findViewById(R.id.phone);
            this.phone.setText(obj.phone());
            this.email = (TextView) findViewById(R.id.email);
            this.email.setText(obj.email());
            this.cmeth = (TextView) findViewById(R.id.cmeth);
            this.cmeth.setText(obj.cmeth());
            this.creAt = (TextView) findViewById(R.id.creAt);
            this.creAt.setText(obj.creAt() != null ? obj.creAt().toString() : "-"); // CONVERT DATE TO STRING

            // NO NEED TO SHOW available / in_building / location

            /////////////////////////////////////////////////////
            ///     OBJECTS

            /////////////////////////////////////////////////////
            ///     ARRAYS

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (goToActivity) {
                this.i.putExtra("name", obj.fullName());
                startActivity(this.i);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
//        if(AutomanApp.getApp().CURRENT_STATE() != MYDATA){
//        if (true) {
//            MenuItem showdash = menu.add("Show More Options");
//            showdash.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (AutomanApp.getApp().isAuthorized("dashboardLogin")) {
//                        getMenuInflater().inflate(R.menu.dash_menu_user_details, menu);
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

    ///     ARRAYS -
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ListView listview;
        private static AutomanEnumerations[] fragments = {};
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
//                switch(currentFragment){
//                    case someAutoEnum:
//                        break;
//                }
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
//                    s = "SOMEPROP (" + ((User)GeneralDetails.get().getObj()).someprop().length() + ")";
                    break;
            }
            if (s.length() > 0) return str.concat(s);
            return null;
        }
    }
}
