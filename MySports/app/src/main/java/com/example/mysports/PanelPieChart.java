package com.example.mysports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
@SuppressLint("NewApi")

public class PanelPieChart extends View {

    private int ScrHeight;
    private int ScrWidth;

    private Paint[] arrPaintArc;
    private Paint PaintText = null;
    private Paint PaintText1;
    /*
    final int[] colors = new int[]{
            R.color.red,
            R.color.white,
            R.color.green,
            R.color.yellow,
            R.color.blue,
        };*/

    //颜色数组规定各个扇形块颜色
    private final int arrColorRgb[][] = {  {253, 180, 90},
            {39, 51, 72},
            {255, 135, 195},
            {52, 194, 188},
            {215, 124, 124},
            {148, 159, 181},
            {39, 51, 72},
            {255, 135, 195},
            {215, 124, 124},
            {180, 205, 230}} ;


    public float arrPer[];
    public String score[];
    public String title ="";

    public PanelPieChart(Context context,AttributeSet paramAttributeSet){
        super(context,paramAttributeSet);


        this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;
        BlurMaskFilter PaintBGBlur = new BlurMaskFilter(
                1, BlurMaskFilter.Blur.INNER);

        arrPaintArc = new Paint[5];
        //Resources res = this.getResources();
        for(int i=0;i<5;i++)
        {
            arrPaintArc[i] = new Paint();
            //arrPaintArc[i].setColor(res.getColor(colors[i] ));
            arrPaintArc[i].setARGB(255, arrColorRgb[i][0], arrColorRgb[i][1], arrColorRgb[i][2]);
            arrPaintArc[i].setStyle(Paint.Style.FILL);
            arrPaintArc[i].setStrokeWidth(4);
            arrPaintArc[i].setMaskFilter(PaintBGBlur);
        }

        PaintText = new Paint();
        PaintText.setColor(Color.BLACK);
        PaintText.setTextSize(50);
        PaintText.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void onDraw(Canvas canvas){
        canvas.drawColor(Color.WHITE);//背景设置成白色

        float cirX = ScrWidth / 2;
        float cirY = ScrHeight / 2 ;
        float radius = ScrHeight / 5 ;
        float arcLeft = cirX - radius;
        float arcTop  = cirY - radius ;
        float arcRight = cirX + radius ;
        float arcBottom = cirY + radius ;
        RectF arcRF0 = new RectF(arcLeft ,arcTop,arcRight,arcBottom);

        Path pathArc=new Path();
        pathArc.addCircle(cirX,cirY,radius,Direction.CW);
        canvas.drawPath(pathArc,arrPaintArc[0]);

        float CurrPer = 0f; //偏移角度
        float Percentage =  0f; //当前所占比例

        int scrOffsetW = ScrWidth - 400;
        int scrOffsetH = ScrHeight - 300;
        int scrOffsetT = 40;

        //Resources res = this.getResources();
        int i= 0;
        for(i=0; i<3; i++) //通过循环绘制各个部分
        {
            //计算角度
            Percentage = 360 * (arrPer[i]/ 100);
            Percentage = (float)(Math.round(Percentage *100))/100;

            //计算所占比例
            canvas.drawArc(arcRF0, CurrPer, Percentage, true, arrPaintArc[i+2]);

            //读取颜色
            canvas.drawRect(scrOffsetW ,scrOffsetH + i * scrOffsetT,
                    scrOffsetW + 90 ,scrOffsetH - 30 + i * scrOffsetT, arrPaintArc[i+2]);
            //获取比例
            canvas.drawText(score[i] +String.valueOf(arrPer[i]) +"%",
                    scrOffsetW + 120,scrOffsetH + i * scrOffsetT, PaintText);
            //获取下次的起始角度
            CurrPer += Percentage;
        }

        //绘制图注
        canvas.drawRect(scrOffsetW ,scrOffsetH + i * scrOffsetT,
                scrOffsetW + 90 ,scrOffsetH - 30 + i * scrOffsetT, arrPaintArc[0]);

        canvas.drawText(score[i] + String.valueOf(arrPer[i]) +"%",
                scrOffsetW + 120,scrOffsetH + i * scrOffsetT, PaintText);
        PaintText1 = new Paint();
        PaintText1.setColor(Color.BLACK);
        PaintText1.setTextSize(100);
        PaintText1.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(title,60,200,PaintText1);

    }
}
