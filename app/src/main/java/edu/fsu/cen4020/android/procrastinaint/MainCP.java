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

    public final static String _ID ="_ID";
    public final static String Title = "Title";
    public final static String StartDay = "StartDay";
    public final static String EndDay = "EndDay";
    public final static String StartDate = "StartDate";
    public final static String EndDate = "EndDate";
    public final static String Location = "Location";
    public final static String Notes = "Notes";
    public final static String Invitee = "Invitee";

    private static final String SQL_CREATE_MAIN =
            "CREATE TABLE " + TABLE_NAMESTABLE+" ( "+
                    " CALENDAR_ID INTEGER PRIMARY KEY, " +
                    "ORGANIZER TEXT, " +
                    "TITLE TEXT, " +
                    "EVENT_LOCATION TEXT," +
                    "DESCRIPTION TEXT," +
                    "DTSTART TEXT," +
                    "DTEND TEXT," +
                    "LAST_DATE TEXT," +
                    "Invitee TEXT)";

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
