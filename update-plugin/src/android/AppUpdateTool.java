package com.microduino.mDesigner;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.base.UpdateStrategy;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;

/*
 * ：Created by z on 2018/10/17
 */
public class AppUpdateTool {

    private static String TAG = "xiaoqiang";
    static Context context;

    public static void init(Context newcontext, String url, AppUpdateListener newAppUpdateListener) {
        appUpdateListener = newAppUpdateListener;
        context = newcontext;
        UpdateConfig.getConfig()
                .setUrl(url)
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
/*[
                       {
                           "_id": "5bb0aabf44e5aa0011c3f7e2",
                               "appKey": "IBB_ANDROID",
                               "releaseInfo": "",
                               "__v": 0,
                               "downloadAt": "2018-10-15T07:32:55.197Z",
                               "downloadCounts": 21,
                               "status": 4,
                               "updatedAt": "2018-09-30T10:51:43.099Z",
                               "createdAt": "2018-09-30T10:51:43.099Z",
                               "updateUrl": "http://oss.microduino.cn/download/download/IBBV1.1.0.apk",
                               "ignorable": true,
                               "buildCode": 20180930,
                               "version": "v1.1.0"
                       },
                       {
                           "_id": "5b9b59db44e5aa0011c3edb3",
                               "appKey": "IBB_ANDROID",
                               "releaseInfo": "",
                               "__v": 0,
                               "downloadAt": "2018-09-30T10:43:11.650Z",
                               "downloadCounts": 334,
                               "status": 4,
                               "updatedAt": "2018-09-14T06:48:59.607Z",
                               "createdAt": "2018-09-14T06:48:59.607Z",
                               "updateUrl": "http://oss.microduino.cn/download/download/IBBV1.0.2.apk",
                               "ignorable": true,
                               "buildCode": 20180910,
                               "version": "v1.0.2"
                       }
]*/
                        JSONArray jarr = null;
                        JSONObject jsonObject = null;
                        int maxBuildCode = 0;
                        try {
                            jarr = new JSONArray(response);
                            for (int i = 0; i < jarr.length(); i++) {
                                int code = jarr.getJSONObject(i).getInt("buildCode");
                                if (code > maxBuildCode) {
                                    maxBuildCode = code;
                                }
                            }
                            for (int i = 0; i < jarr.length(); i++) {
                                int code = jarr.getJSONObject(i).getInt("buildCode");
                                if (code == maxBuildCode) {
                                    jsonObject = jarr.getJSONObject(i);
                                }
                            }
                            // 此处模拟一个Update对象
                            Update update = new Update();
                            // 云端apk包的下载地址
                            update.setUpdateUrl(jsonObject.getString("updateUrl"));
                            // 云端apk包的版本号
                            //cloudVersionCode = jsonObject.getInt("buildCode");
                            update.setVersionCode(jsonObject.getInt("buildCode"));
                            // 云端apk包的版本名称
                            update.setVersionName(jsonObject.optString("version"));
                            // 云端apk包的更新内容
                            update.setUpdateContent(jsonObject.optString("releaseInfo"));
                            // 云端apk包是否为强制更新
                            update.setForced(false);
                            // 是否显示忽略此次版本更新 按钮
                            /*jsonObject.optBoolean("ignorable")*/
                            update.setIgnore(false);
                            return update;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                })
                .setCheckCallback(new CheckCallback() {
                    @Override
                    public void onCheckStart() {
                        Log.i(TAG, "onCheckStart: ");
                    }

                    @Override
                    public void hasUpdate(Update update) {
                        Log.i(TAG, "hasUpdate: ");
                    }

                    @Override
                    public void noUpdate() {
                        Log.i(TAG, "noUpdate: ");
                        appUpdateListener.noUpdate();
                    }

                    @Override
                    public void onCheckError(Throwable t) {
                        Log.i(TAG, "onCheckError: "+t);
                        appUpdateListener.onCheckError(t);
                    }

                    @Override
                    public void onUserCancel() {
                        Log.i(TAG, "onUserCancel: ");
                        appUpdateListener.onUserCancel();
                    }

                    @Override
                    public void onCheckIgnore(Update update) {
                        Log.i(TAG, "onCheckIgnore: ");
                    }
                })
                .setDownloadCallback(new DownloadCallback() {
                    @Override
                    public void onDownloadStart() {
                        Log.i(TAG, "onDownloadStart: ");
                    }

                    @Override
                    public void onDownloadComplete(File file) {
                        Log.i(TAG, "onDownloadComplete: ");
                        appUpdateListener.onDownloadComplete();
                    }

                    @Override
                    public void onDownloadProgress(long current, long total) {
                        //Log.i(TAG, "onDownloadProgress: current = "+current+"  total = "+total);
                    }

                    @Override
                    public void onDownloadError(Throwable t) {
                        Log.i(TAG, "onDownloadError: "+t);
                        appUpdateListener.onDownloadError(t);
                    }
                });
        UpdateBuilder.create()
                .setUpdateStrategy(new UpdateStrategy() {
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        // 有新更新直接展示
                        return true;
                    }

                    @Override
                    public boolean isAutoInstall() {
                        return true;
                    }

                    @Override
                    public boolean isShowDownloadDialog() {
                        // 展示下载进度
                        return true;
                    }
                })
                /*.setInstallStrategy(new InstallStrategy() {
                    @Override
                    public void install(Context context, String filename, Update update) {

                    }
                })*/
                .check();//检查版本更新的代码
    }

    static AppUpdateListener appUpdateListener;

    public static interface AppUpdateListener {

        void onUserCancel();

        void onDownloadError(Throwable t);

        void onCheckError(Throwable t);

        void onInstalled();

        void noUpdate();

        void onDownloadComplete();
    }
}
