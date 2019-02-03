package net.tommyc.android.tourofhonor;

/*

public class AppDataDBAdapter_Old
{
    protected static final String TAG = "AppDataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private AppDataDBHelper mDbHelper;

    public AppDataDBAdapter_Old(Context context)
    {
        this.mContext = context;
        mDbHelper = new AppDataDBHelper(mContext);
    }

    public AppDataDBAdapter_Old createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public AppDataDBAdapter_Old open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor getAppData()
    {
        try
        {
            String sql ="SELECT hMy as _id,* FROM myTable";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}
*/