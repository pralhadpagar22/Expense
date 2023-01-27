package com.example.pralhad.dailyexpneses.general;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.DashboardDataSource;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static androidx.core.content.ContextCompat.getColor;

public class GraphView {
    //Graph View
    public static final int GRAPH_TYPE_BAR = 1;
    public static final int GRAPH_TYPE_PIE = 2;

    private PieChart pieChart;
    private BarChart barChart;
    private String year;
    private DashboardDataSource dataSource;
    private View root;
    private Context context;
    private ArrayList<String> barChartMonthlyVals;

    private Map<String, Object> barChartData;

    public GraphView(String year, View root, Context context, int graphType) {
        this.year = year;
        this.dataSource = new DashboardDataSource(MainActivity.dataSource);
        this.root = root;
        this.context = context;
        if (graphType == GraphView.GRAPH_TYPE_BAR)
            barChart();
        barChartMonthlyVals = new ArrayList<>(Arrays.asList(
                context.getResources().getString(R.string.text_Jan),
                context.getResources().getString(R.string.text_Feb),
                context.getResources().getString(R.string.text_Mar),
                context.getResources().getString(R.string.text_Apr),
                context.getResources().getString(R.string.text_May),
                context.getResources().getString(R.string.text_Jun),
                context.getResources().getString(R.string.text_July),
                context.getResources().getString(R.string.text_Aug),
                context.getResources().getString(R.string.text_Sep),
                context.getResources().getString(R.string.text_Oct),
                context.getResources().getString(R.string.text_Nov),
                context.getResources().getString(R.string.text_Dec)
        ));
    }
    private void barChart() {
        barChart = (BarChart) root.findViewById(R.id.bar_chart);
        //setupBarChart();
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getLegend().setEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.animateY(3000);

        //related to x axis
        barChart.getAxisRight().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barChartMonthlyVals));
        xAxis.setTextColor(getColor(context, R.color.colorPrimary));

        xAxis.setAxisMaximum(14);
        xAxis.setAxisMinimum(0);
        //xAxis.setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        //xAxis.setAxisMaximum(0 + barChart.getBarData().getGroupWidth(0.4f, 0f) * 12);


        //related to Y axis
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setStartAtZero(true);
        //yAxis.setAxisMinimum(123456789.00f);
        //yAxis.setAxisMaximum(-1234.00f);
        yAxis.setValueFormatter(new LargeValueFormatter());
        yAxis.setTextColor(getColor(context, R.color.colorPrimary));


        //setup bar chart data, bar related vars amd marker
        barChartData = dataSource.getBarChartData(year);
        BarDataSet setPaidList, setUnpaidList;
        //set value for paid unpaid bar


        setPaidList = new BarDataSet((ArrayList<BarEntry>) barChartData.get("inPaidMonthlyFloat"), context.getResources().getString(R.string.title_expenses));
        setPaidList.setColor(getColor(context, R.color.colorPrimary));

        setUnpaidList = new BarDataSet((ArrayList<BarEntry>) barChartData.get("inDueMonthlyFloat"), context.getResources().getString(R.string.text_income_type));
        setUnpaidList.setColor(getColor(context, R.color.color_income));

        BarData barCharData = new BarData(setPaidList, setUnpaidList);
        barCharData.setDrawValues(false);
        barCharData.setValueFormatter(new LargeValueFormatter());
        barCharData.setBarWidth(0.3f);

        barChart.setData(barCharData);
        barChart.groupBars(0.5f, 0.4f, 0f);
        //on bar click listener marker view
        barChart.setMarker(new SetMarker(R.layout.bar_chart_marker));
    }

    public class SetMarker extends MarkerView {
        private TextView barMonth, barPaidVal, barUnpaidVal;

        public SetMarker(int layoutResource) {
            super(GraphView.this.context, layoutResource);
            this.barMonth = (TextView) findViewById(R.id.barMonth);
            this.barPaidVal = (TextView) findViewById(R.id.barPaidVal);
            this.barUnpaidVal = (TextView) findViewById(R.id.barUnpaidVal);
            // find your layout components
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            int index = Math.round(e.getX()) - 1;//(int)(e.getX()+ 0.16);
            barMonth.setText(barChartMonthlyVals.get(index).toString());
            barPaidVal.setText(String.valueOf(BigDecimal.valueOf(((ArrayList<Double>) barChartData.get("inPaidMonthlyDouble")).get(index))));
            barUnpaidVal.setText(String.valueOf(BigDecimal.valueOf(((ArrayList<Double>) barChartData.get("inDueMonthlyDouble")).get(index))));
            super.refreshContent(e, highlight);
        }

        private MPPointF mOffset;

        @Override
        public MPPointF getOffset() {
            if (mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-getWidth(), -getHeight());
            }
            return mOffset;
        }
    }

    //    public void pieChart(){
