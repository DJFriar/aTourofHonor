package net.tommyc.android.tourofhonor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void goToSpashScreen (View View) {
        Log.e("MainActivity","Going to Splash Screen");
        Intent goToSplashScreen = new Intent(this,splashScreen.class);
        startActivity(goToSplashScreen);
    }
}
