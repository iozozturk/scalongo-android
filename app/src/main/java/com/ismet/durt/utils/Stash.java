package com.ismet.durt.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Optional;

/**
 * Created by ismet on 01/02/16.
 */
public class Stash {
    private static String SESSION_ID = "sessionId";

    Context context;
    SharedPreferences pref;

    public Stash(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("stash", 0);
    }

    public Optional<String> getSessionId() {
        return Optional.fromNullable(pref.getString(SESSION_ID, null));
    }

    public void setSessionId(String value) {
        pref.edit().putString(SESSION_ID, value).apply();
    }

}
