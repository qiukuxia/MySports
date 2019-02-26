package com.example.mysports;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Roc on 2018/4/20.
 */

public class DataView extends View {

    private int[] disDatas;
    private int sw, sh;
    private int total;
    private int[] maxMin;
    private Paint pPoint, pLine, pBox, pText;
    private int nullWidth = 90;

    private String[][] Strs = {{"前两周","上周","本周"},{"6天前","5天前","4天前","3天前","前天","昨天","今天"}};

    private Point[] mPoints;

    public DataView(Context context) {
        super(context);
    }

    public DataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {//根据点的数量将Canvas等分，等分后先绘制图表的所有横轴和纵轴。再将数据转化为点坐标，绘制到屏幕上，最后将相邻两点连成线即可。

        if (sw == 0)
        {
            sw = getWidth();
            sh = getHeight();

            pPoint = new Paint();//paint相当于画笔
            pPoint.setColor(0xffff0000);
            pPoint.setStyle(Paint.Style.FILL);
            pLine = new Paint();
            pLine.setColor(0xaa0000ff);
            pLine.setStrokeWidth(6);
            pLine.setStyle(Paint.Style.STROKE);
            pBox = new Paint();
            pBox.setColor(0xff00ff33);
            pBox.setStrokeWidth(3);
            pBox.setStyle(Paint.Style.STROKE);
            pText = new Paint();
            pText.setColor(0xff333333);
            pText.setTextSize(30);
        }


//        for(int i=1;i<disDatas.length;i++)
//        {
//
//
//                canvas.drawLine(nullWidth+(sw-nullWidth*2)*i/(disDatas.length-1),
//                        sh-nullWidth-(disDatas[i]-disDatas[maxMin[1]])*1.0f*(sh-nullWidth*2)/(disDatas[maxMin[0]]-disDatas[maxMin[1]]),
//                        nullWidth+(sw-nullWidth*2)*(i-1)/(disDatas.length-1),
//                        sh-nullWidth-(disDatas[i-1]-disDatas[maxMin[1]])*1.0f*(sh-nullWidth*2)/(disDatas[maxMin[0]]-disDatas[maxMin[1]]),
//                        pLine);
//
//        }


        Rect r = new Rect(nullWidth,nullWidth,sw-nullWidth,sh-nullWidth);
        canvas.drawRect(r,pBox);//绘图，前四个参数为绘制坐标，最后参数为绘制地点


        if (disDatas != null)
        {
            int maxData = disDatas[maxMin[0]];
            if (maxData == 0)
            {
                maxData = 1000;
            }
            mPoints = new Point[disDatas.length];
            for(int i=0;i<disDatas.length;i++)
            {

                canvas.drawCircle(nullWidth+(sw-nullWidth*2)*i/(disDatas.length-1),
                        sh-nullWidth-(disDatas[i]-disDatas[maxMin[1]])*1.0f*(sh-nullWidth*2)/(maxData-disDatas[maxMin[1]]),
                        15,pPoint);
                mPoints[i] = new Point();
                mPoints[i].x = nullWidth+(sw-nullWidth*2)*i/(disDatas.length-1);
                mPoints[i].y = (int) (sh-nullWidth-(disDatas[i]-disDatas[maxMin[1]])*1.0f*(sh-nullWidth*2)/(maxData-disDatas[maxMin[1]]));

                canvas.drawText(Strs[total][i],mPoints[i].x-15,sh-nullWidth+50,pText);
                canvas.drawText(disDatas[i]+"m",mPoints[i].x-60,mPoints[i].y-20,pText);
            }

            try {
                drawScrollLine(canvas);

            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
        }


    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp,endp;
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);//用来实现三次贝塞尔曲线(有两个控制点),(x1,y1) 为控制点，(x2,y2)为控制点，(x3,y3) 为结束点
            canvas.drawPath(path, pLine);
        }
    }

    public void SetDatas(int[] ds)
    {
        disDatas = ds;
        if (disDatas.length == 3)
        {
            total = 0;
            maxMin = GetMaxMin();
        }
        else if (disDatas.length == 7)
        {
            total=1;
            maxMin = GetMaxMin();
        }

        invalidate();
    }

    private int[] GetMaxMin()
    {
        int[] res = new int[2];
        int max = disDatas[0];
        int min = disDatas[0];
        int imax = 0;
        int imin = 1;

        for (int i=1;i<disDatas.length;i++)
        {
            if (max<disDatas[i])
            {
                max = disDatas[i];
                imax = i;

            }
            if (min>disDatas[i])
            {
                min = disDatas[i];
                imin = i;
            }
        }

        res[0] = imax;
        res[1] = imin;
        return res;
    }
}
