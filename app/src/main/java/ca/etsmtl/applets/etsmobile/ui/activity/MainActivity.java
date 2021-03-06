package ca.etsmtl.applets.etsmobile.ui.activity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;

import ca.etsmtl.applets.etsmobile.ApplicationManager;
import ca.etsmtl.applets.etsmobile.http.DataManager;
import ca.etsmtl.applets.etsmobile.model.MyMenuItem;
import ca.etsmtl.applets.etsmobile.service.RegistrationIntentService;
import ca.etsmtl.applets.etsmobile.ui.adapter.MenuAdapter;
import ca.etsmtl.applets.etsmobile.ui.fragment.AboutFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.BandwithFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.BiblioFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.BottinFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.CommentairesFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.FAQFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.HoraireFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.MonETSFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.MoodleFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.NewsFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.NotesFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.OtherAppsFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.ProfilFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.SecuriteFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.SponsorsFragment;
import ca.etsmtl.applets.etsmobile.ui.fragment.TodayFragment;
import ca.etsmtl.applets.etsmobile.util.Constants;
import ca.etsmtl.applets.etsmobile.util.SecurePreferences;
import ca.etsmtl.applets.etsmobile2.R;
import io.supportkit.core.User;
import io.supportkit.ui.ConversationActivity;

/**
 * Main Activity for �TSMobile, handles the login and the menu
 *
 * @author Philippe David
 */
public class MainActivity extends Activity {

