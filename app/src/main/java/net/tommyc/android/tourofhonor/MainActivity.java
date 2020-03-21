package net.tommyc.android.tourofhonor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

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
