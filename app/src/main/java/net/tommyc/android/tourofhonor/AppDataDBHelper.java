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
    private static String DB_TABLE = "Bonus_Data";
    private static int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public AppDataDBHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createDataBase() throws IOException {
        //If the database does not exist, copy it from the assets.

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

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase() {
        Log.e(TAG,"entered checkDatabase");
        File dbFile = new File(mContext.getDatabasePath(DB_NAME).getPath());
        if (dbFile.exists()) return true;
        Log.e(TAG,"dbFile = true");
        File dbDir = dbFile.getParentFile();
        if (!dbDir.exists()) {
            Log.e(TAG,"dbFile = false");
            dbDir.mkdirs();
        }
        return false;
    }

    //Copy the database from assets
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
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }



    public Cursor getAppDataFilteredBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sState = 'CA'", null);
        return data;
    }

    public Cursor getAppDataTrophyRuns() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " WHERE sTrophyGroup IS NOT NULL GROUP BY sTrophyGroup", null);
        return data;
    }

    public Cursor getAppDataAllBonuses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT hMy as _id,* FROM " + DB_TABLE + " ORDER BY sCode", null);
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
