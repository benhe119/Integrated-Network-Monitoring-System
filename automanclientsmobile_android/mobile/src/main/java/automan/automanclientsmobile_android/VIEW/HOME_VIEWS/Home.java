package automan.automanclientsmobile_android.VIEW.HOME_VIEWS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.GeneralAutoAuditing;
import automan.automanclientsmobile_android.VIEW.SETTINGS_VIEWS.UserProfile;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.AUTO_AUDITING;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.DASHBOARD;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autoaudits;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autoevents;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autologs;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.stocks;
import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.users;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static GeneralAutoAuditing gAudit = null;

    public static GeneralAutoAuditing gAudit() {
        return gAudit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Home.gAudit = new GeneralAutoAuditing(DASHBOARD, this, "AUTO-AUDITING",
                new SectionsPagerAdapter(getSupportFragmentManager()));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                  NAVIGATION DRAWER CODE
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, Home.gAudit.getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle); // DEPRECATED
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // EDIT THIS VIEW IN ORDER TO THE MATCH THE CURRENT MYDATA_STATE
        navigationView.setNavigationItemSelectedListener(this);
        //
        View navigationHeaderView = navigationView.getHeaderView(0); // R.id.nav_header_home
        //
        ImageView userImg = (ImageView) navigationHeaderView.findViewById(R.id.navheaderimgview);
        /// SET IMAGE VIEW TO THE USER'S IMAGE (GET FROM AUTOMAN SERVER FILE SYSTEM)
        AutomanApp.getApp().getImage(users, AutomanApp.getApp().getUser().img(), (Activity) Home.this, userImg);
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SEND USER TO PROFILE ACTIVITY
                startActivity(new Intent(Home.this, UserProfile.class));
            }
        });
        TextView usernameview = (TextView) navigationHeaderView.findViewById(R.id.navheaderusernameview);
        usernameview.setText(AutomanApp.getApp().getUser().username());
        TextView emailview = (TextView) navigationHeaderView.findViewById(R.id.navheaderemailview);
        emailview.setText(AutomanApp.getApp().getUser().email());
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.gAudit.gotoAddEdit(PlaceholderFragment.getCurrentFragment(), "add");
            }
        });
    }

    @Override
    public void onBackPressed() {
        long bp = 0;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Home.gAudit.mViewPager().getCurrentItem() == 0) {
                if ((bp + 2000) > System.currentTimeMillis()) {
//                    super.onBackPressed();
                    // LOGOUT
                    if (AutomanApp.getApp().server().logout()) {
                        Home.this.finish();
                        // If the user is currently looking at the first step, allow the system to handle the
                        // Back button. This calls finish() on this activity and pops the back stack.
                        super.onBackPressed();
                    }
                } else {
                    AutomanApp.getApp().showToast("Press again to log out");
                }
                bp = System.currentTimeMillis();
            } else { // Otherwise, select the previous step.
                Home.gAudit.mViewPager().setCurrentItem(Home.gAudit.mViewPager().getCurrentItem() - 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.search:
//                this.doSearch("");
//                // ON SEARCH STATE CANCELLED
//                Home.gAudit.setupListView(PlaceholderFragment.getCurrentFragment(), PlaceholderFragment.getCurrentFragmentListView());
                return true;
            case R.id.refresh:
                this.doRefresh();
                return true;
            case R.id.add:
                Home.gAudit.gotoAddEdit(PlaceholderFragment.getCurrentFragment(), "add");
                return true;
            case R.id.profile:
                startActivity(new Intent(Home.this, UserProfile.class));
                return true;
            case R.id.logout:
                AutomanApp.getApp().logout(this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSearch(final String query) {
        Home.gAudit.genericDoSearch(PlaceholderFragment.getCurrentFragment(), PlaceholderFragment.getCurrentFragmentListView(), query);
    }

    private void doRefresh() {
        Log.e("REFRESH", "ABOUT TO REFRESH DATA NOW ...");
        AutomanApp.getApp().getData("dashboard");
        /// THEN RESET ALL LISTVIEWS
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profilenav) { // DO SAME SHIT FOR NAV HEADER'S onClick EVENT
            startActivity(new Intent(Home.this, UserProfile.class));
        } else if (id == R.id.autolognav) {
            Home.gAudit.mViewPager().setCurrentItem(0);
        } else if (id == R.id.autoeventnav) {
            Home.gAudit.mViewPager().setCurrentItem(1);
        } else if (id == R.id.usernav) {
            Home.gAudit.mViewPager().setCurrentItem(2);
        } else if (id == R.id.stocknav) {
            Home.gAudit.mViewPager().setCurrentItem(3);
        } else if (id == R.id.logout) {
            AutomanApp.getApp().logout(this);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";
        // THESE FRAGMENTS MUST BE IN THE SAME ORDER AS THE TITLES GIVEN TO THE VIEW PAGER
        private static AutomanEnumerations[] fragments = {autologs, autoevents, users, stocks};
        private static AutomanEnumerations currentFragment;

        public static AutomanEnumerations getCurrentFragment() {
            return getCurrentFragment(Home.gAudit.mViewPager().getCurrentItem());
        }

        public static AutomanEnumerations getCurrentFragment(int sectionNumber) {
            currentFragment = fragments[sectionNumber];
            return currentFragment;
        }

        private static ListView lview = null;

        public static ListView getCurrentFragmentListView() {
            return lview;
        }

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            lview = (ListView) rootView.findViewById(R.id.listview);
            Home.gAudit.setupListView(currentFragment, lview);

            registerForContextMenu(lview);
            return rootView;
        }

    }

    //  NOW, SETUP THE CONTEXT MENU FUNCTIONALITY ON THE LISTVIEWS' ITEMS

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inf = getMenuInflater();
        Home.gAudit.setupContextMenu(PlaceholderFragment.getCurrentFragment(), inf, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (Home.gAudit.setupOnContextMenuItemSelected(item, info))
            return true;
        else return super.onContextItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show number of total pages.
            return PlaceholderFragment.fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LOGS";
                case 1:
                    return "EVENTS";
                case 2:
                    return "USERS";
                case 3:
                    return "STOCKS";
            }
            return null;
        }
    }
}
