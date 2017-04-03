package com.tyh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 圆形头像
 */
public class CircleImageView extends View {

    private Bitmap bitmap;

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制头像
        if (bitmap != null) {
            Log.i("TAGName", "onDraw......." + bitmap);
            canvas.drawBitmap(drawCircleImage(), 0, 0, null);
        }
    }

    //绘制圆形的头像
    private Bitmap drawCircleImage() {
        Log.i("TAGName", "drawCircleImage......." + bitmap);
        //获取组件的宽和高
        int w = getWidth();
        int h = getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap); //创建画布
        Paint paint = new Paint(); //创建画笔
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
        paint.setColor(Color.WHITE);//设置画笔颜色
        //绘制一个内切圆 --->desc
        int radius = Math.min(w, h) / 2;
        canvas.drawCircle(w / 2, h / 2, radius, paint);
        //绘制图片----->src
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Matrix matrix = new Matrix();//创建矩阵
        //对图片进行缩放
        matrix.postScale(1.0f * w / bitmap.getWidth(), 1.0f * h / bitmap.getHeight(), 0, 0);
        canvas.drawBitmap(bitmap, matrix, paint);
        return newBitmap;
    }

    //设置图片
    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    //设置图片
    public void setImageResources(int resId) {
        this.bitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }

    //测量宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (bitmap == null) {
            return;
        }

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();

        //获取宽和高的测量模式和测量值
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (wMode == MeasureSpec.AT_MOST) {
            w = bw;
        }

        if (hMode == MeasureSpec.AT_MOST) {
            h = bh;
        }

        //重新设置宽和高
        setMeasuredDimension(w, h);
    }

}
