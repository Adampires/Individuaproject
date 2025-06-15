package com.example.individuaproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Historydetail extends AppCompatActivity {

    TextView monthText, yearText, kwhText, totalChargeText, rebateText, finalCostText, tipText;
    Button deleteBtn;
    DatabaseHelper dbHelper;
    int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historydetail);

        // Initialize views
        monthText = findViewById(R.id.monthText);
        yearText = findViewById(R.id.yearText);
        kwhText = findViewById(R.id.kwhText);
        totalChargeText = findViewById(R.id.totalChargeText);
        rebateText = findViewById(R.id.rebateText);
        finalCostText = findViewById(R.id.finalCostText);
        deleteBtn = findViewById(R.id.deleteBtn);
        tipText = findViewById(R.id.energyTip); // You must add this TextView in your XML

        dbHelper = new DatabaseHelper(this);

        // Get record ID from intent
        recordId = getIntent().getIntExtra("id", -1);
        if (recordId == -1) {
            Toast.makeText(this, "Invalid record", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load data from DB
        loadData();

        // Delete button logic with confirmation
        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this record?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean deleted = dbHelper.deleteRecord(recordId);
                        if (deleted) {
                            Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Historydetail.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Start rotating tips
        startTipRotation();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getOneData(recordId);
        if (cursor != null && cursor.moveToFirst()) {
            monthText.setText("Month: " + cursor.getString(1));
            yearText.setText("Year: " + cursor.getString(2));
            kwhText.setText("Usage (kWh): " + cursor.getDouble(3));
            totalChargeText.setText("Total Charges: RM " + String.format("%.2f", cursor.getDouble(4)));
            rebateText.setText("Rebate: " + String.format("%.0f", cursor.getDouble(5)) + "%");
            finalCostText.setText("Final Cost: RM " + String.format("%.2f", cursor.getDouble(6)));
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void startTipRotation() {
        String[] tips = {
                "Tip: Turn off appliances when not in use.",
                "Tip: Use LED lights to save energy!",
                "Tip: Unplug chargers when not charging.",
                "Tip: Use natural light during daytime.",
                "Tip: Use energy-efficient appliances."
        };

        final int[] index = {0};
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tipText.setText(tips[index[0]]);
                index[0] = (index[0] + 1) % tips.length;
                handler.postDelayed(this, 4000); // rotate every 4 seconds
            }
        };
        handler.post(runnable);
    }
}
