package com.example.individuaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class Estimate extends AppCompatActivity {

    Spinner applianceSpinner, hourSpinner;
    TextView resultKwh, resultCost;
    Button backButton;

    // Appliance power ratings in Watts
    final int[] applianceWatts = {75, 1500, 200, 500}; // Fan, Aircond, Fridge, Washing Machine

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);

        applianceSpinner = findViewById(R.id.applianceSpinner);
        hourSpinner = findViewById(R.id.hourSpinner);
        resultKwh = findViewById(R.id.resultKwh);
        resultCost = findViewById(R.id.resultCost);
        backButton = findViewById(R.id.backBtn);

        String[] appliances = {"Fan", "Aircond", "Fridge", "Washing Machine"};
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) hours[i] = String.valueOf(i + 1);

        ArrayAdapter<String> applianceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, appliances);
        applianceSpinner.setAdapter(applianceAdapter);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hours);
        hourSpinner.setAdapter(hourAdapter);

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

        backButton.setOnClickListener(v -> finish());
    }

    private void calculateEstimate() {
        int applianceIndex = applianceSpinner.getSelectedItemPosition();
        int hour = Integer.parseInt(hourSpinner.getSelectedItem().toString());

        double watt = applianceWatts[applianceIndex];
        double kwh = (watt * hour) / 1000.0;
        double cost = kwh * 0.218; // Use first block rate

        resultKwh.setText(String.format("Estimated kWh: %.2f", kwh));
        resultCost.setText(String.format("Estimated Cost: RM %.2f", cost));
    }
}
