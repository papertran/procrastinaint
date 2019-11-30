package edu.fsu.cen4020.android.procrastinaint;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MainCP extends ContentProvider {
    public final static String DBNAME = "Events";
    public final static String TABLE_NAMESTABLE = "EventTable";
    public static final Uri CONTENT_URI = Uri.parse("content://edu.fsu.cen4020.android.procrasinaint.MainCP.provider");
    MainDatabaseHelper mOpenHelper;

    public final static String CALENDAR_ID ="CALENDAR_ID";
    public final static String ORGANIZER = "ORGANIZER";
    public final static String TITLE = "TITLE";
    public final static String EVENT_LOCATION = "EVENT_LOCATION";
    public final static String DESCRIPTION = "DESCRIPTION";
    public final static String DTSTART = "DTSTART";
    public final static String DTEND = "DTEND";
    public final static String LAST_DATE = "LAST_DATE";
    public final static String RRule = "RRule";
    public final static String DURATION = "DURATION";
    public final static String NEW = "NEW";

    private static final String SQL_CREATE_MAIN =
            "CREATE TABLE " + TABLE_NAMESTABLE+" ( "+
                    " CALENDAR_ID INTEGER PRIMARY KEY, " +
                    "TITLE TEXT, " +
                    "DESCRIPTION TEXT," +
                    "DURATION TEXT," +
                    "RRule TEXT," +
                    "DTSTART INTEGER," +
                    "DTEND INTEGER," +
                    "NEW INTEGER," +
                    "LAST_DATE INTEGER)";

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context){
            super(context, DBNAME,null,1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MAIN);
        }
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}
    }

    public MainCP() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return  mOpenHelper
                .getWritableDatabase()
                .delete(TABLE_NAMESTABLE, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mOpenHelper
                .getWritableDatabase()
                .insert(TABLE_NAMESTABLE, null, values);
        return Uri.withAppendedPath(CONTENT_URI, ""+id);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mOpenHelper
                .getReadableDatabase()
                .query(TABLE_NAMESTABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return mOpenHelper
                .getWritableDatabase()
                .update(TABLE_NAMESTABLE, values, selection, selectionArgs);
    }
}
