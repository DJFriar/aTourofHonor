package net.tommyc.android.tourofhonor;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommonSQLiteUtilities {

    public static final boolean ERROR_CHECKING_ON = true;
    public static final boolean ERROR_CHECKING_OFF = false;

    // SQLite MASTER TABLE definitions
    static final String SQLITE_MASTER = "sqlite_master";
    static final String SM_TABLE_TYPE_COLUMN = "type";
    static final String SM_NAME_COLUMN = "name";
    static final String SM_TABLENAME_COLUMN = "tbl_name";
    static final String SM_ROOTPAGE_COLUMN = "rootpage";
    static final String SM_SQL_COLUMN = "sql";
    static final String SM_TYPE_TABLE = "table";
    static final String SM_TYPE_INDEX = "index";

    static final String PRAGMA_STATEMENT = "PRAGMA ";
    static final String PRAGMA_DATABASELIST = "database_list";
    static final String PRAGMA_USERVERSION = "user_version";
    static final String PRAGMA_ENCODING = "encoding";
    static final String PRAGMA_FOREIGNKEYLIST = "foreign_key_list";
    static final String PRAGMA_INDEXINFO = "index_info";
    static final String PRAGMA_INDEXLIST = "index_list";
    static final String PRAGMA_TABLEINFO = "table_info";

    static final String PRAGMA_DBLIST_SEQ_COL = "seq";
    static final String PRAGMA_DBLIST_NAME_COL = "name";
    static final String PRAGMA_DBLIST_FILE_COL = "file";
    static final String PRAGMA_TABLEINFO_CID_COL = "cid";
    static final String PRAGMA_TABLEINFO_NAME_COl = "name";
    static final String PRAGMA_TABLEINFO_TYPE_COL = "type";
    static final String PRAGMA_TABLEINFO_NOTNULL_COL = "notnull";
    static final String PRAGMA_TABLEINFO_DEFAULTVALUE_COL = "dflt_value";
    static final String PRAGMA_TABLEINFO_PRIMARYKEY_COL = "pk";





    static final String CSU_TAG = "SQLITE_CSU";

    private CommonSQLiteUtilities() {}

    /**
     * Write Database information to the log;
     * Information wrttien is:
     * the database path, (will/should show connected databases)
     * the version number (note! user version i.e. version coded in DBHelper),
     * the tables in the database (includes android_metadata but not sqlite_master),
     * the columns of the tables
     * @param db    The SQLite database to be interrogated
     */
    public static void logDatabaseInfo(SQLiteDatabase db) {

        // Issue PRAGMA database_list commnand
        Cursor dblcsr = db.rawQuery(PRAGMA_STATEMENT + PRAGMA_DATABASELIST,null);
        // Write databases to the log
        while (dblcsr.moveToNext()) {
            Log.d(CSU_TAG,"DatabaseList Row " + Integer.toString(dblcsr.getPosition() + 1) +
                    " Name=" + dblcsr.getString(dblcsr.getColumnIndex(PRAGMA_DBLIST_NAME_COL)) +
                    " File=" + dblcsr.getString(dblcsr.getColumnIndex(PRAGMA_DBLIST_FILE_COL))
            );
        }
        dblcsr.close();
        // Issue PRAGMA user_version to get the version and write to the log
        //Note! to set user_version use execSQL not rawQuery
        Cursor uvcsr = db.rawQuery(PRAGMA_STATEMENT + PRAGMA_USERVERSION,null);
        while (uvcsr.moveToNext()) {
            Log.d(CSU_TAG,"Database Version = " +
                    Integer.toString(uvcsr.getInt(uvcsr.getColumnIndex(PRAGMA_USERVERSION))));
        }
        uvcsr.close();
        // Select all table entry rows from sqlite_master
        Cursor tlcsr = db.rawQuery("SELECT * FROM " +
                        SQLITE_MASTER + " WHERE " +
                        SM_TABLE_TYPE_COLUMN + "='" + SM_TYPE_TABLE + "'"
                ,null);
        // For each table write table information to the log
        // (inner loop gets column info per table)
        while (tlcsr.moveToNext()) {
            String current_table = tlcsr.getString(tlcsr.getColumnIndex(SM_TABLENAME_COLUMN));
            Log.d(CSU_TAG,
                    "Table Name = " + current_table +
                            " Created Using = " + tlcsr.getString(tlcsr.getColumnIndex(SM_SQL_COLUMN)),
                    null
            );
            // Issue PRAGMA tabel_info for the current table
            Cursor ticsr = db.rawQuery(PRAGMA_STATEMENT + PRAGMA_TABLEINFO +
                            "(" + current_table + ")",
                    null
            );
            // Write column info (see headings below) to the log
            while (ticsr.moveToNext()) {
                Log.d(CSU_TAG,"Table = " +
                                current_table +
                                " ColumnName = " +
                                ticsr.getString(ticsr.getColumnIndex(PRAGMA_TABLEINFO_NAME_COl)) +
                                " ColumnType = " +
                                ticsr.getString(ticsr.getColumnIndex(PRAGMA_TABLEINFO_TYPE_COL)) +
                                " Default Value = " +
                                ticsr.getString(ticsr.getColumnIndex(PRAGMA_TABLEINFO_DEFAULTVALUE_COL)) +
                                " PRIMARY KEY SEQUENCE = " + Integer.toString(
                        ticsr.getInt(ticsr.getColumnIndex(PRAGMA_TABLEINFO_PRIMARYKEY_COL))
                        )
                );
            }
            ticsr.close();
        }
        tlcsr.close();
    }

    /**
     * Generic get all rows from an SQlite table,
     * allowing the existence of the table to be checked and also
     * allowing the ROWID to be added AS a supplied string
     *
     * @param db                    The SQLiteDatabase
     * @param tablename             The name of the table from which the
     *                              returned cursor will be created from;
     *                              Note!
     * @param use_error_checking    Whether ot not to try to detect errors
     *                              currently just table doesn't exist,
     *                              true to turn on, false to turn off
     *                              ERROR_CHECKING_ON = true
     *                              ERROR_CHECKING_OFF = false
     * @param forceRowidAs          If length of string passed is 1 or greater
     *                              then a column, as an alias of ROWID, will be
     *                              added to the cursor
     * @return                      the extracted cursor, or in the case of the
     *                              underlying table not existing an empty cursor
     *                              with no columns
     */
    public static Cursor getAllRowsFromTable(SQLiteDatabase db,
                                             String tablename,
                                             boolean use_error_checking,
                                             String forceRowidAs) {
        String[] columns = null;

        // Tablename must be at least 1 character in length
        if (tablename.length() < 1) {
            Log.d(CSU_TAG,new Object(){}.getClass().getEnclosingMethod().getName() +
                    " is finishing as the provided tablename is less than 1 character in length"
            );
            return new MatrixCursor(new String[]{});
        }

        // If use_error_checking is true then check that the table exists
        // in the sqlite_master table
        if (use_error_checking) {
            Cursor chkcsr = db.query(SQLITE_MASTER,null,
                    SM_TABLE_TYPE_COLUMN + "=? AND "
                            + SM_TABLENAME_COLUMN + "=?",
                    new String[]{SM_TYPE_TABLE,tablename},
                    null,null,null
            );

            // Ooops table is not in the Database so return an empty
            // column-less cursor
            if (chkcsr.getCount() < 1) {
                Log.d(CSU_TAG,"Table " + tablename +
                        " was not located in the SQLite Database Master Table."
                );
                // return empty cursor with no columns
                return new MatrixCursor(new String[]{});

            }
            chkcsr.close();
        }

        // If forcing an alias of ROWID then user ROWID AS ???, *
        if(forceRowidAs != null && forceRowidAs.length() > 0) {
            columns = new String[]{"rowid AS " +forceRowidAs,"*"};
        }

        // Finally return the Cursor but trap any exceptions
        try {
            return db.query(tablename, columns, null, null, null, null, null);
        } catch (Exception e) {
            Log.d(CSU_TAG,"Exception encountered but trapped when querying table " + tablename +
                    " Message was: \n" + e.getMessage());
            Log.d(CSU_TAG,"Stacktrace was:");
            e.printStackTrace();
            return new MatrixCursor(new String[]{});
        }
    }

    /**
     * Create and return a Cursor devoid of any rows and columns
     * Not used, prehaps of very little use.
     * @param db    The Sqlite database in which the cursor is to be created
     * @return      The empty Cursor
     */
    private static Cursor getEmptyColumnLessCursor(SQLiteDatabase db) {
        return new MatrixCursor(new String[]{});
    }

    /**
     * Write column names in the passed Cursor to the log
     * @param csr   The Cursor to be inspected.
     */
    public static void logCursorColumns(Cursor csr) {
        Log.d(CSU_TAG,
                new Object(){}.getClass().getEnclosingMethod().getName() +
                        " invoked. Cursor has the following " +
                        Integer.toString(csr.getColumnCount())+
                        " columns.");
        int position = 0;
        for (String column: csr.getColumnNames()) {
            position++;
            Log.d(CSU_TAG,"Column Name " +
                    Integer.toString(position) +
                    " is "
                    + column
            );
        }
    }

    /**
     * Write the contents of the Cursor to the log
     * @param csr   The Cursor that is to be displayed in the log
     */
    public static void logCursorData(Cursor csr) {
        int columncount = csr.getColumnCount();
        int rowcount = csr.getCount();
        int csrpos = csr.getPosition(); //<<< added 20171016
        Log.d(CSU_TAG,
                new Object(){}.getClass().getEnclosingMethod().getName() +
                        " Cursor has " +
                        Integer.toString(rowcount) +
                        " rows with " +
                        Integer.toString(columncount) + " columns."
        );
        csr.moveToPosition(-1);     //Ensure that all rows are retrieved <<< added 20171016
        while (csr.moveToNext()) {
            String unobtainable = "unobtainable!";
            String logstr = "Information for row " + Integer.toString(csr.getPosition() + 1) + " offset=" + Integer.toString(csr.getPosition());
            for (int i=0; i < columncount;i++) {
                logstr = logstr + "\n\tFor Column " + csr.getColumnName(i);
                switch (csr.getType(i)) {
                    case Cursor.FIELD_TYPE_NULL:
                        logstr = logstr + " Type is NULL";
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        logstr = logstr + "Type is FLOAT";
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        logstr = logstr + " Type is INTEGER";
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        logstr = logstr + " Type is STRING";
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        logstr = logstr + " Type is BLOB";
                        break;
                }
                String strval_log = " value as String is ";
                String lngval_log = " value as long is ";
                String dblval_log = " value as double is ";
                String blbval_log = "";
                try {
                    strval_log = strval_log + csr.getString(i);
                    lngval_log = lngval_log + csr.getLong(i);
                    dblval_log = dblval_log +  csr.getDouble(i);
                } catch (Exception e) {
                    strval_log = strval_log + unobtainable;
                    lngval_log = lngval_log + unobtainable;
                    dblval_log = dblval_log + unobtainable;
                    try {
                        blbval_log = " value as blob is " +
                                getBytedata(csr.getBlob(i),24);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                }
                logstr = logstr + strval_log + lngval_log + dblval_log + blbval_log;
            }
            Log.d(CSU_TAG,logstr);
        }
        csr.moveToPosition(csrpos); // restore cursor position <<< added 20171016
    }

    /**
     * Return a hex string of the given byte array
     * @param bytes     The byte array to be converted to a hexadecimal string
     * @param limit     the maximum number of bytes;
     *                  note returned string will be up to twice as long
     * @return          The byte array represented as a hexadecimal string
     */
    private static String getBytedata(byte[] bytes, int limit) {
        if (bytes.length < limit) {
            return convertBytesToHex(bytes);
        } else {
            byte[] subset = new byte[limit];
            System.arraycopy(bytes,0,subset,0,limit);
            return convertBytesToHex(subset);
        }
    }

    // HEX characters as a char array for use by convertBytesToHex
    private final static char[] hexarray = "0123456789ABCDEF".toCharArray();

    /**
     * Return a hexadecimal string representation of the passed byte array
     * @param bytes     The byte array to be represented.
     * @return          The string representing the byte array as hexadecimal
     */
    private static String convertBytesToHex(byte[] bytes) {
        char[] hexstr = new char[bytes.length * 2];
        for (int i=0; i < bytes.length; i++) {
            int h = bytes[i] & 0xFF;
            hexstr[i * 2] = hexarray[h >>> 4];
            hexstr[i * 2 + 1] = hexarray[h & 0xF];
        }
        return new String(hexstr);
    }
}
