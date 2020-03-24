package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppDataDBHelper extends SQLiteOpenHelper {

    private static String TAG = "AppDataDBHelper"; // Tag just for the LogCat window
    private static String DB_NAME ="appdata.db";
    private static String DB_TABLE = "bonuses";
    private static int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    AppDataDBHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void createDataBase() throws IOException {
        Log.e(TAG,"Entered createDatabase");
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                //Copy the database from assets
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //See if DB exists in userspace
    private boolean checkDataBase() {
        Log.e(TAG,"entered checkDatabase");
        File dbFile = new File(mContext.getDatabasePath(DB_NAME).getPath());
        // this.deleteDatabase();
        if (dbFile.exists()) return true;
        Log.e(TAG,"dbFile = true");
        File dbDir = dbFile.getParentFile();
        if (!dbDir.exists()) {
            Log.e(TAG,"dbFile = false");
            dbDir.mkdirs();
        }
        return false;
    }

    //Delete the DB from userspace
    private void deleteDatabase() {
        Log.e(TAG, "entered deleteDatabase");
        // Insert the actual delete DB logic here
    }

    //Copy the database from assets to userspace
    private void copyDataBase() throws IOException {
        InputStream mInput;
        OutputStream mOutput;
        String outFileName = mContext.getDatabasePath(DB_NAME).getPath();

        try {
            mInput = mContext.getAssets().open(DB_NAME);
            mOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInput.read(buffer)) > 0) {
                mOutput.write(buffer, 0, length);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
        } catch (IOException ie) {
            throw new Error("copyDatabase() Error");
        }
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    Cursor getAppDataTOHBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'Tour of Honor' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataDBBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'Doughboys' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataHueyBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'Hueys' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataGSBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'Gold Star Family' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataNPBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'National Parks' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataK9Bonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'War Dogs' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataMTrBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCategory = 'Madonna Trail' ORDER BY sState,sCode", null);
        return data;
    }

    Cursor getAppDataTrophyRuns() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sTrophyGroup IS NOT NULL GROUP BY sTrophyGroup", null);
        return data;
    }

    Cursor getAppDataAllBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " ORDER BY sState,sCode", null);
        Log.d("INFO", data.toString() );
        return data;
    }

    Cursor getAppDataByState(String state) {
        Log.e(TAG,"Entered getAppDataByState");
        SQLiteDatabase db = this.getWritableDatabase();
        String [] args={state};
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sState = ? ORDER BY sCode", args);
        return data;
    }

    Cursor getAppDataSelectedBonus(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String [] args={id};
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sCode = ?", args);
        return data;
    }

    public Cursor getAppDataListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE, null);
        return data;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }
}
