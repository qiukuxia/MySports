package com.example.mysports;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,MediaPlayer.OnCompletionListener {
    //除了实现音乐播放，还实现进度条监听和播放结束监听
    private String engPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/eng/";
    private String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/music/";

    private int type;

    private List<File> fileDatas = new ArrayList<>();
    private ListView fileList;
    private Button buttonPre;
    private Button buttonPlay;
    private Button buttonnext;
    private RelativeLayout btnBox;
    private TextView textViewInfo;
    private ScrollView infoBox;
    private SeekBar mSeekBar;//歌曲进度条
    private TextView mTotalTimeTv;//歌曲总时间
    private TextView mCurrentTimeTv;//歌曲当前时间
    private TextView now;//当前歌曲名称
    FileAdapter fileAdapter;

    private int chooseIndex;//当前音乐播放曲目
    private static MediaPlayer mediaPlayer=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ActivitiesJudgement.getInstance().addActivity(this);
        type = getIntent().getExtras().getInt("type");

        initData();
        initView();
    }

    private void initData() {

        if (type == 0)
        {
            File f = new File(engPath);
            if (!f.exists())
            {
                f.mkdir();
            }
            File[] tf = f.listFiles();
            for (int i = 0;i<tf.length;i++)
            {
                if (tf[i].getName().toLowerCase().endsWith("mp3"))
                {
                    fileDatas.add(tf[i]);
                }

            }
        }
        else if (type == 1)
        {
            File f = new File(musicPath);
            if (!f.exists())
            {
                f.mkdir();
            }
            File[] tf = f.listFiles();
            for (int i = 0;i<tf.length;i++)
            {
                if (tf[i].getName().toLowerCase().endsWith("mp3"))//转换为小写
                {
                    fileDatas.add(tf[i]);
                }

            }
        }

        fileAdapter = new FileAdapter();

    }

    private void initView() {
        fileList = (ListView) findViewById(R.id.fileList);
        buttonPre = (Button) findViewById(R.id.buttonPre);
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonnext = (Button) findViewById(R.id.buttonnext);
        btnBox = (RelativeLayout) findViewById(R.id.btnBox);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo1);
        infoBox = (ScrollView) findViewById(R.id.infoBox);
        mSeekBar = findViewById(R.id.seekBar1);
        mCurrentTimeTv=findViewById(R.id.current_time_tv);
        mTotalTimeTv = findViewById(R.id.total_time_tv);
        now = findViewById(R.id.now);

        fileList.setAdapter(fileAdapter);

        buttonPre.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        buttonnext.setOnClickListener(this);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseIndex = position;//获取当前点击条目位置
                Play();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPre:
                chooseIndex--;
                if (chooseIndex<0)
                {
                    chooseIndex = fileDatas.size()-1;
                }
                Play();
                break;
            case R.id.buttonPlay:

                if (mediaPlayer!= null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        buttonPlay.setText("播放");
                    }
                    else if (!mediaPlayer.isPlaying())
                    {
                        mediaPlayer.start();
                        buttonPlay.setText("暂停");
                    }
                }
                break;
            case R.id.buttonnext:
                chooseIndex++;
                if (chooseIndex>=fileDatas.size())
                {
                    chooseIndex = 0;
                }
                Play();
                break;
        }
    }
    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //展示进度条和当前时间
            int progress = mediaPlayer.getCurrentPosition();
            mSeekBar.setProgress(progress);

            mCurrentTimeTv.setText(parseTime(progress));
            //继续定时发送数据
            updateProgress();
            return true;
        }
    });

    private void Play()
    {
        mSeekBar.setOnSeekBarChangeListener(this);//设置进度条拖拽监听器
        if(mediaPlayer == null)
        {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);//监听当前歌曲是否结束，结束后自动播放下一首
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        mediaPlayer.setOnCompletionListener(this);//监听当前歌曲是否结束，结束后自动播放下一首
        File f = fileDatas.get(chooseIndex);
        String title = f.getName();
        now.setText(title);//将歌名赋值给now

        try {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
//                mediaPlayer.reset();
//                mediaPlayer.release();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(f.getAbsolutePath());
            try {

                mediaPlayer.prepare();

            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
            mediaPlayer.start();
            buttonPlay.setText("暂停");
            mSeekBar.setProgress(0);//初始化进度条
            mSeekBar.setMax(mediaPlayer.getDuration());//进度条长度设置为当前歌曲时间
            mTotalTimeTv.setText(parseTime(mediaPlayer.getDuration()));//显示歌曲总时长
            updateProgress();//更新进度条
            ShowInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }

    }
    private void updateProgress(){
        //使用handler每隔一秒发送一次消息，通知进度条更新
        Message msg = Message.obtain();//获取一个消息
        //使用mediaplayer获取当前时间，除以总时间
        int progress = mediaPlayer.getCurrentPosition();
        msg.arg1 = progress;
        mhandler.sendMessageDelayed(msg,1000);//将某个需要处理的消息事件发送给handler来处理，并且在此之前按你传入的参数延迟一定的时间。
    }
    private String parseTime(int original_time){
        //时间格式化方法，参数为原始时间信息
        //SimpleDateFormat 是一个以国别敏感的方式格式化和分析数据的具体类。 它允许格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//时间格式 分：秒
        String newTime = simpleDateFormat.format(new Date(original_time));
        return newTime;
    }

    //实现进度条拖拽监听器   3个方法
    public void onProgressChanged(SeekBar seekBar,int i,boolean b){}
    public void onStartTrackingTouch(SeekBar seekBar){}
    //当停止对进度条的拖拽动作时，获取进度条当前进度，并将进度返回给mediaplayer
    public void onStopTrackingTouch(SeekBar seekBar){
        int progress = seekBar.getProgress();
        mediaPlayer.seekTo(progress);
    }

    public  void onCompletion(MediaPlayer mediaPlayer1) {//监听当前歌曲是否结束，结束后自动播放下一首
        chooseIndex++;
        if (chooseIndex>=fileDatas.size())
        {
            chooseIndex = 0;
        }
        Play();
    }
    private void ShowInfo() {
        try {
            File f = new File(fileDatas.get(chooseIndex).getAbsolutePath().split("\\.")[0]+".txt");
            if (f.exists())
            {
                StringBuffer sb = new StringBuffer();
                try {
                    InputStream instream = new FileInputStream(f);
                    if (instream != null)
                    {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line;
                        //分行读取
                        while (( line = buffreader.readLine()) != null) {
                            line+="\n";
                            sb.append(line);
                        }
                        instream.close();
                        textViewInfo.setText(sb.toString());
                    }
                }
                catch (java.io.FileNotFoundException e)
                {
                    Log.d("TestFile", "The File doesn't not exist.");
                }
                catch (IOException e)
                {
                    Log.d("TestFile", e.getMessage());
                }
            }
            else
            {
                textViewInfo.setText("无信息");
            }
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
            textViewInfo.setText("无信息");
        }

    }
    public  void onBackPressed(){//重写返回方法，保证音乐后台播放
        Intent intent = new Intent(AudioActivity.this,MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    class FileAdapter extends BaseAdapter {
        private LayoutInflater mInflater = LayoutInflater.from(AudioActivity.this);

        class ViewHolder {
            public TextView tvplace, tvFlag;

        }
        ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            /**说明：     */

            return fileDatas.size();
        }

        @Override
        public Object getItem(int position) {
            /**说明：     */

            return null;
        }

        @Override
        public long getItemId(int position) {
            /**说明：     */

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**说明：     */
            convertView = mInflater.inflate(
                    R.layout.item, null);

            viewHolder = new ViewHolder();


            viewHolder.tvplace = (TextView) convertView
                    .findViewById(R.id.textView1);
            viewHolder.tvFlag = (TextView) convertView
                    .findViewById(R.id.textView2);

            convertView.setTag(viewHolder);

            viewHolder.tvplace.setText(fileDatas.get(position).getName());
            viewHolder.tvFlag.setText("");

            return convertView;
        }

    }
}
