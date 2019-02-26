package com.example.mysports;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mysports.database.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText1;
    private EditText editText2;
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                submit();
                break;
            case R.id.button2:
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        String editText1String = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(editText1String)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String editText2String = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(editText2String)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        RegisterUser(editText1String, editText2String);

    }

    private void RegisterUser(String name, String pass) {
        List<Users> tu = Users.find(Users.class,"name = ?", name);
        if (tu.size()>0)
        {
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
//            return;
        }
        else
        {
            Users u = new Users();
            u.setName(name);
            u.setPassword(pass);
            u.setRemark("");

            u.save();
            finish();
        }

    }
}
