package net.tommyc.android.tourofhonor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class appSettings extends AppCompatActivity {

    int riderNumToH = 479;
    int pillionNumToH = 000;
    String myEmailAddress = "me@tommyc.net";
    String appVersionName;
    String jsonVersionName = "JSON 2019";
    private TextView versionDisplay;
    Button btnSendAppFeedback;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
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

    }

    private String getVersionName() {

        appVersionName = BuildConfig.VERSION_NAME;
        versionDisplay = (TextView) findViewById(R.id.lblAppVersionInfo);
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
}
