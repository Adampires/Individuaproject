package com.example.individuaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner monthSpinner, rebateSpinner;
    EditText kwhInput;
    TextView totalCharges, finalCost;
    Button calculateBtn, saveBtn, estimateBtn, historyBtn, aboutBtn;

    double currentTotal = 0;
    double currentFinal = 0;
    int currentKwh = 0;
    int currentRebate = 0;
    String currentMonth = "";
    String currentYear = "2025"; // You can make this dynamic if needed

    double calculateCharges(int kwh) {
        double total = 0;
        if (kwh <= 200) total = kwh * 0.218;
        else if (kwh <= 300) total = 200 * 0.218 + (kwh - 200) * 0.334;
        else if (kwh <= 600) total = 200 * 0.218 + 100 * 0.334 + (kwh - 300) * 0.516;
        else total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (kwh - 600) * 0.546;
        return total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking UI components
        monthSpinner = findViewById(R.id.monthSpinner);
        rebateSpinner = findViewById(R.id.rebateSpinner);
        kwhInput = findViewById(R.id.kwhInput);
        totalCharges = findViewById(R.id.totalCharges);
        finalCost = findViewById(R.id.finalCost);

        calculateBtn = findViewById(R.id.calculateBtn);
        saveBtn = findViewById(R.id.saveBtn);
        estimateBtn = findViewById(R.id.estimateBtn);
        historyBtn = findViewById(R.id.historyBtn);
        aboutBtn = findViewById(R.id.aboutBtn);

        // Populate spinners
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> rebateAdapter = ArrayAdapter.createFromResource(this, R.array.rebate_array, android.R.layout.simple_spinner_item);
        rebateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rebateSpinner.setAdapter(rebateAdapter);

        // Calculate button logic
        calculateBtn.setOnClickListener(v -> {
            String kwhText = kwhInput.getText().toString().trim();
            if (kwhText.isEmpty()) {
                kwhInput.setError("Please enter kWh used");
                return;
            }

            currentKwh = Integer.parseInt(kwhText);
            currentTotal = calculateCharges(currentKwh);
            currentRebate = Integer.parseInt(rebateSpinner.getSelectedItem().toString().replace("%", ""));
            currentFinal = currentTotal - (currentTotal * currentRebate / 100);
            currentMonth = monthSpinner.getSelectedItem().toString();

            totalCharges.setText(String.format("Total Charges: RM %.2f", currentTotal));
            finalCost.setText(String.format("Final Cost: RM %.2f", currentFinal));
        });

        // Save button logic
        saveBtn.setOnClickListener(v -> {
            if (currentKwh == 0 || currentTotal == 0) {
                Toast.makeText(this, "Please calculate before saving", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            boolean success = db.insertBill(currentMonth, currentYear, currentKwh, currentTotal, currentRebate, currentFinal);

            if (success) {
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                kwhInput.setText("");
                totalCharges.setText("Total Charges:");
                finalCost.setText("Final Cost:");
                currentKwh = 0;
                currentTotal = 0;
                currentFinal = 0;
            } else {
                Toast.makeText(this, "Failed to save data!", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation buttons
        estimateBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Estimate.class)));
        historyBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, History.class)));
        aboutBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, about.class)));
    }
}