//        pieChart = (PieChart) root.findViewById(R.id.pieChart);
//        pieChart.setRotationEnabled(true);
//        pieChart.setDescription(null);
//        pieChart.getLegend().setEnabled(false);
//        pieChart.setHoleRadius(75f);
//        pieChart.setTransparentCircleAlpha(0);
//        pieChart.setCenterTextSize(16);
//
//        final Map<String,Double> pieChartData = dataSource.getPieChartData(year);
//
//        ArrayList<PieEntry> pieChartDataEntries = new ArrayList<>();
//
//        //If totalPaidYearlyVal or totalUnpaidYearlyVal is zero then add a basic /1 entry to make it representable in the chart.
//        if(pieChartData.get("totalPaidYearlyVal") == 0 && pieChartData.get("totalUnpaidYearlyVal") > 0){
//            // Add this calculation for assign pic chart value when paid value is zero then total unpaid value 1% value set to paid value for pieChart
//            pieChartDataEntries.add(new PieEntry((float)(pieChartData.get("totalUnpaidYearlyVal") * 1/100),""));
//            pieChartDataEntries.add(new PieEntry(Math.abs(Float.parseFloat(String.valueOf(pieChartData.get("totalUnpaidYearlyVal")))),""));
//        }else if(pieChartData.get("totalUnpaidYearlyVal") == 0 && pieChartData.get("totalPaidYearlyVal") > 0){
//            pieChartDataEntries.add(new PieEntry(Math.abs(Float.parseFloat(String.valueOf(pieChartData.get("totalPaidYearlyVal")))),""));
//            // Add this calculation for assign pic chart value when Unpaid value is zero then total paid value 1% value set to Unpaid value for pieChart
//            pieChartDataEntries.add(new PieEntry((float) (pieChartData.get("totalPaidYearlyVal") * 1/100),""));
//        }else{
//            pieChartDataEntries.add(new PieEntry(Math.abs(Float.parseFloat(String.valueOf(pieChartData.get("totalPaidYearlyVal")))),""));
//            pieChartDataEntries.add(new PieEntry(Math.abs(Float.parseFloat(String.valueOf(pieChartData.get("totalUnpaidYearlyVal")))),""));
//        }
//
//
//        //create the data set
//        PieDataSet pieChartDataSet = new PieDataSet(pieChartDataEntries, "");
//        pieChartDataSet.setDrawValues(false);
//        pieChartDataSet.setColors((getColor(context,R.color.colorPaidInvoices)),(getColor(context,R.color.colorUnpaidInvoices)));
//        PieData pieData = new PieData(pieChartDataSet);
//        pieChart.setData(pieData);
//        pieChart.invalidate();
//
//        //Total label & vale of the pie chart
//        TextView pieChartPaidUnpaidTotalYearly = (TextView)root.findViewById(R.id.totalPaidUnpaidInvoiceCount);
//        pieChartPaidUnpaidTotalYearly.setText("Total: " + (SharedVariables.numFormat(BigDecimal.valueOf(pieChartData.get("totalYearlyVal")),true,true,false)));
//
//        //highlights the value of bigger/higher entry in the pie chart also set the value in center circle according to that
//        if(pieChartData.get("totalPaidYearlyVal") >= pieChartData.get("totalUnpaidYearlyVal")){
//            pieChart.setCenterText(spannableText("Paid\n"+(SharedVariables.numFormat(BigDecimal.valueOf(pieChartData.get("totalPaidYearlyVal")), true, true, false)), 4));
//            Highlight highlightTotalPaid = new Highlight(0.0f, Float.parseFloat(String.valueOf(pieChartData.get("totalPaidYearlyVal"))),0); // dataset index for piechart is always 0
//            pieChart.highlightValues(new Highlight[] { highlightTotalPaid });
//        }else if (pieChartData.get("totalUnpaidYearlyVal") > pieChartData.get("totalPaidYearlyVal")){
//            pieChart.setCenterText(spannableText("Unpaid\n"+(SharedVariables.numFormat(BigDecimal.valueOf(pieChartData.get("totalUnpaidYearlyVal")), true, true, false)), 6));
//            Highlight highlightTotalUnpaid = new Highlight(1.0f, Float.parseFloat(String.valueOf(pieChartData.get("totalUnpaidYearlyVal"))),0); // dataset index for piechart is always 0
//            pieChart.highlightValues(new Highlight[] { highlightTotalUnpaid });
//        }
//
//
//        //set on click action to set paid unpaid value in center circle
//        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                if(h.getX() == 0.0)
//                    pieChart.setCenterText(spannableText("Paid\n"+(SharedVariables.numFormat(BigDecimal.valueOf(pieChartData.get("totalPaidYearlyVal")), true, true, false)), 4));
//                if(h.getX() == 1.0)
//                    pieChart.setCenterText(spannableText("Unpaid\n"+(SharedVariables.numFormat(BigDecimal.valueOf(pieChartData.get("totalUnpaidYearlyVal")), true, true, false)), 6));
//            }
//            @Override
//            public void onNothingSelected() {
//            }
//        });
//    }
    private SpannableString spannableText(String text, int end) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, end, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, end, 0);
        return s;
    }

}
