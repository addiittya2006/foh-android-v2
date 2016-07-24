package org.fundsofhope.android.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by anip on 23/07/16.
 */
public class ApplicationConstant {

    //Flag for debugging log mode
    public static boolean isDebuggable = true;

    //server connection timeout (in minutes)
    public static final int SERVER_TIMEOUT = 1;
    public static final int READ_TIMEOUT = 1;

    // Application Constants
    public static final String PLAY_STORE_URL_OLD = "market://details?id=";
    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
