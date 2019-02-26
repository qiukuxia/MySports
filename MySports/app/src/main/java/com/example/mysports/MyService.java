package com.example.mysports;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {

    public static MyService instance;

    public static MyService getInstance()
    {
        return instance;
    }

    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;

    private boolean isRun;
    private List<LatLng> runPath = new ArrayList<>();
    private int num;
    private long startTime;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    public MyService() {
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        if (run)
        {
            runPath.clear();
        }

        isRun = run;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<LatLng> getRunPath() {
        return runPath;
    }

    public void setRunPath(List<LatLng> runPath) {
        this.runPath = runPath;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null ) {
                return;
            }

            if (isRun ) {
                mCurrentLat = location.getLatitude()+num*0.0005;
                mCurrentLon = location.getLongitude()+num*0.0005;
                num++;
                runPath.add(new LatLng(mCurrentLat,mCurrentLon));
            }

            Log.e("POS", mCurrentLat + ":" + mCurrentLon);

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
