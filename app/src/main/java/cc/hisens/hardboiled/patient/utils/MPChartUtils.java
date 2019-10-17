package cc.hisens.hardboiled.patient.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import cc.hisens.hardboiled.patient.R;

public class MPChartUtils {


    /*** 不显示无数据的提示 ** @param mChart*/
    public static void NotShowNoDataText(Chart mChart) {
        mChart.clear();
        mChart.notifyDataSetChanged();
        mChart.setNoDataText("您还没有数据");
        mChart.setNoDataTextColor(Color.parseColor("#666666"));
        mChart.invalidate();
    }


    /*** 初始化数据 ** @param chart* @param lineDatas*/
    public static void initData(Context context, CombinedChart chart, LineData... lineDatas) {
        CombinedData combinedData = new CombinedData();
        for (LineData lineData : lineDatas) {
            combinedData.setData(lineData);
        }
        chart.setData(combinedData);
        //   chart.setMarker(new MPChartMarkerView(context, R.layout.custom_marker_view));

        chart.invalidate();
    }







//设置监测图表控件
    public static void configChart(CombinedChart mChart, List<String> mLabels, float yMax, float yMin, boolean isShowLegend,boolean isShowLine) {
        mChart.setDrawGridBackground(false);
        mChart.setDrawBorders(false);
        mChart.setScaleEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setNoDataText("");
        // 不显示描述数据
        mChart.getDescription().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        Legend legend = mChart.getLegend();
        // 是否显示图例
        if (isShowLegend) {
            legend.setEnabled(true);
            legend.setTextColor(Color.WHITE);
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setYEntrySpace(20f);
            //图例的大小
            legend.setFormSize(7f);
            // 图例描述文字大小
            legend.setTextSize(10);
            legend.setXEntrySpace(20f);
        } else {
            legend.setEnabled(false);
        }

        XAxis xAxis = mChart.getXAxis();
        // 是否显示x轴线
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(0);
        xAxis.setLabelCount(mLabels.size());
        // 设置x轴线的颜色
        xAxis.setAxisLineColor(Color.parseColor("#4cffffff"));
        // 是否绘制x方向网格线
        xAxis.setDrawGridLines(false);
        //x方向网格线的颜色
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置x轴文字的大小
        xAxis.setTextSize(12);
        // 设置x轴数据偏移量
        xAxis.setYOffset(5);
        final List<String> labels = mLabels;
        // 显示x轴标签
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (index < 0 || index >= labels.size()) {
                    return "";
                }
                return labels.get(index);
            }
        };


        // 引用标签
        xAxis.setValueFormatter(formatter);
        xAxis.setAxisLineWidth(5f);

        xAxis.setAxisLineColor(Color.WHITE);
        // 设置x轴文字颜色
        xAxis.setTextColor(mChart.getResources().getColor(R.color.white));
        // 设置x轴每最小刻度interval
        xAxis.setGranularity(1f);




        YAxis yAxis = mChart.getAxisLeft();
//        //设置x轴的最大值
        yAxis.setAxisMaximum(yMax);


        // 设置y轴的最大值
        yAxis.setAxisMinimum(yMin);

        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });


        // 不显示y轴
        yAxis.setDrawAxisLine(false);

        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        // 从y轴发出横向直线
        yAxis.setDrawGridLines(false);
//        yAxis.setGridColor(Color.RED); //设置线的颜色
//        yAxis.enableGridDashedLine(10f, 5f, 0f); //设置直线为虚线
        // 是否显示y轴坐标线
        // 设置y轴的文字颜色
        yAxis.setTextColor(mChart.getResources().getColor(R.color.white));
        // 设置y轴文字的大小
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(-3);
        yAxis.setXOffset(15);
        yAxis.setLabelCount(5, true);
        yAxis.setGranularity(1);

        if (isShowLine) {
            //添加限制线
            LimitLine yLimitLine = new LimitLine(90f, "");
            yLimitLine.setLineColor(Color.parseColor("#999999"));
            yLimitLine.setTextColor(Color.RED);
            yLimitLine.enableDashedLine(10f, 8f, 0f);
            yAxis.addLimitLine(yLimitLine);

            LimitLine yLimitLine2 = new LimitLine(140f, "");
            yLimitLine2.setLineColor(Color.parseColor("#999999"));
            yLimitLine2.setTextColor(Color.RED);
            yLimitLine2.enableDashedLine(10f, 8f, 0f);
            yAxis.addLimitLine(yLimitLine2);
            LimitLine yLimitLine3 = new LimitLine(190f, "");
            yLimitLine3.setLineColor(Color.parseColor("#999999"));
            yLimitLine3.setTextColor(Color.RED);
            yLimitLine3.enableDashedLine(10f, 8f, 0f);
            yAxis.addLimitLine(yLimitLine3);

            LimitLine yLimitLine4 = new LimitLine(550f, "");
            yLimitLine4.setLineColor(Color.parseColor("#999999"));
            yLimitLine4.setTextColor(Color.RED);
            yLimitLine4.enableDashedLine(10f, 8f, 0f);
            yAxis.addLimitLine(yLimitLine4);

        }
        Matrix matrix = new Matrix();
        // 根据数据量来确定 x轴缩放大倍
        if (mLabels.size() <= 10) {
            matrix.postScale(1.0f, 1.0f);
        } else if (mLabels.size() <= 15) {
            matrix.postScale(1.5f, 1.0f);
        } else if (mLabels.size() <= 20) {
            matrix.postScale(2.0f, 1.0f);
        } else {
            matrix.postScale(1f, 1.0f);
        }


