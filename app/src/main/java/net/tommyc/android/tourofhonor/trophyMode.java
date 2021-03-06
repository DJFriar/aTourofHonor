package net.tommyc.android.tourofhonor;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class trophyMode extends AppCompatActivity {

    Button btnStartTrophyRun;
    AppDataDBHelper appDBHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_mode);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the buttons
        btnStartTrophyRun = findViewById(R.id.btnStartTrophyRun);

        // Handle the appData DB.
        appDBHelper = new AppDataDBHelper(this);


//        // Populate the list of Trophy Runs
//        ListView listView = findViewById(R.id.lvTrophyRuns);
//        Cursor data = appDBHelper.getAppDataTrophyRuns();
//        listView.setAdapter(new SimpleCursorAdapter(this, R.layout.trophy_run_row_item, data,
//                new String[] {"sTrophyGroup"},
//                new int[] {R.id.trophyGroup}, 0));

        /*
        // Attempt at forcing the height of the ListView
        if(adapter.getCount() > 5){
            View item = adapter.getView(0, null, listView);
            item.measure(0, 0);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
            listView.setLayoutParams(params);
        }
        */

    /*
    public void startTrophyRun(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (fieldRiderNumber.getText().toString().equals("777")) {
            if (fieldPillionNumber.getText().toString().equals("777")) {
                editor.putBoolean(devModeStatus, true);
                editor.apply();
                Log.e("appSettings","Developer Mode Enabled");
                Snackbar.make(findViewById(R.id.appSettingsView), R.string.devModeEnabled,
                        Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }
        }
    }
    */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
}
