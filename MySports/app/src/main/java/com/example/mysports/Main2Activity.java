package com.example.mysports;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {
    private TextView title;
private Button button1,button2;
private Button btn_youdao,btn_CLR,btn_xinlv;
private int imageIds[] = {R.drawable.run1,R.drawable.run2,R.drawable.run4,R.drawable.run3};
private String titles[] = new String[]{"生命在于运动！","锻炼贵在坚持！","掌握科学锻炼方法！","成为更好的自己！"};
private ArrayList<ImageView> images = new ArrayList<>();
private ViewPager vp;
private  int currentItem;//记录当前页面
    private ScheduledExecutorService scheduledExecutorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActivitiesJudgement.getInstance().addActivity(this);
        title = findViewById(R.id.tv_test_title);
        button1 = findViewById(R.id.login_btn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,LoginActivity.class));
            }
        });
        button2 = findViewById(R.id.register_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,RegisterActivity.class));
            }
        });
        btn_CLR = findViewById(R.id.buttonCLL);
        btn_xinlv = findViewById(R.id.buttonPulse);
        btn_youdao = findViewById(R.id.buttonDic);
        btn_xinlv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Main2Activity.this,PulseActivity.class);
                startActivity(intent);
            }
        });
        btn_CLR.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Main2Activity.this,CLRCaculateActivity.class);
                startActivity(intent);
            }
        });
        btn_youdao.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Main2Activity.this,TranslateActivity.class);
                startActivity(intent);
            }
        });
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds[i]);

            images.add(imageView);
        }
        class ViewPagerAdapter extends PagerAdapter{
            public int getCount(){
                return images.size();
            }
            public boolean isViewFromObject(View view,Object object){
                return view == object;
            }
            public void destroyItem(ViewGroup container, int position, Object object) {
                //将试图移除试图组
                View v =images.get(position);
                container.removeView(v);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //将试图添加进试图组
                View v =images.get(position);
                container.addView(v);
                return v;
            }
        }
        vp = findViewById(R.id.vp);
        vp.setAdapter(new ViewPagerAdapter());
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });}

        protected void onStart() {
            super.onStart();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            //每隔两秒换一张图片
            scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(),2,2, TimeUnit.SECONDS);

        }
        //实现一个碎片的接口
        class ViewPagerTask implements Runnable{

            @Override
            public void run() {
                currentItem = (currentItem+1)%imageIds.length;
                //更新界面
                handler.obtainMessage().sendToTarget();
            }
        }
        //在handler进行碎片跳转
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                vp.setCurrentItem(currentItem);
            }
        };
    }
