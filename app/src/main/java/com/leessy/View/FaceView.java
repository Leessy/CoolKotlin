package com.leessy.View;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import com.leessy.coolkotlin.R;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Created by 刘承. on 2019/2/15
 * business@onfacemind.com
 */
public class FaceView extends View {
    private static final String TAG = "Liuc";
    private Paint mLinePaint;
    private Matrix mMatrix = new Matrix();
    private RectF mRect = null;
    float _width = 480;
    float _height = 640;

    boolean isMirror = false;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initPaint();
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        initTimingInvalidate();
//    }
//
//    Disposable disposable;
//
//    private void initTimingInvalidate() {
//        disposable = Observable.interval(300, 200, TimeUnit.MILLISECONDS)
//                .subscribe(aLong -> postInvalidate());
//    }


    /**
     * 可见光人脸框
     *
     * @param faceResult
     * @param w
     * @param h
     */
    public void setFaces(RectF faceResult, int w, int h) {
        this.mRect = faceResult;
        _width = w;
        _height = h;
        invalidate();
    }

    /**
     * 可见光人脸框  镜像
     *
     * @param faceResult
     * @param w
     * @param h
     */
    public void setFaces(RectF faceResult, int w, int h, boolean isMirror) {
        this.mRect = faceResult;
        this.isMirror = isMirror;
        _width = w;
        _height = h;
        invalidate();
    }


    /**
     * 清除人脸框
     */
    public void clearFaces() {
        mRect = null;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mRect == null) {
            return;
        }
        //调整屏幕宽高比
        float wws = getWidth() / _width;//宽比
        float hhs = getHeight() / _height;//高比
        mRect.left *= wws;
        mRect.right *= wws;
        mRect.top *= hhs;
        mRect.bottom *= hhs;

        //根据中心点转换 为正方形
        //画第一个原 3条弧形 外圆圈
        int centerX = (int) mRect.centerX();
        int centerY = (int) mRect.centerY();
        int reatr = (int) ((mRect.width() + mRect.height()) / 4);
        mRect.set(centerX - reatr, centerY - reatr, centerX + reatr, centerY + reatr);
        //开始绘制

//        Log.d("---- 人脸框1 =", mRect.toString());
        /////镜像
        if (isMirror){
            mMatrix.setScale(-1, 1);
            mMatrix.postTranslate(getWidth(), 0);
            mMatrix.mapRect(mRect);
            Log.d("---- 人脸框 mirror =", mRect.toString());

        }

        np.draw(canvas, mRect);

//        canvas.drawRect(mRect, mLinePaint);
//        canvas.restore();//防止影响下一次绘制  多人脸
        super.onDraw(canvas);
    }


    NinePatch np;//点9图

    /**
     * 画笔配置
     */
    private void initPaint() {
        //图片资源
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rect_icon);
        //获取点9块
        np = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
    }

}