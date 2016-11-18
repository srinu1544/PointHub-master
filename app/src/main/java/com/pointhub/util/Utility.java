package com.pointhub.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;

/**
 * Created by Venu gopal on 17-11-2016.
 */

public class Utility {


    static Gson gson = null;

    static boolean testing = true;

    public static synchronized Gson getGsonObject() {

        if(null == gson){
            gson = new Gson();
        }

        return gson;
    }

    public static synchronized boolean isTesting () {
        return testing;
    }

}
