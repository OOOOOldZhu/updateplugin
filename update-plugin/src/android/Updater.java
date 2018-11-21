package com.microduino.mDesigner;

import org.apache.cordova.CordovaPlugin;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.json.JSONException;


/*
 * ：Created by z on 2018/11/20
 */

public class Updater extends CordovaPlugin {

    static int count = 0;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if ("check".equals(action)) {
            if(count != 0)return true;
            count++;
            // 获取activity和context --> cordova.getActivity()和cordova.getContext()
            Toast.makeText(cordova.getContext(), args.getString(0), Toast.LENGTH_SHORT).show();
            check();
            return true;
        }
        return false;
    }
    private void check(){
        AppUpdateTool.init(cordova.getActivity(), "https://mcotton.microduino.cn/api/v2.0/app_versions/IBB_ANDROID"
                , new AppUpdateTool.AppUpdateListener() {
                    @Override
                    public void onUserCancel() {

                    }

                    @Override
                    public void onCheckError(Throwable t) {

                    }

                    @Override
                    public void onInstalled() {

                    }

                    @Override
                    public void noUpdate() {

                    }

                    @Override
                    public void onDownloadComplete() {

                    }

                    @Override
                    public void onDownloadError(Throwable t) {

                    }
                });

    }
}
