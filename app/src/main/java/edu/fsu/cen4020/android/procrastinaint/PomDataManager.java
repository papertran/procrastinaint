package edu.fsu.cen4020.android.procrastinaint;

import java.util.HashMap;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PomDataManager {
    private static final String TAG = PomDataManager.class.getCanonicalName();
    SharedPreferences pref;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "prefStorage";

    public static final String KEY_NAME = "userID";
    private static final String PREF_POM = "AllTimeP";
    private static final String PREF_TOM = "AllTimeGP";
    private static final String PREF_TIME = "AllTimeTime";

    public PomDataManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

//    public void createSession(long AllTimeP, long AllTimeGP, long AllTimeTime){
    public void createSession(String user, long AllTimeP){
        Log.i(TAG, "DATA BEING STORED IS " + AllTimeP);

        editor.putString(KEY_NAME, user);
        editor.putLong(PREF_POM, AllTimeP);

        Log.i(TAG, "DATA BEING STORED IS " + user + AllTimeP);

//        editor.putLong(PREF_TOM, AllTimeGP);
//        editor.putLong(PREF_TIME, AllTimeTime);

        editor.commit();
    }

    public HashMap<String, String> getData(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));

        Log.i(TAG, "RETRIEVED DATA IS " + user.get(KEY_NAME));

        return user;
    }

}
