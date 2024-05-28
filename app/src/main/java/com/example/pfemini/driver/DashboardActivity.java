package com.example.pfemini.driver;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Models.DestinationCount;
import com.example.pfemini.Models.MonthlyDistance;
import com.example.pfemini.R;
import com.example.pfemini.Network.RetrofitClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private BarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Fetch and display bar chart data
        fetchBarChartData(username);

        // Fetch and display pie chart data
        fetchPieChartData(username);
    }

    private void fetchBarChartData(String username) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<List<MonthlyDistance>> call = apiService.getMonthlyDistance(username);
        call.enqueue(new Callback<List<MonthlyDistance>>() {
            @Override
            public void onResponse(Call<List<MonthlyDistance>> call, Response<List<MonthlyDistance>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MonthlyDistance> monthlyDistances = response.body();
                    setBarChartData(monthlyDistances);
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MonthlyDistance>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBarChartData(List<MonthlyDistance> monthlyDistances) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < monthlyDistances.size(); i++) {
            MonthlyDistance data = monthlyDistances.get(i);
            entries.add(new BarEntry(i, data.getTotalKm()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "KM per Month");
        dataSet.setColors(getBlueShades());
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Customizing the X-Axis
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < monthlyDistances.size()) {
                    return monthlyDistances.get((int) value).getMonth();
                } else {
                    return "";
                }
            }
        });
        barChart.getXAxis().setTextSize(12f);
        barChart.getXAxis().setGranularity(1f); // Ensure labels are shown for each bar

        // Customizing the Y-Axis
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getAxisRight().setEnabled(false); // Disable the right axis

        // Customizing the Chart
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawGridBackground(false);

        barChart.invalidate(); // Refresh the chart
    }

    private void fetchPieChartData(String username) {
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<List<DestinationCount>> call = apiService.getDestinationCounts(username);
        call.enqueue(new Callback<List<DestinationCount>>() {
            @Override
            public void onResponse(Call<List<DestinationCount>> call, Response<List<DestinationCount>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DestinationCount> destinationCounts = response.body();
                    setPieChartData(destinationCounts);
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DestinationCount>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPieChartData(List<DestinationCount> destinationCounts) {
        List<PieEntry> entries = new ArrayList<>();
        for (DestinationCount data : destinationCounts) {
            entries.add(new PieEntry(data.getCount(), data.getDestination()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Destinations");
        dataSet.setColors(getBlueShades());
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);

        pieChart.invalidate(); // Refresh the chart
    }

    private int[] getBlueShades() {
        return new int[]{
                Color.rgb(135, 206, 250), // Light Sky Blue
                Color.rgb(70, 130, 180),  // Steel Blue
                Color.rgb(0, 191, 255),   // Deep Sky Blue
                Color.rgb(30, 144, 255),  // Dodger Blue
                Color.rgb(65, 105, 225),  // Royal Blue
                Color.rgb(0, 0, 255),     // Blue
                Color.rgb(0, 0, 139)      // Dark Blue
        };
    }
}
