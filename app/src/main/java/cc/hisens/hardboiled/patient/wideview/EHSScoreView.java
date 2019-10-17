package cc.hisens.hardboiled.patient.wideview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import cc.hisens.hardboiled.patient.R;

public class EHSScoreView extends ScoreView {
    public EHSScoreView(Context context) {
        super(context);
    }

    public EHSScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EHSScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void drawUnit(Paint paint, Canvas canvas, float yScore, float x) {

    }

    @Override
    public void setScore(int score) {
        mScore = score;
        Resources res = getResources();
        switch (score) {
            case 1:
                mAnalysisResultText = res.getString(R.string.l_first_level_result);
                mTextColor = Color.parseColor("#ff807a");
                mRoundRectColor = Color.parseColor("#ffe6e4");
                break;
            case 2:
                mAnalysisResultText = res.getString(R.string.l_second_level_result);
                mTextColor = Color.parseColor("#ffcf5c");
                mRoundRectColor = Color.parseColor("#fef6dd");
                break;
            case 3:
                mAnalysisResultText = res.getString(R.string.l_third_level_result);
                mTextColor = Color.parseColor("#51cd30");
                mRoundRectColor = Color.parseColor("#dbf6d6");
                break;
            case 4:
                mAnalysisResultText = res.getString(R.string.l_fourth_level_result);
                mTextColor = Color.parseColor("#2ab5d7");
                mRoundRectColor = Color.parseColor("#d2f1f6");
                break;

        }

        postInvalidate();
    }

    @Override
    protected String getScoreText() {
        String result = "--";
        switch (mScore) {
            case 1:
                result = getResources().getString(R.string.l_first_level);
                break;
            case 2:
                result = getResources().getString(R.string.l_second_level);
                break;
            case 3:
                result = getResources().getString(R.string.l_third_level);
                break;
            case 4:
                result = getResources().getString(R.string.l_fourth_level);
                break;

        }

        return result;
    }
}
