package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.IOException;

import static net.tommyc.android.tourofhonor.splashScreen.targetState;
import static net.tommyc.android.tourofhonor.splashScreen.tohPreferences;


public class bonusListing extends AppCompatActivity {

    private static String TAG = "bonusListing"; // Tag just for the LogCat window
    SharedPreferences sharedpreferences;
    String targetStateToH = "XX";

    UserDataDBHelper userDBHelper;
    AppDataDBHelper appDBHelper;

    private String chosenState = "";

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

        /*try {
            Log.e(TAG,"Calling createDatabase");
            appDBHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to CREATE DATABASE");
        }
*/
        // Grab the target state from SharedPreferences
        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(targetState)) {
            targetStateToH = sharedpreferences.getString(targetState,"XX");
            Log.e(TAG,"targetState set to " + targetState);
        } else {
            Log.e(TAG,"targetState Failed");
        }

        // Print to Log the DB headers
        CommonSQLiteUtilities.logDatabaseInfo(appDBHelper.getWritableDatabase());

        // Populate the ListView w/ all rows
        Cursor data = appDBHelper.getAppDataAllBonuses();
        listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //use the position variable to get the list item from the list
                String tappedBonus = ((TextView) view.findViewById(R.id.bonusListCode)).getText().toString();
                Log.e(TAG,"goToCaptureBonus, tapped bonus = " + tappedBonus);
                Intent goToCaptureBonus = new Intent(bonusListing.this,captureBonus.class);
                goToCaptureBonus.putExtra("codeTapped",tappedBonus);
                startActivity(goToCaptureBonus);
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

                /*

                Put the following 3 lines in the toolbar.xml:

                <item android:id="@+id/action_filter_state"
                android:title="@string/btnLblFilterState"
                app:showAsAction="never" />

            case R.id.action_filter_state:
                Log.e(TAG,"entering action_filter_State");
                promptForState(this);
                //chosenState = "OH";
                Log.e(TAG,"action_filter_State, returned from prompt");
                // Populate the ListView w/ filtered data
                listView = findViewById(R.id.lvBonusData);
                data = appDBHelper.getAppDataByState(chosenState);
                listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                        new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                        new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
                return true;

                */
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

    public void goToCaptureBonus (View View) {
        String tappedBonus = ((TextView) findViewById(R.id.bonusListCode)).getText().toString();
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

    public void promptForState(bonusListing view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Two Letter State Code");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG,"chosenState was: " + chosenState);
                chosenState = input.getText().toString();
                Log.e(TAG,"chosenState is now: " + chosenState);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
