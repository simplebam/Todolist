package com.yueyue.todolist.widget.lock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yueyue.todolist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yueyue on 2018/3/21 23:27
 * desc   :
 */

public class LockPatternView extends View {
    //判断线的状态
    private static boolean isLineState = true;
    //判断点是否被实例化了
    private  boolean isInitPoint = false;
    //判断手指是否离开屏幕
    private static boolean isFinish = false;
    //判断手指点击屏幕时是否选中了九宫格中的点
    private static boolean isSelect = false;
    // 创建MyPoint的数组
    private Point[][] mPoints = new Point[3][3];
    // 声明屏幕的宽和高
    private int mScreenHeight;
    private int mScreenWidth;
    // 声明点线的图片的半径
    private float mPointRadius;
    // 声明线的图片的高（即是半径）
    private float mLineHeight;
    // 声明鼠标移动的x，y坐标
    private float mMoveX, mMoveY;
    // 声明屏幕上的宽和高的偏移量
    private int mScreenHeightOffSet = 0;
    private int mScreenWidthOffSet = 0;
    // 创建一个画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 声明资源图片
    private Bitmap mBitmapNormal;
    private Bitmap mBitmapPressed;
    private Bitmap mBitmapError;
    private Bitmap mLinePressed;
    private Bitmap mLineError;
    // 创建一个矩阵
    private Matrix mMatrix = new Matrix();
    // 创建MyPoint的列表
    private List<Point> mPointList = new ArrayList<Point>();
    // 实例化鼠标点
    private Point mMousePoint = new Point();
    // 用获取从activity中传过来的密码字符串
    private String mPassword = "";

