package com.example.addicheung.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 自定义棋盘view
 * Created by AddiCheung on 2016/4/2.
 */
public class WuZiPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private static int MAX_LINE = 10;

    private float ratioOfLineHeight = 3*1.0f/4;

    private Paint mPaint = new Paint();

    private Bitmap mBlack,mWhite;

    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean mIsWhite = true;
    private boolean mIsGameOver;
    private boolean mIsWhiteWin;
    private boolean mIsBlackWin;

    public WuZiPanel(Context context) {
        this(context,null);
    }

    public WuZiPanel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WuZiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       // setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mBlack = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
        mWhite = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
    } 

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width  = Math.min(widthSize,heightSize);

        if(widthMode==MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if(heightMode==MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }

        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int scalWidth = (int) (mLineHeight*ratioOfLineHeight);

        mWhite = Bitmap.createScaledBitmap(mWhite,scalWidth,scalWidth,false);
        mBlack = Bitmap.createScaledBitmap(mBlack,scalWidth,scalWidth,false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkIsGameOver();
    }

    private void checkIsGameOver() {
        CheckUtil checkUtil= new CheckUtil();
        boolean whiteWin = checkUtil.checkFiveLine((ArrayList<Point>) mWhiteArray);
        boolean blackWin = checkUtil.checkFiveLine((ArrayList<Point>) mBlackArray);
        if(whiteWin||blackWin){
            mIsGameOver = true;
            mIsWhiteWin = whiteWin;
            String text = mIsWhiteWin?"白子赢了":"黑子赢了";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }else if(mWhiteArray.size()==50&&mBlackArray.size()==50){
            String text = "这一盘是和棋";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }

    }

    //绘制棋子
    private void drawPieces(Canvas canvas) {
        for(int i=0,n = mWhiteArray.size();i<n;i++){
            Point whitePoint  = mWhiteArray.get(i);
            canvas.drawBitmap(mWhite,
                    ((whitePoint.x+(1-ratioOfLineHeight)/2)*mLineHeight),
                    ((whitePoint.y+(1-ratioOfLineHeight)/2)*mLineHeight),null);

        }
        for(int i=0,n = mBlackArray.size();i<n;i++){
            Point blackPoint  = mBlackArray.get(i);
            canvas.drawBitmap(mBlack,
                    ((blackPoint.x+(1-ratioOfLineHeight)/2)*mLineHeight),
                    ((blackPoint.y+(1-ratioOfLineHeight)/2)*mLineHeight),null);

        }
    }

    //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for(int i =0;i<MAX_LINE;i++){
            int startX = (int) (lineHeight/2);
            int endX = (int) (w-lineHeight/2);
            int y = (int) ((0.5+i)*lineHeight);
            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsGameOver){
            return false;
        }
        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point point = getValidPoint(x,y);
            if(mWhiteArray.contains(point)||mBlackArray.contains(point)){
                return false;
            }
            if(mIsWhite){
                mWhiteArray.add(point);
            }else{
                mBlackArray.add(point);
            }
            invalidate();
            mIsWhite = !mIsWhite;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight), (int)(y / mLineHeight));
    }


    private static final String INSTANCE = "instance";
    private static final String WHITE_ARRAY = "white_array";
    private static final String BLACK_ARRAY  = "balck_array";
    private static final String IS_GAMEOVER = "is_game_over";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(IS_GAMEOVER,mIsGameOver);
        bundle.putParcelableArrayList(WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    /**
     * 若发现代码逻辑没有问题，而旋转屏幕是依然不能恢复布局
     * 请检查自定义view再响应Layout是否添加了ID
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof  Bundle){
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(IS_GAMEOVER);
            mWhiteArray = bundle.getParcelableArrayList(WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(BLACK_ARRAY);
            //注意这里
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * 再来一局方法
     */
    public void reStart(){
        mIsGameOver = false;
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsWhiteWin =false;
        invalidate();
    }
}
