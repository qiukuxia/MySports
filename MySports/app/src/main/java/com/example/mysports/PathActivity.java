package com.example.mysports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mysports.database.Paths;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PathActivity extends AppCompatActivity {

    private App myApp;
    private TextView textViewTitle;
    private ListView pathList;

    private List<Paths> pathDatas = new ArrayList<>();
    private PathAdapter pathAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        myApp = (App) getApplication();

        pathDatas = Paths.find(Paths.class,"users = ?",new String[]{myApp.getUserName()},null,"times desc",null);
        pathAdapter = new PathAdapter();

        initView();

    }

    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        pathList = (ListView) findViewById(R.id.pathList);
        pathList.setAdapter(pathAdapter);

        pathList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PathActivity.this, MapActivity.class);
                intent.putExtra("path",pathDatas.get(position).getPaths());
                startActivity(intent);
            }
        });
    }

    public static String formatDateTime(long datelong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(datelong);
    }

    class PathAdapter extends BaseAdapter {
        private LayoutInflater mInflater = LayoutInflater.from(PathActivity.this);

        class ViewHolder {
            public TextView tvplace, tvFlag;

        }

        ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            /**说明：     */

            return pathDatas.size();
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

            int d = pathDatas.get(position).getNum();
            String disStr = "";
            if (d >= 0 && d < 1000) {
                String[] dis = (d + "").split("\\.");
                disStr = "距离：" + dis[0] + "m";
            } else if (d >= 1000) {
                d = d / 1000;
                String[] dis = (d + "").split("\\.");
                if (dis.length == 2) {
                    disStr = "距离：" + dis[0] + "." + dis[1].substring(0, 1) + "Km";
                } else {
                    disStr = "距离：" + d + "Km";
                }

            }

            int pt = pathDatas.get(position).getPasst();
            String sp;
            if (d>20) {
            sp = "速度："+(int) (d*3.6/pt)+"Km/h";}
            else sp = "速度："+ (d*3600/pt)+"Km/h";
            String ptstr = "时间："+pt/60+"m"+pt%60+"s";
            viewHolder.tvplace.setText(pathDatas.get(position).getRemark()+"\n"+disStr+"\n"+ptstr+"\n"+sp);
            viewHolder.tvFlag.setText(formatDateTime(pathDatas.get(position).getTimes()));

            return convertView;
        }

    }
}
