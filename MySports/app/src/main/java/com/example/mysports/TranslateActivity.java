package com.example.mysports;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;


/**
 * @author lukun
 */
public class TranslateActivity extends Activity {

    // 查询列表
    private ListView translateList;

    private TranslateAdapter adapter;

    private List<TranslateData> list = new ArrayList<TranslateData>();
    private List<Translate> trslist = new ArrayList<Translate>();


    private ProgressDialog progressDialog = null;

    private Handler waitHandler = new Handler();

    private EditText fanyiInputText;

    private InputMethodManager imm;

    private TextView fanyiBtn;

    TextView languageSelectFrom;

    TextView languageSelectTo;

    private Translator translator;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(Activity.RESULT_OK);
        try {
            getWindow().requestFeature(Window.FEATURE_PROGRESS);
            getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                    Window.PROGRESS_VISIBILITY_ON);
        } catch (Exception e) {
        }
        setContentView(R.layout.activity_translate);

        fanyiInputText = (EditText) findViewById(R.id.fanyiInputText);

        fanyiBtn = (TextView) findViewById(R.id.fanyiBtn);

        translateList = (ListView) findViewById(R.id.commentList);

        imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);

        View view = this.getLayoutInflater().inflate(R.layout.translate_head,
                translateList, false);
        translateList.addHeaderView(view);
        adapter = new TranslateAdapter(this, list, trslist);

        translateList.setAdapter(adapter);

        fanyiBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                query();
            }
        });

        languageSelectFrom = (TextView) findViewById(R.id.languageSelectFrom);
        languageSelectFrom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectLanguage();
            }
        });
        languageSelectTo = (TextView) findViewById(R.id.languageSelectTo);
        languageSelectTo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectLanguage();
            }
        });
        query();
    }

    private void selectLanguage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(TranslateActivity.this);
        String[] strs = {"中文->英文","英文->中文"};

        builder.setSingleChoiceItems(
                strs,
                0,
                new DialogInterface.OnClickListener()                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            languageSelectFrom.setText("中文");
                            languageSelectTo.setText("英文");
                        }
                        if (which == 1)
                        {
                            languageSelectTo.setText("中文");
                            languageSelectFrom.setText("英文");
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void query() {
        showLoadingView("正在查询");

        // 源语言或者目标语言其中之一必须为中文,目前只支持中文与其他几个语种的互译
        String from = languageSelectFrom.getText().toString();
        String to = languageSelectTo.getText().toString();
        final String input = fanyiInputText.getText().toString();

        Language langFrom = LanguageUtils.getLangByName(from);
        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        // Language langFrosm = LanguageUtils.getLangByName("自动");

        Language langTo = LanguageUtils.getLangByName(to);

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("MySports").from(langFrom).to(langTo).timeout(3000).build();// appkey可以省
        final long start = System.currentTimeMillis();

        translator = Translator.getInstance(tps);
        //查询，返回两种情况，一种是成功，相关结果存储在result参数中，另外一种是失败，失败信息放在TranslateErrorCode中，TranslateErrorCode是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。
        translator.lookup(input, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TranslateData td = new TranslateData(
                                System.currentTimeMillis(), result);

                        long end = System.currentTimeMillis();
                        long time = end-start;
                        Log.i("1111111111111111","111111111查词时间"+time);

                        list.add(td);
                        trslist.add(result);
                        adapter.notifyDataSetChanged();
                        translateList.setSelection(list.size() - 1);
                        dismissLoadingView();
                        imm.hideSoftInputFromWindow(
                                fanyiInputText.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtils.show("查询错误:" + error.name());
                        Log.e("查询错误",error.getCode()+"");
                        dismissLoadingView();
                    }
                });
            }
            @Override
            public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

            }
        });
    }

    private void showLoadingView(final String text) {
        waitHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.setMessage(text);
                    progressDialog.show();
                }
            }
        });

    }

    private void dismissLoadingView() {
        waitHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }

    public void postQuery(final Translate bean) {
        showLoadingView("正在翻译，请稍等");
    }

    public void loginBack(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void finish() {
        super.finish();
    }

}