    private Context mContext;
    private OnLockListener mListener;

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context) {
        super(context);
    }

    /**
     * 画点和画线
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInitPoint) {
            initPoint(); // 先初始化
        }

        canvasPoint(canvas); // 开始画点

        // 开始画线
        if (mPointList.size() > 0) {
            Point b = null;
            Point a = mPointList.get(0);
            for (int i = 1; i < mPointList.size(); i++) {
                b = mPointList.get(i);
                canvasLine(a, b, canvas);
                a = b;
            }
            if (!isFinish) {
                canvasLine(a, mMousePoint, canvas);
            }
        }
    }

    /**
     * 手指点击手机屏幕
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMoveX = event.getX();
        mMoveY = event.getY();
        // 设置移动点的坐标
        mMousePoint.setX(mMoveX);
        mMousePoint.setY(mMoveY);
        Point mPoint = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isLineState = true;
                isFinish = false;
                // 每次点击时就会将pointList中元素设置转化成正常状态
                for (int i = 0; i < mPointList.size(); i++) {
                    mPointList.get(i).setState(Point.BITMAP_NORMAL);
                }
                // 将pointList中的元素清除掉
                mPointList.clear();
                // 判断是否点中了九宫格中的点
                mPoint = getIsSelectedPoint(mMoveX, mMoveY);
                if (mPoint != null) {
                    isSelect = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSelect == true) {
                    mPoint = getIsSelectedPoint(mMoveX, mMoveY);
                }

                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;
                // 规定至少要有4个点被连线才有可能是正确
                // 其他种情况都是错误的
                if (mPointList.size() >= 4) {// 正确情况
                    for (int j = 0; j < mPointList.size(); j++) {
                        mPassword += mPointList.get(j).getIndex();
                    }
                    //将连线后得到的密码传给activity
                    mListener.getStringPassword(mPassword);
                    mPassword = "";
                    //经过activity判断传过来是否正确
                    if (mListener.isPassword()) {
                        for (int i = 0; i < mPointList.size(); i++) {
                            mPointList.get(i).setState(Point.BITMAP_PRESS);
                        }
                    } else {
                        for (int i = 0; i < mPointList.size(); i++) {
                            mPointList.get(i).setState(Point.BITMAP_ERROR);
                        }
                        isLineState = false;
                    }
                    // 错误情况
                } else if (mPointList.size() < 4 && mPointList.size() > 1) {
                    for (int i = 0; i < mPointList.size(); i++) {
                        mPointList.get(i).setState(Point.BITMAP_ERROR);
                    }
                    isLineState = false;
                    // 如果只有一个点被点中时为正常情况
                } else if (mPointList.size() == 1) {
                    for (int i = 0; i < mPointList.size(); i++) {
                        mPointList.get(i).setState(Point.BITMAP_NORMAL);
                    }
                }
                break;

        }
        // 将mPoint添加到pointList中
        if (isSelect && mPoint != null) {
            if (mPoint.getState() == Point.BITMAP_NORMAL) {
                mPoint.setState(Point.BITMAP_PRESS);
                mPointList.add(mPoint);
            }
        }
        // 每次发生OnTouchEvent()后都刷新View
        postInvalidate();
        return true;
    }

    /**
     * 判断九宫格中的某个点是否被点中了，或者某个点能否被连线
     *
     * @param moveX
     * @param moveY
     * @return
     */
    private Point getIsSelectedPoint(float moveX, float moveY) {
        Point myPoint = null;
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                if (mPoints[i][j].isWith(mPoints[i][j], moveX, moveY,
                        mPointRadius)) {
                    myPoint = mPoints[i][j];
                }
            }
        }

        return myPoint;
    }

    /**
     * 画线
     *
     * @param a      起始点
     * @param b      目的点
     * @param canvas 画布
     */
    private void canvasLine(Point a, Point b, Canvas canvas) {
        // Math.sqrt(平方+平方)
        float abInstance = (float) Math.sqrt(
                (a.getX() - b.getX()) * (a.getX() - b.getX())
                        + (a.getY() - b.getY()) * (a.getY() - b.getY())
        );
        canvas.rotate(RotateDegrees.getDegrees(a, b), a.getX(), a.getY());

        mMatrix.setScale(abInstance / mLineHeight, 1);
        mMatrix.postTranslate(a.getX(), a.getY());
        if (isLineState) {
            canvas.drawBitmap(mLinePressed, mMatrix, mPaint);
        } else {
            canvas.drawBitmap(mLineError, mMatrix, mPaint);
        }

        canvas.rotate(-RotateDegrees.getDegrees(a, b), a.getX(), a.getY());
    }

    /**
     * 画点
     *
     * @param canvas
     */
    private void canvasPoint(Canvas canvas) {
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                if (mPoints[i][j].getState() == Point.BITMAP_NORMAL) {
                    canvas.drawBitmap(mBitmapNormal,
                            mPoints[i][j].getX() - mPointRadius,
                            mPoints[i][j].getY() - mPointRadius, mPaint);
                } else if (mPoints[i][j].getState() == Point.BITMAP_PRESS) {
                    canvas.drawBitmap(mBitmapPressed,
                            mPoints[i][j].getX() - mPointRadius,
                            mPoints[i][j].getY() - mPointRadius, mPaint);
                } else {
                    canvas.drawBitmap(mBitmapError,
                            mPoints[i][j].getX() - mPointRadius,
                            mPoints[i][j].getY() - mPointRadius, mPaint);
                }
            }
        }
    }

    /**
     * 实例化九宫格中所有点和所有的资源图片
     */
    private void initPoint() {
        // 获取View的宽高
        mScreenWidth = getWidth();
        mScreenHeight = getHeight();
        if (mScreenHeight > mScreenWidth) {
            // 获取y轴上的偏移量
            mScreenHeightOffSet = (mScreenHeight - mScreenWidth) / 2;
            // 将屏幕高的变量设置成与宽相等，目的是为了new Point(x,y)时方便操作
            mScreenHeight = mScreenWidth;
        } else {
            // 获取x轴上的偏移量
            mScreenWidthOffSet = (mScreenWidth - mScreenHeight) / 2;
            // 将屏幕宽的变量设置成与高相等，目的是为了new Point(x,y)时方便操作
            mScreenWidth = mScreenHeight;
        }

        /**
         * 实例化所有的资源图片
         */
        mBitmapError = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_error);
        mBitmapNormal = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_normal);
        mBitmapPressed = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_pressed);
        mLineError = BitmapFactory.decodeResource(getResources(), R.drawable.line_error);
        mLinePressed = BitmapFactory.decodeResource(getResources(), R.drawable.line_pressed);

        mPointRadius = mBitmapNormal.getWidth() / 2;
        mLineHeight = mLinePressed.getHeight();

        /**
         * 开始实例化九宫格中点
         */
        mPoints[0][0] = new Point(mScreenWidthOffSet + mScreenWidth / 4,
                mScreenHeightOffSet + mScreenHeight / 4);
        mPoints[0][1] = new Point(mScreenWidthOffSet + mScreenWidth / 2,
                mScreenHeightOffSet + mScreenHeight / 4);
        mPoints[0][2] = new Point(mScreenWidthOffSet + mScreenWidth * 3 / 4,
                mScreenHeightOffSet + mScreenHeight / 4);

        mPoints[1][0] = new Point(mScreenWidthOffSet + mScreenWidth / 4,
                mScreenHeightOffSet + mScreenHeight / 2);
        mPoints[1][1] = new Point(mScreenWidthOffSet + mScreenWidth / 2,
                mScreenHeightOffSet + mScreenHeight / 2);
        mPoints[1][2] = new Point(mScreenWidthOffSet + mScreenWidth * 3 / 4,
                mScreenHeightOffSet + mScreenHeight / 2);

        mPoints[2][0] = new Point(mScreenWidthOffSet + mScreenWidth / 4,
                mScreenHeightOffSet + mScreenHeight * 3 / 4);
        mPoints[2][1] = new Point(mScreenWidthOffSet + mScreenWidth / 2,
                mScreenHeightOffSet + mScreenHeight * 3 / 4);
        mPoints[2][2] = new Point(mScreenWidthOffSet + mScreenWidth * 3 / 4,
                mScreenHeightOffSet + mScreenHeight * 3 / 4);


        // 设置九宫格中的各个index
        int index = 1;
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                mPoints[i][j].setIndex(index + "");
                // 在没有任何操作的情况下默認点的状态
                mPoints[i][j].setState(Point.BITMAP_NORMAL);
                index++;
            }
        }

        // 将isInitPoint设置为true
        isInitPoint = true;
    }

    public interface OnLockListener {
        public void getStringPassword(String password);

        public boolean isPassword();
    }


    public void setLockListener(OnLockListener listener) {
        this.mListener = listener;
    }

}
