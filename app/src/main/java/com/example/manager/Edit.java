package com.example.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Edit extends AppCompatActivity {
    private DBHelper dbHelper;
    private Spinner edit_spinner;
    private EditText emailEditText, usernameEditText, passwordEditText;
    private Button editAccountButton, deleteAccountButton;
    private String selectedAppType;
    private String emailToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbHelper = new DBHelper(this);

        edit_spinner = findViewById(R.id.edit_spinner);
        emailEditText = findViewById(R.id.emailphone_Edit);
        usernameEditText = findViewById(R.id.username_Edit);
        passwordEditText = findViewById(R.id.password_Edit);
        editAccountButton = findViewById(R.id.editbtn);
        deleteAccountButton = findViewById(R.id.deletebtn);

        loadAppTypes();

        emailToEdit = getIntent().getStringExtra("email_akun");
        if (emailToEdit != null) {
            fillAccountData(emailToEdit);
        } else {
            Toast.makeText(this, "No account found to edit", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke activity sebelumnya jika tidak ada email
        }

        edit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAppType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        editAccountButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Edit.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                // Panggil metode updateAccount
                boolean isUpdated = dbHelper.updateAccount(emailToEdit, username, password);
                if (isUpdated) {
                    Toast.makeText(Edit.this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Tambahkan ini
                    finish();
                } else {
                    Toast.makeText(Edit.this, "Failed to update account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteAccountButton.setOnClickListener(v -> {
            if (emailToEdit != null) {
                boolean isDeleted = dbHelper.deleteAccount(emailToEdit);
                if (isDeleted) {
                    Toast.makeText(Edit.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Tambahkan ini
                    finish();
                } else {
                    Toast.makeText(Edit.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Edit.this, "No account to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAppTypes() {
        List<String> appTypes = dbHelper.getAccountTypes();
        ArrayAdapter<String> appAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, appTypes);
        appAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_spinner.setAdapter(appAdapter);
    }

    private void fillAccountData(String email) {
        List<akun> accounts = dbHelper.getAllAccounts();
        boolean accountFound = false;
        for (akun account : accounts) {
            if (account.getEmail_akun().equals(email)) {
                emailEditText.setText(account.getEmail_akun());
                usernameEditText.setText(account.getUsername_akun());
                passwordEditText.setText(account.getPassword_akun());
                accountFound = true;
                break;
            }
        }
        if (!accountFound) {
            Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
            finish(); // Kembali jika akun tidak ditemukan
        }
    }
}

