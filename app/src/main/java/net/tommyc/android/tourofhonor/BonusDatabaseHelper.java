package net.tommyc.android.tourofhonor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BonusDatabaseHelper extends SQLiteOpenHelper {
    private static BonusDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "appdata.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_BONUSES = "bonuses";


    // Post Table Columns
    private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_POST_TEXT = "text";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";

    // Bonus Table Columns
    private static final String KEY_BONUS_HMY = "hMy";
    private static final String KEY_BONUS_SCODE = "sCode";
    private static final String KEY_BONUS_SCATEGORY = "sCategory";
    private static final String KEY_BONUS_SNAME = "sName";
    private static final String KEY_BONUS_SADDRESS = "sAddress";
    private static final String KEY_BONUS_SCITY = "sCity";
    private static final String KEY_BONUS_SSTATE = "sState";
    private static final String KEY_BONUS_SGPS = "sGPS";
    private static final String KEY_BONUS_SACCESS = "sAccess";
    private static final String KEY_BONUS_SFLAVOR = "sFlavor";
    private static final String KEY_BONUS_SMADEINAMERICA = "sMadeInAmerica";
    private static final String KEY_BONUS_SIMAGENAME = "sImageName";
    private static final String KEY_BONUS_STROPHYGROUP = "sTrophyGroup";

    public static synchronized BonusDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new BonusDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private BonusDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public long addOrUpdateBonus(Bonus bonus) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long bonusId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_BONUS_SCODE, bonus.sCode);
            values.put(KEY_BONUS_SNAME, bonus.sName);
            values.put(KEY_BONUS_SCATEGORY, bonus.sCategory);
            values.put(KEY_BONUS_SADDRESS, bonus.sAccess);
            values.put(KEY_BONUS_SCITY, bonus.sCity);
            values.put(KEY_BONUS_SSTATE, bonus.sState);
            values.put(KEY_BONUS_SGPS, bonus.sGPS);
            values.put(KEY_BONUS_SIMAGENAME, bonus.sImageName);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_BONUSES, values, KEY_BONUS_SCODE + "= ?", new String[]{bonus.sCode});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String selectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_BONUS_HMY, TABLE_BONUSES, KEY_BONUS_SCODE);
                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(bonus.sCode)});
                try {
                    if (cursor.moveToFirst()) {
                        bonusId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                bonusId = db.insertOrThrow(TABLE_BONUSES, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return bonusId;
    }
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_POSTS,
                        TABLE_USERS,
                        TABLE_POSTS, KEY_POST_USER_ID_FK,
                        TABLE_USERS, KEY_USER_ID);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));

                    Post newPost = new Post();
                    newPost.text = cursor.getString(cursor.getColumnIndex(KEY_POST_TEXT));
                    newPost.user = newUser;
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }
    // Insert a post into the database
    public void addBonus(Bonus bonus) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            ContentValues values = new ContentValues();
            values.put(KEY_BONUS_SCODE, bonus.sCode);
            values.put(KEY_BONUS_SNAME, bonus.sName);
            values.put(KEY_BONUS_SCATEGORY, bonus.sCategory);
            values.put(KEY_BONUS_SADDRESS, bonus.sAccess);
            values.put(KEY_BONUS_SCITY, bonus.sCity);
            values.put(KEY_BONUS_SSTATE, bonus.sState);
            values.put(KEY_BONUS_SGPS, bonus.sGPS);
            values.put(KEY_BONUS_SIMAGENAME, bonus.sImageName);
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_BONUSES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public Integer getBonusCount(){
        String BONUSES_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_BONUSES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(BONUSES_SELECT_QUERY, null);
        int result = cursor.getCount();
        cursor.close();
        return result;
    }

    public List<Bonus> getAllBonus() {
        List<Bonus> bonuses = new ArrayList<>();

        String BONUSES_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_BONUSES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(BONUSES_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    Bonus newBonus = new Bonus();
                    newBonus.sCode = cursor.getString(cursor.getColumnIndex(KEY_BONUS_SCODE));
                    bonuses.add(newBonus);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get bonuses from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return bonuses;
    }


    public void populateBonusTable(){
        new RetrieveData(sInstance).execute("https://www.basicbitch.dev/bonuses.json");
        Log.d(TAG, "populateBonusTable: grabbed data");
    }
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
                "(" +
                KEY_POST_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                KEY_POST_TEXT + " TEXT" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
                ")";

        String CREATE_BONUSES_TABLE = "CREATE TABLE " + TABLE_BONUSES +
                "(" +
                KEY_BONUS_HMY + " INTEGER PRIMARY KEY," +
                KEY_BONUS_SCODE + " TEXT," +
                KEY_BONUS_SCATEGORY+ " TEXT," +
                KEY_BONUS_SNAME + " TEXT," +
                KEY_BONUS_SADDRESS + " TEXT," +
                KEY_BONUS_SCITY + " TEXT," +
                KEY_BONUS_SSTATE + " TEXT," +
                KEY_BONUS_SGPS + " TEXT," +
                KEY_BONUS_SACCESS + " TEXT," +
                KEY_BONUS_SFLAVOR + " TEXT," +
                KEY_BONUS_SMADEINAMERICA + " TEXT," +
                KEY_BONUS_SIMAGENAME + " TEXT," +
                KEY_BONUS_STROPHYGROUP + " TEXT" +
                ")";
        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_BONUSES_TABLE);

        populateBonusTable();
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
}