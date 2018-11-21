package com.microduino.mDesigner;

import org.apache.cordova.CordovaPlugin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/*
 * ：Created by z on 2018/11/20
 */

public class Updater extends CordovaPlugin {

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if ("check".equals(action)) {
            // 获取activity和context --> cordova.getActivity()和cordova.getContext()
            Toast.makeText(cordova.getContext(), args.getString(0), Toast.LENGTH_SHORT).show();
            check();
            return true;
        }
        return false;
    }
    private void check(){
        AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://mcotton-t.office.microduino.cn/api/v2.0/app_versions/MD3_ANDROID")
                .request(new RequestVersionListener() {
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        Log.i("zhu", "onRequestVersionSuccess: "+result);
                        //拿到服务器返回的数据，解析，拿到downloadUrl和一些其他的UI数据
                        //如果是最新版本直接return null
                        UIData uiData = UIData
                                .create()
//                                .setDownloadUrl(downloadUrl)
                                .setTitle("title")
                                .setContent("content . . ");
                        //放一些其他的UI参数，拿到后面自定义界面使用
                        uiData.getVersionBundle().putString("key", "your value");
                        return uiData;
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        Log.i("zhu", "onRequestVersionFailure: "+message);
                    }
                }).executeMission(cordova.getContext());

    }
}
