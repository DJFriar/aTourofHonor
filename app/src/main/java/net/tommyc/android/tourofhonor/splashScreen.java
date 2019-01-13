package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class splashScreen extends AppCompatActivity {

    public static String jsonURL="https://www.tourofhonor.com/BonusData.json";
    public static String jsonDevURL="https://www.tommyc.net/BonusData.json";

    SharedPreferences sharedpreferences;
    public static final String tohPreferences = "Tour of Honor Preferences";
    public static final String riderNum = "RiderNumber";
    public static final String pillionNum = "PillionNumber";
    public static final String devModeStatus = "DevModeStatus";

    public static String riderNumToH;
    public static String pillionNumToH;
    public static boolean devModeOn = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("splashScreen","Creating Splash Screen");
        setContentView(R.layout.activity_splash_screen);

        sharedpreferences = getSharedPreferences(tohPreferences,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(riderNum)) {
            Log.e("splashScreen","riderNum set to " + riderNum);
            riderNumToH = sharedpreferences.getString(riderNum,"000");
        } else {
            Log.e("splashScreen","riderNum Failed");
        }
        if (sharedpreferences.contains(pillionNum)) {
            Log.e("splashScreen","pillionNum set to " + pillionNum);
            pillionNumToH = sharedpreferences.getString(pillionNum,"000");
        } else {
            Log.e("splashScreen","pillionNum Failed");
        }
        if (sharedpreferences.contains(devModeStatus)) {
            Log.e("splashScreen","Dev Mode set to " + devModeStatus + devModeOn);
            devModeOn = sharedpreferences.getBoolean(devModeStatus,false);
        } else {
            Log.e("splashScreen","devModeStatus Failed " +devModeStatus + devModeOn);
        }
    }

    public void goToBonusDetail (View View) {
        Log.e("splashScreen","Gong to Bonus Detail");
        Intent goToBonusDetail = new Intent(this, captureBonus.class);
        startActivity(goToBonusDetail);
    }

    public void goToBonusList (View View) {
        Log.e("splashScreen","Going to Bonus List");
        Intent goToBonusList = new Intent(this,bonusListing.class);
        startActivity(goToBonusList);
    }
}
