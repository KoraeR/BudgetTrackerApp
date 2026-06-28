package com.example.budgettrackerapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        if (dbHelper.getAllExpenses().isEmpty()) {

            dbHelper.insertExpense(85.50, "Food", "01/05/2026", "Lunch at KFC");

            dbHelper.insertExpense(25.00, "Transport", "02/05/2026", "Taxi fare to campus");

            dbHelper.insertExpense(320.75, "Groceries", "03/05/2026", "Monthly grocery shopping");

            dbHelper.insertExpense(120.00, "Entertainment", "04/05/2026", "Cinema tickets");

            dbHelper.insertExpense(49.00, "Data", "05/05/2026", "Mobile data bundle");
        }
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });

        refreshExpenseList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshExpenseList();
    }

    public void refreshExpenseList() {
        expenseList = dbHelper.getAllExpenses();

        if (adapter == null) {
            adapter = new ExpenseAdapter(this, expenseList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter = new ExpenseAdapter(this, expenseList);
            recyclerView.setAdapter(adapter);
        }
    }
}