package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static net.tommyc.android.tourofhonor.splashScreen.devModeOn;
import static net.tommyc.android.tourofhonor.splashScreen.devModeStatus;
import static net.tommyc.android.tourofhonor.splashScreen.jsonURL;
import static net.tommyc.android.tourofhonor.splashScreen.tohPreferences;

public class bonusListing extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    ArrayList<jsonBonuses> dataModels;
    ListView listView;
    private static jsonToListViewAdapter adapter;

    ListView lv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_listing);
        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        devModeOn = sharedpreferences.getBoolean(devModeStatus,false);

        if (!devModeOn) {
            jsonURL = "https://www.tourofhonor.com/BonusData.json";
        } else if (devModeOn) {
            jsonURL = "https://www.tommyc.net/BonusData.json";
        } else {
            Log.e("bonusListing", "Invalid Dev Mode Setting");
            return;
        }

        listView = (ListView) findViewById(R.id.lvBonusData);
        new jsonDownloader(bonusListing.this,jsonURL, lv).execute();

        // Populate the DataModel with some false data for testing purposes
        dataModels= new ArrayList<>();

        // dataModels.add(new jsonBonuses("BC1", "category1", "name1",1,"address1","city1","state1","GPS1","access1","flavor1","madeInAmerica1","imageName"));
        // dataModels.add(new jsonBonuses("BC2", "category2", "name2",2,"address2","city2","state2","GPS2","access2","flavor2","madeInAmerica2","imageName"));

        adapter= new jsonToListViewAdapter(dataModels,getApplicationContext());
        if(adapter != null) {
            Log.e("onCreate","entered adapter code");
            listView.setAdapter(adapter);
        } else {
            Log.e("onCreate","adapter has no data");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jsonBonuses dataModel= dataModels.get(position);

                Log.e("onClick for Data Row","Tapped on row");
            }
            });
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
