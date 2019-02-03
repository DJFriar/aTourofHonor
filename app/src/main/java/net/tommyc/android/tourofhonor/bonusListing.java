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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

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

        // Populate the ListView
        ArrayList<String> theList = new ArrayList<>();
        ArrayAdapter listAdapter = new ArrayAdapter<>(this,R.layout.row_item,R.id.bonusListCode,theList);
        Cursor data = appDBHelper.getAppDataListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        } else {
            for(int i=0;i<data.getCount();i++) {
                data.moveToPosition(i);
                theList.add(data.getString(2));
            }
            listView.setAdapter(listAdapter);
        }


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
                startActivity(new Intent(this, appSettings.class));
                return true;

            case R.id.action_filter:
                // Insert appropriate call here to whatever will make the filters work.
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
        Intent goToAppSettings = new Intent(this,appSettings.class);
        startActivity(goToAppSettings);
    }

    public void goToBonusDetail (View View) {
        Intent goToBonusDetail = new Intent(this,captureBonus.class);
        startActivity(goToBonusDetail);
    }
}
