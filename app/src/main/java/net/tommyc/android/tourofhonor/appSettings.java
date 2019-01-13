package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

public class appSettings extends AppCompatActivity {

    public EditText editRiderNumberText;
    public EditText editPillionNumberText;

    SharedPreferences sharedpreferences;
    public static final String tohPreferences = "Tour of Honor Preferences";
    public static final String riderNum = "RiderNumber";
    public static final String pillionNum = "PillionNumber";
    String riderNumToH = "000";
    String pillionNumToH = "000";

    String myEmailAddress = "me@tommyc.net";
    String appVersionName;
    String jsonVersionName = "JSON 2019";
    TextView versionDisplay;
    Button btnSendAppFeedback;
    EditText fieldRiderNumber;
    EditText fieldPillionNumber;

    private int tapCount = 0;
    private long startTimerMillis=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Set the version number display
        getVersionName();

        // Enable the buttons
        btnSendAppFeedback = findViewById(R.id.btnSendAppFeedback);
        btnSendAppFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchAppFeedbackIntent();
            }
        });

        Log.e("appSettings", "Initial Values: " + riderNumToH + " / " + pillionNumToH);
        fieldRiderNumber = findViewById(R.id.fieldRiderNumber);
        fieldRiderNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRiderNumberText.getText().clear();
            }
        });
        fieldPillionNumber = findViewById(R.id.fieldPillionNumber);
        fieldPillionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPillionNumberText.getText().clear();
            }
        });
        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(riderNum)) {
            Log.e("appSettings","riderNum set to " + riderNum);
            fieldRiderNumber.setText(sharedpreferences.getString(riderNum, ""));
            riderNumToH = fieldRiderNumber.getText().toString();
        } else {
            Log.e("appSettings","riderNum Failed");
        }
        if (sharedpreferences.contains(pillionNum)) {
            Log.e("appSettings","pillionNum set to " + pillionNum);
            fieldPillionNumber.setText(sharedpreferences.getString(pillionNum, ""));
            pillionNumToH = fieldPillionNumber.getText().toString();
        } else {
            Log.e("appSettings","pillionNum Failed");
        }


        //editPillionNumberText = (EditText) findViewById(R.id.fieldPillionNumber);
        //editPillionNumberText.setOnClickListener(this);

    }

    public void saveRiderInfo(View view) {
        riderNumToH = fieldRiderNumber.getText().toString();
        pillionNumToH = fieldPillionNumber.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(riderNum, riderNumToH);
        editor.putString(pillionNum, pillionNumToH);
        editor.apply();
        Log.e("appSettings","Screen Values " + riderNumToH + " / " + pillionNumToH);
        Log.e("appSettings","Retrieved Values: " + riderNum + " / " + pillionNum);
        Snackbar.make(findViewById(R.id.appSettingsView), R.string.resultsSettingsSaved,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    private String getVersionName() {

        appVersionName = BuildConfig.VERSION_NAME;
        versionDisplay = findViewById(R.id.lblAppVersionInfo);
        versionDisplay.setText("Version " + appVersionName + " | " + jsonVersionName);
        return appVersionName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * Submits the bonus images via email.
     */
    private void dispatchAppFeedbackIntent() {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendEmailIntent.setType("plain/text");
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{myEmailAddress});
        sendEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TOH Feedback from Rider " + riderNumToH);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, "\n\nSent from TOH App\nAndroid Version " + appVersionName);
        this.startActivity(Intent.createChooser(sendEmailIntent, "Sending email..."));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_UP) {
            //get system current milliseconds
            long time= System.currentTimeMillis();
            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
            if (startTimerMillis==0 || (time-startTimerMillis> 3000) ) {
                startTimerMillis=time;
                tapCount=1;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else{ //  time-startMillis< 3000
                tapCount++;
            }
            if (tapCount==7) {
                //do whatever you need
                Log.e("appSettings","Secret Unlocked!");
            }
            return true;
        }
        return false;
    }
}
