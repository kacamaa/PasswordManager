package com.example.manager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper; // Deklarasi DBHelper
    private akunAdapter accountAdapter;
    private ActivityResultLauncher<Intent> addAccountLauncher;
    private ActivityResultLauncher<Intent> editAccountLauncher; // Tambahkan ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext()); // Inisialisasi di sini

        // Inisialisasi ActivityResultLauncher untuk menambahkan akun
        addAccountLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Refresh ListView setelah akun ditambahkan
                        refreshListView(findViewById(R.id.listViewSelectedApp), dbHelper);
                    }
                }
        );

        // Inisialisasi ActivityResultLauncher untuk mengedit akun
        editAccountLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Refresh ListView setelah akun diedit
                        refreshListView(findViewById(R.id.listViewSelectedApp), dbHelper);
                    }
                }
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.listViewSelectedApp);
        refreshListView(listView, dbHelper);

        // Start Add activity when the button is clicked
        findViewById(R.id.add).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Add.class);
            addAccountLauncher.launch(intent);
        });

        // Handle item click to open Edit activity
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Ambil item yang diklik sebagai objek akun
            akun selectedAccount = (akun) accountAdapter.getItem(position);

            // Kirim email ke Edit activity dengan key yang sesuai
            Intent intent = new Intent(MainActivity.this, Edit.class);
            if (selectedAccount != null) {
                intent.putExtra("email_akun", selectedAccount.getEmail_akun()); // Kirim email dengan key "email_akun"
                editAccountLauncher.launch(intent); // Ganti startActivity dengan editAccountLauncher.launch
            }
        });
    }

    public void refreshListView(ListView listView, DBHelper dbHelper) {
        List<akun> accounts = dbHelper.getAllAccounts();
        if (accountAdapter == null) {
            accountAdapter = new akunAdapter(this, accounts);
            listView.setAdapter(accountAdapter);
        } else {
            accountAdapter.clear();
            accountAdapter.addAll(accounts);
            accountAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

