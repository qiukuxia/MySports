package com.example.mysports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.mysports.database.Paths;
import com.example.mysports.database.Pos;

import java.util.ArrayList;
import java.util.List;

public class RunActivity extends AppCompatActivity implements View.OnClickListener {

    MapView mMapView;
    BaiduMap mBaiduMap;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;

    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    private List<LatLng> runPath = new ArrayList<>();
    Polyline mPolyline;


    private TextView textViewDis;
    private TextView textViewSpeed;
    private TextView textViewPower;
    private Button buttonStop;
    private Chronometer chronometer2;

    private long startTime;
    private boolean isRun = false;
    private int num;
    private TextView textViewTime;


    private App myApp;
    private int speed;
    private double disTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        myApp = (App)getApplication();

        if (MyService.getInstance().isRun())
        {
            startTime = MyService.getInstance().getStartTime();
            isRun = true;
        }
        else
        {
            isRun = true;
            MyService.getInstance().setRun(isRun);
        }

        initView();

        initMap();
    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.mapFragmentView);
        mBaiduMap = mMapView.getMap();

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//设置定位频率，1秒1次
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initView() {
        textViewDis = (TextView) findViewById(R.id.textViewDis);
        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        textViewPower = (TextView) findViewById(R.id.textViewPower);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        buttonStop = (Button) findViewById(R.id.buttonStop);
        chronometer2 = (Chronometer) findViewById(R.id.chronometer2);
        chronometer2.start();//设置计时器
        chronometer2.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                //距离
                if (isRun)
                {
                    double d = 0;
                    for (int i = 1; i < runPath.size(); i++) {
                        d += DistanceUtil.getDistance(runPath.get(i - 1), runPath.get(i));
                    }
                    disTotal = d;
                    if (d >= 0 && d < 1000) {
                        String[] dis = (d + "").split("\\.");
                        textViewDis.setText("距离\n" + dis[0] + "m");
                    } else if (d >= 1000) {
                        d = d / 1000;
                        String[] dis = (d + "").split("\\.");
                        if (dis.length == 2) {
                            textViewDis.setText("距离\n" + dis[0] + "." + dis[1].substring(0, 1) + "Km");
                        } else {
                            textViewDis.setText("距离\n" + d + "Km");
                        }

                    }
                    else
                    {
                        textViewDis.setText("距离\n" + d + "Km");
                    }

                    //时间
                    int passt = 0;
                    if (startTime == 0) {
                        startTime = System.currentTimeMillis();
                        MyService.getInstance().setStartTime(startTime);
                    } else {
                        passt = (int) ((System.currentTimeMillis()-startTime)/1000);

                        textViewTime.setText("时间\n"+passt/60+"m"+passt%60+"s");
                    }

                    //速度
                    if (passt>0)
                    {
                        if(d>20)
                        {
                        speed = (int) (d*3.6/passt);}
                        else speed = (int) d*3600/passt;
                        textViewSpeed.setText("速度\n"+speed+"Km/h");
                    }
                }

            }        });

        buttonStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonStop:

                AlertDialog.Builder b=new AlertDialog.Builder(this).setTitle("是否结束跑步？");

                b
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("结束", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                isRun = false;
                                MyService.getInstance().setRun(isRun);
                                RecordPath();
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作

                            }
                        });
                b.show();
                break;
        }
    }

    private void RecordPath()
    {
        View view;
        if (runPath.size()>0)
        {
            Paths p = new Paths();
            p.setNum((int)disTotal);
            p.setPasst((int) ((System.currentTimeMillis()-startTime)/1000));
            p.setPaths(myApp.getUserName()+"_"+System.currentTimeMillis());
            p.setUsers(myApp.getUserName());
            p.setTimes(System.currentTimeMillis());
            String rem = "成绩：良好";
            p.setScore("good");
            p.setDistance("b");
            p.setSpeed("b");
            view = LayoutInflater.from(RunActivity.this).inflate(R.layout.layout_dialog_good,null);
            if (disTotal<1000 && speed<4)
            {
                rem = "成绩：较差";
                p.setScore("bad");
                p.setDistance("c");
                p.setSpeed("c");
                view = LayoutInflater.from(RunActivity.this).inflate(R.layout.layout_dialog_bad,null);
            }
            else {
                if (speed < 4) {
                    rem = "成绩：一般";
                    p.setScore("normal");
                    p.setDistance("b");
                    p.setSpeed("c");
                    view = LayoutInflater.from(RunActivity.this).inflate(R.layout.layout_dialog_normal1, null);
                } else {
                    if (disTotal < 1000) {
                        rem = "成绩：一般";
                        p.setScore("normal");
                        p.setDistance("c");
                        p.setSpeed("b");
                        view = LayoutInflater.from(RunActivity.this).inflate(R.layout.layout_dialog_normal2, null);
                    } else if (disTotal > 2000 && speed > 8) {
                        rem = "成绩：优秀！";
                        p.setScore("great");
                        p.setDistance("a");
                        p.setSpeed("a");
                        view = LayoutInflater.from(RunActivity.this).inflate(R.layout.layout_dialog_great, null);
                    }
                    else {
                        p.setScore("good");
                        p.setDistance("b");
                        p.setSpeed("b");
                    }
                }
            }
            p.setRemark(rem);
            p.save();

            for (int i=0;i<runPath.size();i++)
            {
                Pos po = new Pos();
                po.setPath(p.getPaths());
                po.setNum(i);
                po.setLat(runPath.get(i).latitude);
                po.setLon(runPath.get(i).longitude);
                po.save();
            }

            AlertDialog.Builder dialog = new AlertDialog.Builder(RunActivity.this);
            dialog.setTitle("成绩报告单").setView(view);
            dialog.setCancelable(false);
            dialog.setPositiveButton("我会努力的！", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
//            Toast.makeText(RunActivity.this, rem, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            if (isRun ) {
//                mCurrentLat = location.getLatitude() + 0.0001 * num;
//                mCurrentLon = location.getLongitude() + 0.0001 * num;
//                num++;
                runPath=MyService.getInstance().getRunPath();
                DrawPath();
            }


            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())

                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
//设置定位数据
            mBaiduMap.setMyLocationData(locData);


            try {

                if (isFirstLoc && !isRun) {
                    isFirstLoc = false;
                }

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(19.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            } catch (Exception e) {

            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void DrawPath() {

        if (runPath.size() > 1) {
            OverlayOptions ooPolyline = new PolylineOptions().width(10)
                    .color(0xFFFF2200).points(runPath);
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        }

//        zoomToSpan(recordList);
    }

    public void zoomToSpan(List<LatLng> posList) {//缩放级别
        if (mBaiduMap == null) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < posList.size(); i++) {
            builder.include(posList.get(i));
        }
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLngBounds(builder.build());
        mBaiduMap.animateMapStatus(u1);

    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
