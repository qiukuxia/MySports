package com.example.mysports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.mysports.database.Paths;
import com.example.mysports.database.Pos;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mapFragmentView;
    private TextView textViewInfo;

    MapView mMapView;
    BaiduMap mBaiduMap;

    private com.example.mysports.database.Paths myPath;
    private List<Pos> posList = new ArrayList<>();
    private List<LatLng> pointList = new ArrayList<>();
    Polyline mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
    }

    private void initView() {
        mapFragmentView = (MapView) findViewById(R.id.mapFragmentView);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo);

        mMapView = (MapView) findViewById(R.id.mapFragmentView);
        mBaiduMap = mMapView.getMap();

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);

        String name = getIntent().getExtras().getString("path");
        if (!TextUtils.isEmpty(name)) {
            List<Paths> tp = Paths.find(Paths.class, "paths = ?", name);
            if (tp.size() > 0) {
                myPath = tp.get(0);
            } else {
                finish();
            }
        } else {
            finish();
        }

        posList = Pos.find(Pos.class, "path = ?", new String[]{name}, null, "num desc", null);
        for (int i=0;i<posList.size();i++)
        {
            pointList.add(new LatLng(posList.get(i).getLat(),posList.get(i).getLon()));
        }

        DrawPath();

        int d = myPath.getNum();
        String disStr = "";
        if (d >= 0 && d < 1000) {
            String[] dis = (d + "").split("\\.");
            disStr = "距离：" + dis[0] + "m";
        } else if (d >= 1000) {
            d = d / 1000;
            String[] dis = (d + "").split("\\.");
            if (dis.length == 2) {
                disStr = "距离：" + dis[0] + "." + dis[1].substring(0, 1) + "Km";
            } else {
                disStr = "距离：" + d + "Km";
            }

        }

        int pt = myPath.getPasst();
        String sp = "速度："+(int) (d*3.6/pt)+"Km/h";
        String ptstr = "时间："+pt/60+"m"+pt%60+"s";
        textViewInfo.setText(myPath.getRemark()+"\n"+disStr+"\n"+ptstr+"\n"+sp);
//        textViewInfo.setText(myPath.getRemark());
    }

    private void DrawPath() {

        if (pointList.size() > 1) {
            OverlayOptions ooPolyline = new PolylineOptions().width(10)
                    .color(0xFFFF2200).points(pointList);
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        }

        zoomToSpan(pointList);
    }

    public void zoomToSpan(List< LatLng > posList) {//缩放地图
        if (mBaiduMap == null) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();//地理范围构造器

        for (int i = 0; i < posList.size(); i++) {
            builder.include(posList.get(i));
        }
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLngBounds(builder.build());
        mBaiduMap.animateMapStatus(u1);
        //参数MapStatusUpdate描述的是地图状态将要发生的变化。也就是说，要想改变地图的某些状态，
        // 需要构造出一个MapStatusUpdate对象，然后使用animateMapStatus()方法来更新地图状态
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapFragmentView:

                break;
        }
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
