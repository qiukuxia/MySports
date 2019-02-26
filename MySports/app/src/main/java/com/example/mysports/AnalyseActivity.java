package com.example.mysports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mysports.database.Paths;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnalyseActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView weekText;
    private DataView weekView;
    private TextView dayText;
    private DataView dayView;
    private Button btn_chart;

    private App myApp;

    private List<Paths> pathDatas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        myApp = (App)getApplication();
        pathDatas = Paths.find(Paths.class,"users = ?",new String[]{myApp.getUserName()},null,"times desc",null);

        initView();
    }

    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        weekText = (TextView) findViewById(R.id.weekText);
        weekView = (DataView) findViewById(R.id.weekView);
        dayText = (TextView) findViewById(R.id.dayText);
        dayView = (DataView) findViewById(R.id.dayView);
        btn_chart = findViewById(R.id.chart_btn);
        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyseActivity.this,ChartActivity.class);
                startActivity(intent);
            }
        });
        Calendar c = Calendar.getInstance();
        Calendar ct = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        c.set(year,month,day,0,0,0);
        int f = c.get(Calendar.DAY_OF_WEEK)-2;
        long startTime = c.getTimeInMillis();
        long startWeek = startTime-1000*60*60*24*f;

        int[] dayDatas = new int[7];
        int[] weekDatas = new int[3];
        int di=0,wi=0;
        for (int i = 0;i<pathDatas.size();i++)
        {
            di = 0;
            wi = 0;
            Paths p = pathDatas.get(i);
            long tt = p.getTimes();

            while (tt < startTime && di <7)
            {
                di++;
                startTime -= 1000*60*60*24;
            }
            if (di<7)
            {
                dayDatas[6-di]+=p.getNum();
            }

            while (tt < startWeek && wi <3)
            {
                wi++;
                startWeek -= 1000*60*60*24*7;
            }
            if (wi<3)
            {
                weekDatas[2-wi]+=p.getNum();
            }
        }

        dayView.SetDatas(dayDatas);
        weekView.SetDatas(weekDatas);

    }
}
