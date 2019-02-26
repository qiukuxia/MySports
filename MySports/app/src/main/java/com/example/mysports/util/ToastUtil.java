package com.example.mysports.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ToastUtil {
    public static Toast mtoast;
    public static void showMsg(Context context,String msg){
        if (mtoast == null) {
            mtoast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        else mtoast.setText(msg);
        mtoast.show();
    }
}
