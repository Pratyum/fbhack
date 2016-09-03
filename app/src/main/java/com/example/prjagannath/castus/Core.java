package com.example.prjagannath.castus;

import android.app.Application;
import android.content.Context;

import com.example.prjagannath.castus.API.APICall;
import com.example.prjagannath.castus.API.AppInfo;
import com.example.prjagannath.castus.CustomEnum.API;

/**
 * Created by prjagannath on 9/2/2016.
 */
public class Core extends Application{
    private static AppInfo appInfo;
    private static Context context;
    private Session exchangeSession;

    public Core() {
    }

    public void onCreate() {
        super.onCreate();
        context = this;
        this.setup();
    }

    public static AppInfo getAppInfo() {
        return appInfo;
    }

    public void getServerTime() {
        getServerTime(appInfo);
    }

    private void setup() {
        appInfo = AppInfo.getInstance();
    }


    public Session getExchangeSession() {
        return this.exchangeSession;
    }

    public void getServerTime(AppInfo appInfo){
        long serverTime;
        APICall apiCall = new APICall(context);
        String result = apiCall.request(API.GET,R.string.API_server_time, null, null);
        if(apiCall.isRequestSuccess(result, true)){
            serverTime = Long.parseLong(result);
            appInfo.setInterval(serverTime);
        }
    }

}
