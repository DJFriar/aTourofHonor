package net.tommyc.android.tourofhonor;

import android.content.ContentValues;
import android.content.Context;
// import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class dbHelper extends SQLiteOpenHelper {
    private static final String TAG = dbHelper.class.getSimpleName();
    private static final String DATABASE_FILENAME = "TourOfHonor.db";
    private static final int DATABASE_VERSION = 1;
    // Context context;


    public dbHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
        Log.e(TAG,"Entered dbHelper method");
        // Resources bResources;
        // bResources = context.getResources();
        SQLiteDatabase db;
        db = this.getWritableDatabase();

        try {
            Log.e(TAG,"trying to readDataToDb");
            readDataToDb(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE " + dbContract.BonusDataDB.TABLE_NAME + " (" +
                dbContract.BonusDataDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dbContract.BonusDataDB.COLUMN_CODE + " TEXT UNIQUE NOT NULL, " +
                dbContract.BonusDataDB.COLUMN_NAME + " TEXT NOT NULL, " +
                dbContract.BonusDataDB.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                dbContract.BonusDataDB.COLUMN_CITY + " TEXT NOT NULL, " +
                dbContract.BonusDataDB.COLUMN_STATE + " TEXT NOT NULL, " +
                dbContract.BonusDataDB.COLUMN_GPS + " TEXT, " +
                dbContract.BonusDataDB.COLUMN_ACCESS + " TEXT, " +
                dbContract.BonusDataDB.COLUMN_FLAVOR + " TEXT, " +
                dbContract.BonusDataDB.COLUMN_MADEINAMERICA + " TEXT, " +
                dbContract.BonusDataDB.COLUMN_IMAGE + " TEXT " + ");";
        db.execSQL(SQL_CREATE_BUGS_TABLE);
        Log.e(TAG,"Database Created Successfully");

        try {
            Log.e(TAG,"trying to readDataToDb");
            readDataToDb(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {

        String filename = "BonusData.json";

        final String BONUS_CODE = "bonusCode";
        final String BONUS_NAME = "category";
        final String BONUS_ADDRESS = "name";
        final String BONUS_CITY = "city";
        final String BONUS_STATE = "state";
        final String BONUS_GPS = "GPS";
        final String BONUS_ACCESS = "Access";
        final String BONUS_FLAVOR = "flavor";
        final String BONUS_MADEINAMERICA = "madeinamerica";
        final String BONUS_IMAGENAME = "imageName";

        try {
            Log.e(TAG,"inside try block of readDataToDb");
            String jsonDataString = readJsonDataFromFile(filename);

            JSONObject object = new JSONObject(jsonDataString);
            JSONArray bonusDataJsonArray  = object.getJSONArray(filename);

            for (int i = 0; i < bonusDataJsonArray.length(); ++i) {
                String code;
                String name;
                String address;
                String city;
                String state;
                String gps;
                String access;
                String flavor;
                String madeinamerica;
                String imagename;

                JSONObject bonusDataObject = bonusDataJsonArray.getJSONObject(i);

                code = bonusDataObject.getString(BONUS_CODE);
                name = bonusDataObject.getString(BONUS_NAME);
                address = bonusDataObject.getString(BONUS_ADDRESS);
                city = bonusDataObject.getString(BONUS_CITY);
                state = bonusDataObject.getString(BONUS_STATE);
                gps = bonusDataObject.getString(BONUS_GPS);
                access = bonusDataObject.getString(BONUS_ACCESS);
                flavor = bonusDataObject.getString(BONUS_FLAVOR);
                madeinamerica = bonusDataObject.getString(BONUS_MADEINAMERICA);
                imagename = bonusDataObject.getString(BONUS_IMAGENAME);

                ContentValues bonusValues = new ContentValues();

                bonusValues.put(dbContract.BonusDataDB.COLUMN_CODE, code);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_NAME, name);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_ADDRESS, address);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_CITY, city);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_STATE, state);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_GPS, gps);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_ACCESS, access);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_FLAVOR, flavor);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_MADEINAMERICA, madeinamerica);
                bonusValues.put(dbContract.BonusDataDB.COLUMN_IMAGE, imagename);

                db.insert(dbContract.BonusDataDB.TABLE_NAME, null, bonusValues);

                Log.e(TAG, "Inserted Successfully " + bonusValues);
            }
        } catch (JSONException e){
            Log.e(TAG, "JSON Exception Thrown");
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private String readJsonDataFromFile(String filename) throws IOException {
        Log.e(TAG,"Entered readJsonDataFromFile method");
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        File dataDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File jsonFile = new File(dataDirectory , "Tour of Honor/" + filename);

        try {
            String jsonDataString;
            inputStream = new FileInputStream(jsonFile);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }
}
