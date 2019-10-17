package cc.hisens.hardboiled.patient.wideview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.view.component
 * @fileName ScoreView
 * @date on 2017/6/3 16:10
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class ScoreView extends View {

    private Paint mPaint;
    private String mScoreText = getResources().getString(R.string.score);
    
    protected int mRoundRectColor = Color.parseColor("#35d5db");
    protected int mTextColor = Color.parseColor("#35d5db");
    protected String mAnalysisResultText = getResources().getString(R.string.no_assessment);
    protected int mScore;


    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    protected void initPaint() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRectAndScore(canvas, mPaint);
        drawAnalysisResult(canvas, mPaint);
    }

    private void drawRectAndScore(Canvas canvas, Paint paint) {
        float strokeWidth = ScreenUtils.dp2px(getContext(), 15);
        paint.setColor(mRoundRectColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        int width = Math.min(getWidth(), getHeight());

        RectF rectF = new RectF();
        rectF.left = strokeWidth / 2;
        rectF.top = strokeWidth / 2;
        rectF.right = rectF.left + width - strokeWidth;
        rectF.bottom = rectF.top + width - strokeWidth;
        float radius = ScreenUtils.dp2px(getContext(), 38);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 绘制分数
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(ScreenUtils.dp2px(getContext(), 48));
        paint.setStrokeWidth(1);

        Paint.FontMetrics metrics = paint.getFontMetrics();
        float yScoreHeight = Math.abs(metrics.descent - metrics.ascent);
        float x = (rectF.left + rectF.right) / 2;
        float yScore = strokeWidth + ScreenUtils.dp2px(getContext(), 20) + yScoreHeight;
        // yScore - metrics.bottom调整基线
        String scoreText = getScoreText();
        canvas.drawText(scoreText, x, yScore - metrics.bottom, paint);

        drawUnit(paint, canvas, yScore, x);
    }

    private void drawAnalysisResult(Canvas canvas, Paint paint) {
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(ScreenUtils.dp2px(getContext(), 17));
        paint.setStrokeWidth(1);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float yOffset = metrics.ascent + metrics.descent;
        float x = getWidth() / 2.0f;
        float y = getHeight() - (float) (Math.ceil(metrics.ascent - metrics.descent) / 2);
        canvas.drawText(mAnalysisResultText, x, y + yOffset, paint);
    }

    public void setScore(int score) {
        mScore = score;
        Resources res = getResources();
        if (score >= 22) {
            mAnalysisResultText = res.getString(R.string.normal);
            mTextColor = Color.parseColor("#2ab5d7");
            mRoundRectColor = Color.parseColor("#d2f1f6");
        } else if (score >= 12) {
            mAnalysisResultText = res.getString(R.string.slight_exception);
            mTextColor = Color.parseColor("#51cd30");
            mRoundRectColor = Color.parseColor("#dbf6d6");
        } else if (score >= 8) {
            mAnalysisResultText = res.getString(R.string.medium_exception);
            mTextColor = Color.parseColor("#ffcf5c");
            mRoundRectColor = Color.parseColor("#fef6dd");
        } else if (score >= 0) {
            mAnalysisResultText = res.getString(R.string.serious_exception);
            mTextColor = Color.parseColor("#ff807a");
            mRoundRectColor = Color.parseColor("#ffe6e4");
        } else {
            mAnalysisResultText = res.getString(R.string.no_assessment);
            mTextColor = Color.parseColor("#2ab5d7");
            mRoundRectColor = Color.parseColor("#d2f1f6");
        }
        postInvalidate();
    }

    protected String getScoreText() {
        return mScore >= 0 ? String.valueOf(mScore) : "--";
    }

    protected void drawUnit(Paint paint, Canvas canvas, float yScore, float x) {
        // 绘制“分”字
        paint.setTextSize(ScreenUtils.dp2px(getContext(), 17));
        float yScoreText = yScore + ScreenUtils.dp2px(getContext(), 10);
        canvas.drawText(String.valueOf(mScoreText), x, yScoreText, paint);
    }
}
