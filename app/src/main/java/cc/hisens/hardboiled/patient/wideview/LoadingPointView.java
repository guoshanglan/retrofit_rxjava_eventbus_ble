package cc.hisens.hardboiled.patient.wideview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;

public class LoadingPointView extends View {
    public static final int MESSAGE_ID = 0;
    //白色圆点
    private Paint mWhitePaint;
    //绿色圆点
    private Paint mGreenPaint;
    //半径
    private float mRadius;
    //下一个被选中的圆点的index
    private int mIndex;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ++mIndex;
            if (mIndex == 3) {
                mIndex = 0;
            }
            postInvalidate();
        }
    };

    public LoadingPointView(Context context) {
        this(context, null);
    }

    public LoadingPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParmas(context);
    }

    private void initParmas(Context context) {
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setColor(ContextCompat.getColor(context, R.color.rectangle_stroke_color));
        mGreenPaint = new Paint();
        mGreenPaint.setAntiAlias(true);
        mGreenPaint.setStyle(Paint.Style.FILL);
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.hisens_blue));

        mRadius =  ScreenUtils.dp2px(context, 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 3; i++) {
            //修改圆心x轴坐标，来画出多个圆点
            canvas.drawCircle(getHeight() / 2 + mRadius * i * 2 + 5 * i, getHeight() / 2, mRadius, mWhitePaint);
        }
        //动态修改绿色圆点的位置
        canvas.drawCircle(getHeight() / 2 + mRadius * mIndex * 2 + 3* mIndex, getHeight() / 2, mRadius, mGreenPaint);
        //发送消息不断绘制，以达到无限循环的效果
        mHandler.sendEmptyMessageDelayed(MESSAGE_ID, 500);
    }

    //停止动画
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(MESSAGE_ID);
        mHandler = null;
    }


}
