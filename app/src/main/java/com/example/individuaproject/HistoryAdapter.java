package com.example.individuaproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Integer> ids;
    private final ArrayList<String> months;
    private final ArrayList<String> years;
    private final ArrayList<String> costs;

    public HistoryAdapter(Context ctx, ArrayList<Integer> ids, ArrayList<String> months, ArrayList<String> years, ArrayList<String> costs) {
        this.context = ctx;
        this.ids = ids;
        this.months = months;
        this.years = years;
        this.costs = costs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.monthYear.setText(months.get(position) + " " + years.get(position));
        holder.cost.setText("Final Cost: RM " + costs.get(position));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Historydetail.class);
            intent.putExtra("id", ids.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView monthYear, cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            monthYear = itemView.findViewById(R.id.textMonthYear);
            cost = itemView.findViewById(R.id.textCost);
        }
    }
}
