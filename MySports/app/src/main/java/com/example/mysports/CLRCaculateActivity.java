package com.example.mysports;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mysports.util.ToastUtil;

public class CLRCaculateActivity extends AppCompatActivity {
private Button CLR_btn;
private EditText editTextin[] = new EditText[10];
    private EditText editTextout[] = new EditText[9];

    private float sum,sum1,sum2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clrcaculate);
       CLR_btn = findViewById(R.id.CLR_btn);
        editTextin[0]=findViewById(R.id.CLR_edit0);
        editTextin[1]=findViewById(R.id.CLR_edit1);
        editTextin[2]=findViewById(R.id.CLR_edit2);
        editTextin[3]=findViewById(R.id.CLR_edit3);
        editTextin[4]=findViewById(R.id.CLR_edit4);
        editTextin[5]=findViewById(R.id.CLR_edit5);
        editTextin[6]=findViewById(R.id.CLR_edit6);
        editTextin[7]=findViewById(R.id.CLR_edit7);
        editTextin[8]=findViewById(R.id.CLR_edit8);
        editTextin[9]=findViewById(R.id.CLR_edit9);
        editTextout[0]=findViewById(R.id.CLRout_edit0);
        editTextout[1]=findViewById(R.id.CLRout_edit1);
        editTextout[2]=findViewById(R.id.CLRout_edit2);
        editTextout[3]=findViewById(R.id.CLRout_edit3);
        editTextout[4]=findViewById(R.id.CLRout_edit4);
        editTextout[5]=findViewById(R.id.CLRout_edit5);
        editTextout[6]=findViewById(R.id.CLRout_edit6);
        editTextout[7]=findViewById(R.id.CLRout_edit7);
        editTextout[8]=findViewById(R.id.CLRout_edit8);

        CLR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sum1 = Float.parseFloat(editTextin[0].getText().toString())*200+Float.parseFloat(editTextin[1].getText().toString())*150+Float.parseFloat(editTextin[2].getText().toString())*200+Float.parseFloat(editTextin[3].getText().toString())*100+Float.parseFloat(editTextin[4].getText().toString())*70+Float.parseFloat(editTextin[5].getText().toString())*80+Float.parseFloat(editTextin[6].getText().toString())*120+Float.parseFloat(editTextin[7].getText().toString())*10+Float.parseFloat(editTextin[8].getText().toString())*50+Float.parseFloat(editTextin[9].getText().toString())*30;
               sum2 = Float.parseFloat(editTextout[0].getText().toString())*255+Float.parseFloat(editTextout[1].getText().toString())*600+Float.parseFloat(editTextout[2].getText().toString())*800+Float.parseFloat(editTextout[3].getText().toString())*500+Float.parseFloat(editTextout[4].getText().toString())*700+Float.parseFloat(editTextout[5].getText().toString())*500+Float.parseFloat(editTextout[6].getText().toString())*400+Float.parseFloat(editTextout[7].getText().toString())*500+Float.parseFloat(editTextout[8].getText().toString())*700;
               sum = sum1-sum2;
               AlertDialog.Builder b = new AlertDialog.Builder(CLRCaculateActivity.this).setTitle("计算结果");
               b.setMessage("热量摄入为："+sum+"大卡") ;
                b.show();
            }
        });
    }

}
