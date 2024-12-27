package com.example.manager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Add extends AppCompatActivity {
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        dbHelper = new DBHelper(getApplicationContext());

        Spinner spinner = findViewById(R.id.spinnerAccountType);
        List<String> accountTypes = dbHelper.getAccountTypes();  // Ini memanggil data dari DB
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        EditText edtEmailorphone = findViewById(R.id.emailphone_Add);
        EditText edtUsername_Add = findViewById(R.id.username_Add);
        EditText edtPassword_Add = findViewById(R.id.password_Add);
        Button btnAddAccount = findViewById(R.id.addbtn);

        btnAddAccount.setOnClickListener(v -> {
            if (accountTypes.isEmpty()) {
                Toast.makeText(this, "No account types available", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            String selectedAccountType = spinner.getSelectedItem() != null
                    ? spinner.getSelectedItem().toString()
                    : "";

            if (selectedAccountType.isEmpty()) {
                Toast.makeText(this, "Please select an account type", Toast.LENGTH_SHORT).show();
                return;
            }

            String email_akun = edtEmailorphone.getText().toString().trim();
            String username_akun = edtUsername_Add.getText().toString().trim();
            String password_akun = edtPassword_Add.getText().toString().trim();

            if (!email_akun.isEmpty() && !username_akun.isEmpty() && !password_akun.isEmpty()) {
                boolean isInserted = dbHelper.insertAccount(email_akun, username_akun, password_akun);
                if (isInserted) {
                    Toast.makeText(this, "Account added successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Notify that the account was added
                    finish();  // Close Add activity and go back to MainActivity
                } else {
                    Toast.makeText(this, "Failed to add account", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}