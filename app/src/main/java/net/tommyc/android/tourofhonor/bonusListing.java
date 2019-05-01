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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
        final ListView listView = findViewById(R.id.lvBonusData);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"Entered onItemClickListener");
                Toast.makeText(bonusListing.this, "You clicked "+position, Toast.LENGTH_SHORT).show();
                //Intent nextActivity = new Intent(bonusListing.this, bonusListing.class);
                //startActivity(nextActivity);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ListView listView;
        Cursor data;

        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.e(TAG,"action_setting");
                startActivity(new Intent(this, appSettings.class));
                return true;

                /*
            case R.id.action_trophyMode:
                Log.e(TAG,"action_trophyMode");
                startActivity(new Intent(this, trophyMode.class));
                return true;
                */

                /*
            case R.id.action_filter:
                Log.e(TAG,"action_filter");
                // Populate the ListView w/ filtered data
                ListView listView = findViewById(R.id.lvBonusData);
                Cursor data = appDBHelper.getAppDataFilteredBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;
                */

            case R.id.action_filter_TOH:
                Log.e(TAG,"action_filter_TOH");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataTOHBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_HUEY:
                Log.e(TAG,"action_filter_HUEY");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataHueyBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_DB:
                Log.e(TAG,"action_filter_DB");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataDBBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_GS:
                Log.e(TAG,"action_filter_GS");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataGSBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_NP:
                Log.e(TAG,"action_filter_NP");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataNPBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_K9:
                Log.e(TAG,"action_filter_K9");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataK9Bonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_none:
                Log.e(TAG,"action_filter_none");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataAllBonuses();
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

            case R.id.action_filter_MTr:
                Log.e(TAG,"action_filter_MTr");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataMTrBonuses();
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

    /*
    public int getIdFromClassName(String className){
        String query = "SELECT rowid" +
                " FROM " + CLASSES_TABLE_NAME +
                " WHERE " + CLASSES_COLUMN_NAME + " = ?;";
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.longForQuery(db, query, new String[]{ className });
    }
    */

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

    public void goToCaptureBonus (View View) {
        String tappedBonus = ((TextView) this.findViewById(R.id.bonusListCode)).getText().toString();
        Log.e(TAG,"goToCaptureBonus, tapped bonus = " + tappedBonus);
        Intent goToCaptureBonus = new Intent(this,captureBonus.class);
        goToCaptureBonus.putExtra("codeTapped",tappedBonus);
        startActivity(goToCaptureBonus);
    }

    public void goToBonusDetail (View View) {
        String tappedBonus = ((TextView) findViewById(R.id.bonusListCode)).getText().toString();
        Log.e(TAG,"goToBonusDetail, tapped bonus = " + tappedBonus);
        Intent goToBonusDetail = new Intent(this,bonusDetail.class);
        goToBonusDetail.putExtra("codeTapped",tappedBonus);
        startActivity(goToBonusDetail);
    }
}
