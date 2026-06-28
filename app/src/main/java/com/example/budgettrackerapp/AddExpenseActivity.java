package com.example.budgettrackerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etAmount, etCategory, etDescription;
    private Button btnDate, btnSave;
    private String selectedDate;
    private int expenseId = -1;
    private DatabaseHelper dbHelper;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DatabaseHelper(this);
        etAmount = findViewById(R.id.et_amount);
        etCategory = findViewById(R.id.et_category);
        etDescription = findViewById(R.id.et_description);
        btnDate = findViewById(R.id.btn_date);
        btnSave = findViewById(R.id.btn_save);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        btnDate.setText(selectedDate);

        btnDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(AddExpenseActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        btnDate.setText(selectedDate);
                    }, year, month, day);
            dpd.show();
        });

        String mode = getIntent().getStringExtra("mode");
        if (mode != null && mode.equals("edit")) {
            expenseId = getIntent().getIntExtra("id", -1);
            double amount = getIntent().getDoubleExtra("amount", 0);
            String category = getIntent().getStringExtra("category");
            String date = getIntent().getStringExtra("date");
            String description = getIntent().getStringExtra("description");

            etAmount.setText(String.valueOf(amount));
            etCategory.setText(category);
            selectedDate = date;
            btnDate.setText(date);
            etDescription.setText(description);
            setTitle("Edit Expense");
        } else {
            setTitle("Add Expense");
        }

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String amountStr = etAmount.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (amountStr.isEmpty()) {
            etAmount.setError("Amount required");
            return;
        }
        if (category.isEmpty()) {
            etCategory.setError("Category required");
            return;
        }

        double amount = Double.parseDouble(amountStr);
        boolean success;

        if (expenseId == -1) {
            success = dbHelper.insertExpense(amount, category, selectedDate, description);
            if (success) Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
        } else {
            success = dbHelper.updateExpense(expenseId, amount, category, selectedDate, description);
            if (success) Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        }

        if (success) {
            finish();
        } else {
            Toast.makeText(this, "Error saving expense", Toast.LENGTH_SHORT).show();
        }
    }
}
