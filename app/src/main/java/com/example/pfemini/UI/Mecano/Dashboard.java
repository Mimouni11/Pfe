package com.example.pfemini.UI.Mecano;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.Models.TaskCount;
import com.example.pfemini.Models.TaskDoneRatio;
import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.Network.RetrofitClient;
import com.example.pfemini.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private Apiservices apiService;
    private PieChart pieChart;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        pieChart = findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);

        // Initialize Retrofit and Apiservices
        apiService = RetrofitClient.getClient().create(Apiservices.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        // Fetch and display pie chart data
        fetchTaskCounts(username);

        // Fetch and display line chart data
        fetchDoneRatioData(username);
    }

    private void fetchTaskCounts(String username) {
        Call<List<TaskCount>> call = apiService.getTaskCounts(username);
        call.enqueue(new Callback<List<TaskCount>>() {
            @Override
            public void onResponse(Call<List<TaskCount>> call, Response<List<TaskCount>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskCount> taskCounts = response.body();
                    displayPieChart(taskCounts);
                } else {
                    Log.e(TAG, "Failed to fetch task counts");
                    Toast.makeText(Dashboard.this, "Failed to fetch task counts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskCount>> call, Throwable t) {
                Log.e(TAG, "Error fetching task counts: " + t.getMessage());
                Toast.makeText(Dashboard.this, "Error fetching task counts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPieChart(List<TaskCount> taskCounts) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (TaskCount taskCount : taskCounts) {
            entries.add(new PieEntry(taskCount.getCount(), taskCount.getTaskType()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Task Counts");
        dataSet.setColors(getBlueShades());
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);

        pieChart.invalidate(); // Refresh chart
    }

    private void fetchDoneRatioData(String username) {
        Call<List<TaskDoneRatio>> call = apiService.getDoneRatio(username);
        call.enqueue(new Callback<List<TaskDoneRatio>>() {
            @Override
            public void onResponse(Call<List<TaskDoneRatio>> call, Response<List<TaskDoneRatio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskDoneRatio> doneRatios = response.body();
                    setLineChartData(doneRatios);
                } else {
                    Toast.makeText(Dashboard.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskDoneRatio>> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLineChartData(List<TaskDoneRatio> doneRatios) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < doneRatios.size(); i++) {
            TaskDoneRatio data = doneRatios.get(i);
            entries.add(new Entry(i, data.getDoneRatio()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Task Done Ratio");
        dataSet.setColors(getBlueShades());
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Customizing the X-Axis
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < doneRatios.size()) {
                    return doneRatios.get((int) value).getDate();
                } else {
                    return "";
                }
            }
        });
        lineChart.getXAxis().setTextSize(12f);
        lineChart.getXAxis().setGranularity(1f); // Ensure labels are shown for each data point

        // Customizing the Y-Axis
        lineChart.getAxisLeft().setTextSize(12f);
        lineChart.getAxisRight().setEnabled(false); // Disable the right axis

        // Customizing the Chart
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        lineChart.invalidate(); // Refresh the chart
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
