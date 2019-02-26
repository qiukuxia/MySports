package com.example.mysports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mysports.database.Users;
import com.example.mysports.util.ToastUtil;

import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText1;
    private EditText editText2;
    private Button button1;
    private Button button2;

    private App myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApp = (App)getApplication();

        initView();

    }

    private void initView() {
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button1 = (Button) findViewById(R.id.button1);
        button2 =  findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                submit();
//                UpFile();
                break;

            case R.id.button2:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void submit() {
        // validate
        String editText1String = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(editText1String)) {
            ToastUtil.showMsg(LoginActivity.this,"请输入用户名");
            return;
        }

        String editText2String = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(editText2String)) {
            ToastUtil.showMsg(LoginActivity.this,"密码");
            return;
        }

        // TODO validate success, do something

        login(editText1String, editText2String);
    }

    private void login(String name, String pass) {
        List<Users> tu = Users.find(Users.class,"name = ? AND password = ?", name, pass);
        if (tu.size()>0)
        {

            myApp.setUserName(name);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else
        {
            ToastUtil.showMsg(LoginActivity.this,"用户名或密码错误");
        }
    }

}
