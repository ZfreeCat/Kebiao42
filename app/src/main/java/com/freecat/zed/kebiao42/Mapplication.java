package com.freecat.zed.kebiao42;

import android.app.Application;
import android.content.Context;

/**
 * Created by zed on 16-3-30.
 */
public class Mapplication extends Application {
    private static Context context;
    @Override
    public void onCreate(){
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
