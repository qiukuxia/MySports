package com.example.mysports;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mysports.database.Paths;

public class ChartActivity extends AppCompatActivity {
    private PanelPieChart speedChart,distanceChart,scoreChart;
    private App mapp;
    private long sum,speedA,speedB,speedC,distanceA,distanceB,distanceC,bad,normal,good,great;
    private RelativeLayout box1,box2,box3;
    private GestureDetector gestureDetector;
    final int Right = 0;
    final int Left = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        gestureDetector = new GestureDetector(ChartActivity.this,onGestureListener);
        speedChart = findViewById(R.id.speedChart);
        distanceChart = findViewById(R.id.distanceChart);
        scoreChart =findViewById(R.id.scoreChart);
        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        speedChart.title = "速度饼状图";
        distanceChart.title = "运动量饼状图";
        scoreChart.title = "综合成绩饼状图";
        mapp = (App) getApplication();
        sum = Paths.count(Paths.class,"users = ?",new String[]{mapp.getUserName()},null,null,null);
        speedA = Paths.count(Paths.class,"speed = ?",new String[]{"a"},null,null,null );
        speedB = Paths.count(Paths.class,"speed = ?",new String[]{"b"},null,null,null );
        speedC = Paths.count(Paths.class,"speed = ?",new String[]{"c"},null,null,null );
        distanceA = Paths.count(Paths.class,"distance = ?",new String[]{"a"},null,null,null );
        distanceB = Paths.count(Paths.class,"distance = ?",new String[]{"b"},null,null,null );
        distanceC = Paths.count(Paths.class,"distance = ?",new String[]{"c"},null,null,null );
        bad = Paths.count(Paths.class,"score = ?",new String[]{"bad"},null,null,null );
        normal = Paths.count(Paths.class,"score = ?",new String[]{"normal"},null,null,null );
        good = Paths.count(Paths.class,"score = ?",new String[]{"good"},null,null,null );
        great = Paths.count(Paths.class,"score = ?",new String[]{"great"},null,null,null );
        if(sum == 0){
        speedChart.arrPer = new float[]{speedA*100/999999,speedB*100/999999,speedC*100/999999,0f};
        speedChart.score = new String[]{"无数据","无数据","无数据","无数据"};
        distanceChart.arrPer = new float[]{distanceA*100/999999,distanceB*100/999999,distanceC*100/999999,0f};
        distanceChart.score = new String[]{"无数据","无数据","无数据","无数据"};
        scoreChart.arrPer = new float[]{great*100/999999,good*100/999999,normal*100/999999,bad*100/999999};
        scoreChart.score = new String[]{"无数据","无数据","无数据","无数据"};}
        else {
            speedChart.arrPer = new float[]{speedA*100/sum,speedB*100/sum,speedC*100/sum,0f};
            speedChart.score = new String[]{"A","B","C","其他"};
            distanceChart.arrPer = new float[]{distanceA*100/sum,distanceB*100/sum,distanceC*100/sum,0f};
            distanceChart.score = new String[]{"A","B","C","其他"};
            scoreChart.arrPer = new float[]{great*100/sum,good*100/sum,normal*100/sum,bad*100/sum};
            scoreChart.score = new String[]{"优秀","良好","一般","较差"};}
        }
    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {//实现滑动事件监听
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {//根据xy坐标来监听滑动事件
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            if(x>0){
                doResult(Right);//判断往右滑动
            }
            else if (x<0){
                doResult(Left);//判断往左滑动
            }

            return true;
        }
    };
    public boolean onTouchEvent(MotionEvent event){//设置滑动监听
        return gestureDetector.onTouchEvent(event);
    }
    public void doResult(int action){
        switch (action){
            case Right:{
                if(box1.getVisibility()==View.VISIBLE)//当界面1时，右滑跳转界面3
                {
                    box3.setVisibility(View.VISIBLE);
                    box2.setVisibility(View.GONE);
                    box1.setVisibility(View.GONE);
                }
                else if(box2.getVisibility() == View.VISIBLE)//当界面2时，右滑跳转界面1
                {
                    box1.setVisibility(View.VISIBLE);
                    box2.setVisibility(View.GONE);
                    box3.setVisibility(View.GONE);
                }
                else {
                    box2.setVisibility(View.VISIBLE);
                    box1.setVisibility(View.GONE);
                    box3.setVisibility(View.GONE);}//当界面3时，右滑跳转界面2
            }
            break;
            case Left:{
                if(box1.getVisibility()==View.VISIBLE)//当界面1时，左滑跳转界面2
                {
                    box2.setVisibility(View.VISIBLE);
                    box1.setVisibility(View.GONE);
                    box3.setVisibility(View.GONE);

                }
                else if(box2.getVisibility() == View.VISIBLE)//当界面2时，右滑跳转界面3
                {
                    box3.setVisibility(View.VISIBLE);
                    box2.setVisibility(View.GONE);
                    box1.setVisibility(View.GONE);

                }
                else {
                    box1.setVisibility(View.VISIBLE);
                    box2.setVisibility(View.GONE);
                    box3.setVisibility(View.GONE);
                }//当界面3时，右滑跳转界面1}
            }
            break;
        }
    }
    }

