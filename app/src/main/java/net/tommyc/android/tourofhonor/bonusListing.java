package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;

import static net.tommyc.android.tourofhonor.splashScreen.tohPreferences;

public class bonusListing extends AppCompatActivity {

    private static String TAG = "bonusListing"; // Tag just for the LogCat window
    SharedPreferences sharedpreferences;

    UserDataDBHelper userDBHelper;
    AppDataDBHelper appDBHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_listing);
        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ListView listView = findViewById(R.id.lvBonusData);

        // Handle the appData DB.
        appDBHelper = new AppDataDBHelper(this);

        try {
            Log.e(TAG,"Calling createDatabase");
            appDBHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to CREATE DATABASE");
        }

        // Populate the ListView w/ all rows
        Cursor data = appDBHelper.getAppDataAllBonuses();
        listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));

        // Print to Log the DB headers
        CommonSQLiteUtilities.logDatabaseInfo(appDBHelper.getWritableDatabase());


        /* TOGGLE DEV MODE USING OLD JSON METHOD
        devModeOn = sharedpreferences.getBoolean(devModeStatus,false);

        if (!devModeOn) {
            jsonURL = "https://www.tourofhonor.com/BonusData.json";
        } else if (devModeOn) {
            jsonURL = "https://www.tommyc.net/BonusData.json";
        } else {
            Log.e("bonusListing", "Invalid Dev Mode Setting");
            return;
        }
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.e(TAG,"action_setting");
                startActivity(new Intent(this, appSettings.class));
                return true;

            case R.id.action_trophyMode:
                Log.e(TAG,"action_trophyMode");
                startActivity(new Intent(this, trophyMode.class));
                return true;

            case R.id.action_filter:
                Log.e(TAG,"action_filter");
                // Populate the ListView w/ filtered data
                ListView listView = findViewById(R.id.lvBonusData);
                Cursor data = appDBHelper.getAppDataFilteredBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public void goToAppSettings (View View) {
        Log.e(TAG,"goToAppSettings");
        Intent goToAppSettings = new Intent(this,appSettings.class);
        startActivity(goToAppSettings);
    }

    public void goToTrophyMode (View View) {
        Log.e(TAG,"goToTrophyMode");
        Intent goToTrophyMode = new Intent(this,trophyMode.class);
        startActivity(goToTrophyMode);
    }

    public void goToBonusDetail (View View) {
        Log.e(TAG,"goToBonusDetail");
        Intent goToBonusDetail = new Intent(this,captureBonus.class);
        startActivity(goToBonusDetail);
    }
}
