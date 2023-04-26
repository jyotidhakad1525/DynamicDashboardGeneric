package com.automate.df.model.sales.lead;

import java.util.List;

public class Chart {

    List<ChartData> ChartData;
    List<String> ChartLabels;

    public List<ChartData> getChartData() {
        return ChartData;
    }

    public void setChartData(List<ChartData> chartData) {
        ChartData = chartData;
    }

    public List<String> getChartLabels() {
        return ChartLabels;
    }

    public void setChartLabels(List<String> chartLabels) {
        ChartLabels = chartLabels;
    }
}