    public static LinkedHashMap<String, MyMenuItem> mMenu = new LinkedHashMap<String, MyMenuItem>(17);
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment mfragment;
    private String TAG = "FRAGMENTTAG";
    private AccountManager accountManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isGCMTokenSent;
    private SecurePreferences securePreferences;
    public SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        checkPlayServices();
        setLocale();
        setTitles();
        if (savedInstanceState != null) {
            instantiateFragments(savedInstanceState);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountManager = AccountManager.get(this);

        isGCMTokenSent = sharedPreferences.getBoolean(Constants.IS_GCM_TOKEN_SENT_TO_SERVER, false);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                isGCMTokenSent = sharedPreferences.getBoolean(Constants.IS_GCM_TOKEN_SENT_TO_SERVER, false);
            }
        };



        // Set the adapter for the list view
        int stringSet = mMenu.keySet().size();
        final Collection<MyMenuItem> myMenuItems = mMenu.values();

        MyMenuItem[] menuItems = new MyMenuItem[stringSet];
        mDrawerList.setAdapter(new MenuAdapter(this, myMenuItems.toArray(menuItems)));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                //R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.drawer_title));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        securePreferences = new SecurePreferences(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(ApplicationManager.userCredentials == null){
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

    }

    @Override
    protected void onStart() {
        super.onStart();
        DataManager.getInstance(this).start();
    }

    @Override
    protected void onStop() {
        DataManager.getInstance(this).stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //In case of : retry registering to GCM
        if (!isGCMTokenSent && ApplicationManager.domaine != null) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));

        if (ApplicationManager.userCredentials == null) {
            if (mfragment == null) {
                selectItem(AboutFragment.class.getName());
            } else {
                MyMenuItem myMenuItem = mMenu.get(mfragment.getTag());
                if (myMenuItem.hasToBeLoggedOn()) {
                    selectItem(AboutFragment.class.getName());
                }
                selectItem(mfragment.getTag());
            }
        } else {
            if (mfragment == null) {
                selectItem(TodayFragment.class.getName());
            }

        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager manager = getFragmentManager();
        if (mfragment != null || mfragment.isAdded()) {
            manager.putFragment(outState, mfragment.getTag(), mfragment);
            outState.putString(TAG, mfragment.getTag());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        instantiateFragments(savedInstanceState);
    }

    private void instantiateFragments(Bundle savedInstanceState) {
        MyMenuItem ajdItem = mMenu.get(TodayFragment.class.getName());

        // Select Aujourd'Hui
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getFragmentManager();
            String tag = savedInstanceState.getString(TAG);
            mfragment = fragmentManager.getFragment(savedInstanceState, tag);

        } else {
            selectItem(ajdItem.mClass.getName());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            User user = User.getCurrentUser();
            user.setEmail(accountName);

            ConversationActivity.show(this);

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            ApplicationManager.deconnexion(this);
        }
        if (id == R.id.action_language){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.lang_title));
            builder.setMessage(getResources().getString(R.string.lang_description));
            builder.setPositiveButton(getResources().getString(R.string.lang_choix_positif),
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    prefs.edit().remove("language").apply();
                    prefs.edit().putString("language", "fr").apply();
                    mMenu = new LinkedHashMap<String, MyMenuItem>(17);
                    recreate();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.lang_choix_negatif),
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    prefs.edit().remove("language").apply();
                    prefs.edit().putString("language", "en").apply();
                    mMenu = new LinkedHashMap<String, MyMenuItem>(17);
                    recreate();
                }
            });
            builder.show();
        }

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocale() {
        Locale locale;
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        prefs = this.getSharedPreferences("Language", 0);
        String restoredText = prefs.getString("language", "");
        if (restoredText.equalsIgnoreCase("en")) {
            locale = Locale.ENGLISH;
        }
        else if(restoredText.equalsIgnoreCase("fr"))
            locale = Locale.CANADA_FRENCH;
        else
        locale = Locale.getDefault();
        Locale.setDefault(locale);
        //conf = new Configuration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @SuppressWarnings("rawtypes")
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            MyMenuItem myMenuItem = (MyMenuItem) parent.getItemAtPosition(position);

            if (myMenuItem.resId == R.drawable.ic_ico_comment) {
                // Opens SupportKit with selected user account
                selectAccount();
            } else {
                selectItem(myMenuItem.mClass.getName());
            }
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    @SuppressWarnings("rawtypes")
    private void selectItem(String key) {
        Fragment fragment = null;
        MyMenuItem myMenuItem = mMenu.get(key);


        if (myMenuItem.hasToBeLoggedOn() && ApplicationManager.userCredentials == null) {

            final AccountManagerFuture<Bundle> future = accountManager.addAccount(Constants.ACCOUNT_TYPE, Constants.AUTH_TOKEN_TYPE, null, null, MainActivity.this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    //Login successful
                }
            }, null);

        } else {
            Class aClass = myMenuItem.mClass;
            try {
                fragment = (Fragment) aClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Insert the fragment by replacing any existing fragment
            String biblio = "ca.etsmtl.applets.etsmobile.ui.fragment.BiblioFragment";
            String monETS = "ca.etsmtl.applets.etsmobile.ui.fragment.MonETSFragment";

            if(aClass.getName().equals(biblio)) {
                String url = getString(R.string.url_biblio);
                Intent internetIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(internetIntent);
            }else if(aClass.getName().equals(monETS)){
                String url = "https://portail.etsmtl.ca/";
                Intent internetIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(internetIntent);

            } else{
                mfragment = fragment;
                getFragmentManager().beginTransaction().replace(R.id.content_frame, mfragment, aClass.getName())
                        .addToBackStack(aClass.getName()).commit();

                // Update the title, and close the drawer
                setTitle(mMenu.get(key).title);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        }

    }

    /**
     * Asks the user to pick a Google account so that we can have his email in slack support with
     * SupportKit
     */
    private void selectAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
        startActivityForResult(intent, Constants.REQUEST_CODE_EMAIL);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    public void setTitles(){
        // Section 1 - Moi
        mMenu.put(getString(R.string.menu_section_1_moi),
                new MyMenuItem(
                        getString(R.string.menu_section_1_moi),
                        null
                ));

        mMenu.put(TodayFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_1_ajd),
                        TodayFragment.class,
                        R.drawable.ic_ico_aujourdhui,
                        true
                ));

        mMenu.put(HoraireFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_1_horaire),
                        HoraireFragment.class,
                        R.drawable.ic_ico_schedule,
                        true
                ));

        mMenu.put(NotesFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_1_notes),
                        NotesFragment.class,
                        R.drawable.ic_ico_notes,
                        true
                ));

        mMenu.put(MoodleFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_2_moodle),
                        MoodleFragment.class,
                        R.drawable.ic_moodle_icon_small,
                        true
                ));

        mMenu.put(ProfilFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_1_profil),
                        ProfilFragment.class,
                        R.drawable.ic_ico_profil,
                        true
                ));

        mMenu.put(MonETSFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_1_monETS),
                        MonETSFragment.class,
                        R.drawable.ic_monets,
                        true
                ));

        mMenu.put(BandwithFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_1_bandwith),
                        BandwithFragment.class,
                        R.drawable.ic_ico_internet,
                        false
                ));


        // Section 2 - ÉTS
        mMenu.put(getString(R.string.menu_section_2_ets),
                new MyMenuItem(
                        getString(R.string.menu_section_2_ets),
                        null
                ));

        mMenu.put(NewsFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_2_news),
                        NewsFragment.class,
                        R.drawable.ic_ico_news,
                        false
                ));


        mMenu.put(BottinFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_2_bottin),
                        BottinFragment.class,
                        R.drawable.ic_ico_bottin,
                        false
                ));

        mMenu.put(BiblioFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_2_biblio),
                        BiblioFragment.class,
                        R.drawable.ic_ico_library,
                        false
                ));

        mMenu.put(SecuriteFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_2_securite),
                        SecuriteFragment.class,
                        R.drawable.ic_ico_security,
                        false
                ));

        // Section 3 - ApplETS
        mMenu.put(getString(R.string.menu_section_3_applets),
                new MyMenuItem(
                        getString(R.string.menu_section_3_applets),
                        null
                ));

        mMenu.put(OtherAppsFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_3_apps),
                        OtherAppsFragment.class,
                        R.drawable.ic_star_60x60,
                        false
                ));

        mMenu.put(AboutFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_3_about),
                        AboutFragment.class,
                        R.drawable.ic_logo_icon_final,
                        false
                ));

        mMenu.put(CommentairesFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_3_comms),
                        CommentairesFragment.class,
                        R.drawable.ic_ico_comment,
                        false
                ));

        mMenu.put(SponsorsFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_3_sponsors),
                        SponsorsFragment.class,
                        R.drawable.ic_ico_partners,
                        false
                ));

        mMenu.put(FAQFragment.class.getName(),
                new MyMenuItem(
                        getString(R.string.menu_section_3_faq),
                        FAQFragment.class,
                        R.drawable.ic_ico_faq,
                        false
                ));
    }
}
