package net.tommyc.android.tourofhonor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class splashScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("splashScreen","Creating Splash Screen");
        setContentView(R.layout.activity_splash_screen);
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
