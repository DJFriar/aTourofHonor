package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static net.tommyc.android.tourofhonor.splashScreen.targetMemorialType;
import static net.tommyc.android.tourofhonor.splashScreen.targetState;
import static net.tommyc.android.tourofhonor.splashScreen.tohPreferences;


public class bonusListing extends AppCompatActivity {

    private static String TAG = "bonusListing"; // Tag just for the LogCat window
    SharedPreferences sharedpreferences;
    String targetStateToH = "XX";

    UserDataDBHelper userDBHelper;
    AppDataDBHelper appDBHelper;

    private String chosenState = "";
    private String chosenCategory = "";

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

        if (sharedpreferences.contains(targetMemorialType)) {
            chosenCategory = sharedpreferences.getString(targetMemorialType,"");
            Log.e(TAG,"targetCategory set to " + chosenCategory);
        } else {
            Log.e(TAG,"targetCategory Failed");
        }

        // Print to Log the DB headers
        CommonSQLiteUtilities.logDatabaseInfo(appDBHelper.getWritableDatabase());

        processRequest();

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

        SharedPreferences.Editor editor = sharedpreferences.edit();

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
                chosenCategory = "Tour of Honor";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_911:
                Log.e(TAG,"action_filter_911");
                chosenCategory = "9/11";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_HUEY:
                Log.e(TAG,"action_filter_HUEY");
                chosenCategory = "Hueys";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_DB:
                Log.e(TAG,"action_filter_DB");
                chosenCategory = "Doughboys";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_GS:
                Log.e(TAG,"action_filter_GS");
                chosenCategory = "Gold Star Family";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_NP:
                Log.e(TAG,"action_filter_NP");
                chosenCategory = "National Parks";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_K9:
                Log.e(TAG,"action_filter_K9");
                chosenCategory = "War Dogs";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_none:
                Log.e(TAG,"action_filter_none");
                chosenCategory = "";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;

            case R.id.action_filter_MTr:
                Log.e(TAG,"action_filter_MTr");
                chosenCategory = "Madonna of the Trail";
                editor.putString(targetMemorialType, chosenCategory);
                editor.apply();

                processRequest();
                return true;



            case R.id.action_FilterState:
                Log.e(TAG,"entering action_filter_State");
                promptForState(this);

                Log.e(TAG,"action_filter_State, returned from prompt");
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
    protected void filterStateWithValue(String value) {
        ListView listView;
        Cursor data;
        Log.e("MyTAG", "CHOSENSTATE: "+value);
        // Populate the ListView w/ filtered data
        listView = findViewById(R.id.lvBonusData);
        data = appDBHelper.getAppDataByState(chosenState);
        listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
    }

    public void promptForState(bonusListing view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose state");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        if (!TextUtils.isEmpty(chosenState))
        {
            input.setText(chosenState);
        }
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.e(TAG,"chosenState was: " + chosenState);
                chosenState = input.getText().toString().toUpperCase();
                //Log.e(TAG,"chosenState is now: " + chosenState);
                //filterStateWithValue(chosenState);

                // Store the chosen state in the TOH preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(targetState, chosenState);
                editor.apply();

                processRequest();
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

    public void promptForStatePrev(bonusListing view) {

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
                //Log.e(TAG,"chosenState was: " + chosenState);
                chosenState = input.getText().toString().toUpperCase();
                //Log.e(TAG,"chosenState is now: " + chosenState);
                filterStateWithValue(chosenState);

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

    public void processRequest() {
        // Populate the ListView w/ filtered data
        ListView listView = findViewById(R.id.lvBonusData);
        //data = appDBHelper.getAppDataTOHBonuses(chosenState);
        Cursor data = appDBHelper.getAppDataBonuses(chosenState, chosenCategory);
        listView.setAdapter(new SimpleCursorAdapter(this, R.layout.bonus_list_row_item, data,
                new String[] {"sCode", "sName", "sCategory", "sCity", "sState"},
                new int[] {R.id.bonusListCode, R.id.bonusListName, R.id.bonusListCategory, R.id.bonusListCity, R.id.bonusListState}, 0));
    }
}