// 在图表动画显示之前进行缩放
        mChart.getViewPortHandler().refresh(matrix, mChart, false);
        // x轴执行动画
       // mChart.animateX(1500);

    }


    /*** 获取LineDataSet** @param entries* @param label* @param textColor* @param lineColor* @return*/
    public static LineDataSet getLineData(List<Entry> entries, String label, @ColorInt int textColor, Drawable drawable, boolean isFill) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        // 设置曲线的颜色
        dataSet.setColor(textColor);
        //数值文字颜色
        dataSet.setValueTextColor(textColor);
        // 模式为贝塞尔曲线
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 是否绘制数据值
        dataSet.setDrawValues(false);
        // 是否绘制圆点
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(true); //设置圆点为空心
        // 这里有一个坑，当我们想隐藏掉高亮线的时候，MarkerView 跟着不见了 // 因此只有将它设置成透明色
        dataSet.setHighlightEnabled(true);// 隐藏点击时候的高亮线
        // 设置高亮线为透明色
        dataSet.setHighLightColor(Color.TRANSPARENT);
        if (isFill) {
            //是否设置填充曲线到x轴之间的区域
            dataSet.setDrawFilled(true);
//            // 填充颜色
          //  dataSet.setFillColor(lineColor);

            //填充渐变色
             dataSet.setFillDrawable(drawable);
        }
        //设置圆点的颜色
      //  dataSet.setCircleColor(lineColor);
        // 设置圆点半径
        dataSet.setCircleRadius(3.5f);
        // 设置线的宽度
        dataSet.setLineWidth(2f);
        return dataSet;
    }

    /*** 获取barDataSet* @param entries* @param label* @param textColor* @param lineColor* @return*/
    public static BarDataSet getBarDataSet(List<BarEntry> entries, String label, @ColorInt int textColor, @ColorInt int lineColor) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setBarBorderWidth(5);
        dataSet.setBarShadowColor(lineColor);
        dataSet.setValueTextColor(textColor);
        dataSet.setDrawValues(false);
        return dataSet;
    }


    /*** 配置柱状图基础设置 * @param barChart* @param xLabels*/
    public static void configBarChart(BarChart barChart, final List<String> xLabels) {
        barChart.getDescription().setEnabled(false);
        //设置描述
        barChart.setPinchZoom(false);
        //设置按比例放缩柱状图
        barChart.setScaleEnabled(false);
        barChart.setDragEnabled(true);
        barChart.setNoDataText("");// 没有数据时的提示文案
        // x坐标轴设置 //

        //设置自定义的x轴值格式化器
        XAxis xAxis = barChart.getXAxis();
        //获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴标签显示位置
        xAxis.setDrawGridLines(false);
        // 不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        // 显示x轴标签
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabels.get(Math.min(Math.max((int) value, 0), xLabels.size() - 1));
            }
        };
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(10);
        //设置标签字体大小
        xAxis.setTextColor(barChart.getResources().getColor(R.color.white));
        xAxis.setAxisLineColor(Color.parseColor("#4cffffff"));
        xAxis.setLabelCount(xLabels.size());//设置标签显示的个数
        // y轴设置
        YAxis leftAxis = barChart.getAxisLeft();
        //获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置y轴标签显示在外侧
        leftAxis.setAxisMinimum(0f);
        //设置Y轴最小值
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);
        //禁止绘制y轴标签
        leftAxis.setDrawAxisLine(false);
        //禁止绘制y轴
        leftAxis.setAxisLineColor(Color.parseColor("#4cffffff"));
        leftAxis.setTextColor(barChart.getResources().getColor(R.color.white));
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ((int) (value * 100)) + "%";
            }
        });
        barChart.getAxisRight().setEnabled(false);
        //禁用右侧y轴
        barChart.getLegend().setEnabled(false);


        Matrix matrix = new Matrix();
        // 根据数据量来确定 x轴缩放大倍
        if (xLabels.size() <= 10) {
            matrix.postScale(1.0f, 1.0f);
        } else if (xLabels.size() <= 15) {
            matrix.postScale(1.5f, 1.0f);
        } else if (xLabels.size() <= 20) {
            matrix.postScale(2.0f, 1.0f);
        } else {
            matrix.postScale(3.0f, 1.0f);
        }
        barChart.getViewPortHandler().refresh(matrix, barChart, false);
        barChart.setExtraBottomOffset(10);//距视图窗口底部的偏移，类似与/ paddingbottom
        barChart.setExtraTopOffset(30);//距视图窗口顶部的偏移，类似与paddingtop
        barChart.setFitBars(true);//使两侧的柱图完全显示
        barChart.animateX(1500);//数据显示动画，从左往右依次显示
    }


    /*** 初始化柱状图图表数据 * @param chart* @param entries* @param title* @param barColor*/
    public static void initBarChart(BarChart chart, List<BarEntry> entries, String title, @ColorInt int barColor) {
        BarDataSet set1 = new BarDataSet(entries, title);
        set1.setValueTextColor(Color.WHITE);
        set1.setColor(barColor);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        // 设置bar的宽度，但是点很多少的时候好像没作用，会拉得很宽
        data.setBarWidth(0.1f);
        // 设置value值 颜色
        data.setValueTextColor(Color.WHITE);
        //设置y轴显示的标签
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) (value * 100)) + "%";
            }
        });
        chart.setData(data);
        chart.invalidate();
    }


}
