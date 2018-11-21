package com.microduino.mDesigner;

import org.apache.cordova.CordovaPlugin;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
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

    static int count = 0;
    private DownloadBuilder downloadBuilder;

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
        Log.i("zhu", "onRequestVersionSuccess: "+cordova.getContext());
        Log.i("zhu", "onRequestVersionSuccess: "+cordova.getActivity());
        Activity activity = cordova.getActivity();
        downloadBuilder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://mcotton-t.office.microduino.cn/api/v2.0/app_versions/MD3_ANDROID")
                .request(new RequestVersionListener() {
                    @Override
                    public UIData onRequestVersionSuccess(String result) {

                        Log.i("zhu", "onRequestVersionSuccess: "+result);
                        //拿到服务器返回的数据，解析，拿到downloadUrl和一些其他的UI数据
                        //如果是最新版本直接return null
                        UIData uiData = UIData.create();
                        uiData.setTitle("aaaaaaa");
                        uiData.setDownloadUrl("http://test-1251233192.coscd.myqcloud.com/1_1.apk");
                        uiData.setContent("cccccccc");
                        return uiData;
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        Log.i("zhu", "onRequestVersionFailure: "+message);
                    }
                })
                .setSilentDownload(true)
                .setDirectDownload(true)
                .setShowNotification(false)
                 .setShowDownloadFailDialog(false)
                .setShowDownloadingDialog(false)
                .setCustomVersionDialogListener((context, versionBundle) -> {
                            BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_one_layout);
                            //versionBundle 就是UIData，之前开发者传入的，在这里可以拿出UI数据并展示
                            TextView textView = baseDialog.findViewById(R.id.tv_msg);
                            textView.setText(versionBundle.getContent());
                            return baseDialog;
                        });

        downloadBuilder.executeMission(activity);

    }
}
