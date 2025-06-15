package com.example.individuaproject;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    RecyclerView historyRecyclerView;
    DatabaseHelper dbHelper;
    ArrayList<Integer> ids;
    ArrayList<String> months, years, costs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        dbHelper = new DatabaseHelper(this);

        ids = new ArrayList<>();
        months = new ArrayList<>();
        years = new ArrayList<>();
        costs = new ArrayList<>();

        loadBills();
    }

    private void loadBills() {
        Cursor cursor = dbHelper.getAllBills();

        if (cursor.getCount() == 0) return;

        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(0));
            months.add(cursor.getString(1));
            years.add(cursor.getString(2));
            costs.add(cursor.getString(6)); // final cost
        }

        HistoryAdapter adapter = new HistoryAdapter(this, ids, months, years, costs);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(adapter);
    }
}
