package com.example.mysports;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.orm.SugarContext;
import com.youdao.sdk.app.YouDaoApplication;

/**
 * Created by Roc on 2018/4/16.
 */

public class App extends Application {

    private String userName;

    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(getApplicationContext());

        SugarContext.init(getApplicationContext());

        YouDaoApplication.init(this,"375e22dfd6b8e3b4");//有道翻译key
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }}

