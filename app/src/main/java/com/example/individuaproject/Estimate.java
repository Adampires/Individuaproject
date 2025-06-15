package com.example.individuaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class Estimate extends AppCompatActivity {

    Spinner applianceSpinner, hourSpinner, daySpinner;
    TextView resultPerHour, resultKwh, resultCost;
    Button backButton;

    // Appliance power ratings in Watts
    final int[] applianceWatts = {75, 1500, 200, 500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);

        applianceSpinner = findViewById(R.id.applianceSpinner);
        hourSpinner = findViewById(R.id.hourSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        resultPerHour = findViewById(R.id.resultPerHour);
        resultKwh = findViewById(R.id.resultKwh);
        resultCost = findViewById(R.id.resultCost);
        backButton = findViewById(R.id.backBtn);

        String[] appliances = {"Fan", "Aircond", "Fridge", "Washing Machine"};
        String[] hours = new String[24];
        String[] days = new String[31];
        for (int i = 0; i < 24; i++) hours[i] = String.valueOf(i + 1);
        for (int i = 0; i < 31; i++) days[i] = String.valueOf(i + 1);

        applianceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, appliances));
        hourSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hours));
        daySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days));

        AdapterView.OnItemSelectedListener selectionListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                calculateEstimate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        applianceSpinner.setOnItemSelectedListener(selectionListener);
        hourSpinner.setOnItemSelectedListener(selectionListener);
        daySpinner.setOnItemSelectedListener(selectionListener);

        backButton.setOnClickListener(v -> finish());
    }

    private void calculateEstimate() {
        int applianceIndex = applianceSpinner.getSelectedItemPosition();
        int hours = Integer.parseInt(hourSpinner.getSelectedItem().toString());
        int days = Integer.parseInt(daySpinner.getSelectedItem().toString());

        double watt = applianceWatts[applianceIndex];
        double kwhPerHour = watt / 1000.0;
        double dailyKwh = kwhPerHour * hours;
        double monthlyKwh = dailyKwh * days;
        double cost = monthlyKwh * 0.218;

        resultPerHour.setText(String.format("kWh Per Hour: %.3f", kwhPerHour));
        resultKwh.setText(String.format("Monthly Estimated kWh: %.2f", monthlyKwh));
        resultCost.setText(String.format("Monthly Estimated Cost: RM %.2f", cost));
    }
}
