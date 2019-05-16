package net.tommyc.android.tourofhonor;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class splashScreen extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String tohPreferences = "Tour of Honor Preferences";
    public static final String riderNum = "RiderNumber";
    public static final String pillionNum = "PillionNumber";
    public static final String targetState = "TargetState";
    public static final String devModeStatus = "DevModeStatus";

    public static String riderNumToH;
    public static String pillionNumToH;
    public static String targetStateToH;

    ArrayList<Long> list = new ArrayList<>();

    UserDataDBHelper userDB;
    AppDataDBHelper appDB;

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("IN", "" + referenceId);
            list.remove(referenceId);
            if (list.isEmpty()) {
                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(splashScreen.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("TourOfHonor")
                                .setContentText("All Download completed");

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("splashScreen","Creating Splash Screen");
        setContentView(R.layout.activity_splash_screen);
        userDB = new UserDataDBHelper(this);

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
        if (sharedpreferences.contains(targetState)) {
            Log.e("splashScreen","pillionNum set to " + targetState);
            targetStateToH = sharedpreferences.getString(targetState,"XX");
        } else {
            Log.e("splashScreen","targetState Failed");
        }
    }

    public void goToAppSettings (View View) {
        Log.e("splashScreen","Gong to Settings Screen");
        Intent goToAppSettings = new Intent(this, appSettings.class);
        startActivity(goToAppSettings);
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

    /*
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Has Storage Permission?","Permission was granted.");
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Log.e("Has Storage Permission?","Permission was NOT granted.");
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    */

    public boolean isFileExists(String filename){

        File dataDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dataFile = new File(dataDirectory , "Tour of Honor/" + filename);
        String dataFilePath = dataFile.getAbsolutePath();
        Log.e("splashScrn isFileExists", "File Found: " + dataFilePath);
        return dataFile.exists();
    }

    public boolean deleteFile( String filename){

        File dataDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dataFile = new File(dataDirectory , "Tour of Honor/" + filename);
        // String dataFile = new File(dirr , "Tour of Honor/" + filename);
        Log.e("splashScreen deleteFile", "File to Delete: ");
        return dataFile.delete();
    }

    public void AddData(String newEntry) {

        boolean insertData = userDB.addData(newEntry);

        if(insertData){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission granted
            Log.e("RequestPermissionResult","Permission granted");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(onComplete);
    }
}
