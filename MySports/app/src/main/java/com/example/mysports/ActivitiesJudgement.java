package com.example.mysports;

        import java.util.LinkedList;
        import java.util.List;
        import android.app.Activity;
        import android.app.Application;
/**
 * 一个类 用来结束所有后台activity
 * @author Administrator
 *
 */
/**
 * Created by Administrator on 2018/4/21.
 */

public class ActivitiesJudgement extends Application {
    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static ActivitiesJudgement instance;
    //构造方法
    private ActivitiesJudgement(){}
    //实例化一次
    public synchronized static ActivitiesJudgement getInstance(){
        if (null == instance) {
            instance = new ActivitiesJudgement();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //杀进程
    public void onLowMemory() {
        super.onLowMemory();//一般来讲后台所有进程都被杀掉的时候,此函数会调用.
        System.gc();//garbage collector 垃圾收集,调用这个方法建议 JVM 进行垃圾回收
    }
}
