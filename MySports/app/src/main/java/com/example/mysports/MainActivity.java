package com.example.mysports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.GestureDetector;

import com.example.mysports.util.ToastUtil;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonTips;
    private Button buttonRun;
    private Button buttonRecord;
    private Button buttonMusicPlayer;
    private RelativeLayout box1;
    private Button buttonNote;
    private Button buttonEng;
    private Button buttonDic;
    private Button buttonCLR;
    private Button buttonFood;
    private Button buttonSports;
    private Button buttonPulse;
    private Button buttonDatas;
    private TextView title;
    private String titles[] = new String[]{"生命在于运动！","锻炼贵在坚持！","掌握科学锻炼方法！","成为更好的自己！"};
    private int imageIds[] = {R.drawable.run1,R.drawable.run2,R.drawable.run4,R.drawable.run3};
    private ArrayList<ImageView> images = new ArrayList<>();
    private ViewPager vp;private int oldPosition = 0;//记录上一次点的位置
    private  int currentItem;//记录当前页面
    private ScheduledExecutorService scheduledExecutorService;
    final int Right = 0;
    final int Left = 1;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivitiesJudgement.getInstance().addActivity(this);
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds[i]);

            images.add(imageView);
        }
        title = findViewById(R.id.tv_test_title);
        initView();
        startService(new Intent(this, MyService.class));
        gestureDetector = new GestureDetector(MainActivity.this,onGestureListener);
        class ViewPagerAdapter extends PagerAdapter {
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
                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                Intent intent = new Intent(this,TranslateActivity.class);
                startActivity(intent);
            }
            break;
            case Left:{
                Intent intent = new Intent(MainActivity.this, AudioActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
                break;
        }
    }


    private void initView() {
        buttonTips = findViewById(R.id.buttonTips);
        buttonRun = (Button) findViewById(R.id.buttonRun);
        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonMusicPlayer = findViewById(R.id.buttonMusicPlayer);
        box1 = (RelativeLayout) findViewById(R.id.box1);
        buttonEng = (Button) findViewById(R.id.buttonEng);
        buttonDic = (Button) findViewById(R.id.buttonDic);
        buttonRun.setOnClickListener(this);
        buttonTips.setOnClickListener(this);
        buttonRecord.setOnClickListener(this);
        buttonEng.setOnClickListener(this);
        buttonDic.setOnClickListener(this);
        buttonMusicPlayer.setOnClickListener(this);
        buttonCLR = findViewById(R.id.buttonCLL);
        buttonCLR.setOnClickListener(this);
        buttonDatas = (Button) findViewById(R.id.buttonDatas);
        buttonDatas.setOnClickListener(this);
        buttonPulse = findViewById(R.id.buttonPulse);
        buttonPulse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRun:
                startActivity(new Intent(MainActivity.this, RunActivity.class));
                break;
            case R.id.buttonRecord:
                startActivity(new Intent(MainActivity.this, PathActivity.class));
                break;
            case R.id.buttonMusicPlayer:
                Intent intent1 = new Intent(MainActivity.this, AudioActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
            case R.id.buttonTips:
                final View view;
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_tips,null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setView(view);
                dialog.show();
                break;

            case R.id.buttonEng:
                Intent intent = new Intent(MainActivity.this, AudioActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.buttonDic:
                startActivity(new Intent(MainActivity.this, TranslateActivity.class));
                break;
            case R.id.buttonCLL:
                startActivity(new Intent(MainActivity.this,CLRCaculateActivity.class));
                break;
            case R.id.buttonDatas:
                startActivity(new Intent(MainActivity.this, AnalyseActivity.class));
                break;
            case R.id.buttonPulse:
                startActivity(new Intent(MainActivity.this, PulseActivity.class));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!MyService.getInstance().isRun()) {
                showExitAlert();
            } else {
                ToastUtil.showMsg(this,"请先结束跑步");
            }
        }
        return false;
    }

    private void showExitAlert() {
        AlertDialog.Builder b = new AlertDialog.Builder(this).setTitle("是否退出？");

        b
                //.setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        stopService(new Intent(MainActivity.this, MyService.class));
                        //关闭整个程序
                        ActivitiesJudgement.getInstance().exit();
                        int nPid = Process.myPid();
                        Process.killProcess(nPid);

                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作

                    }
                });
        b.show();

//        DialogView.ShowDialog(BaseActivity.this,getResources().getString(R.string.exit_confirm),null);
    }
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //每隔两秒换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new MainActivity.ViewPagerTask(),2,2, TimeUnit.SECONDS);

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
