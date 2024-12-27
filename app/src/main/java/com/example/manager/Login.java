package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.manager.databinding.ActivityLoginBinding;
import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in your username and password.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean checkCredentials = dbHelper.checkUserPass(username, password);

                    if (checkCredentials) {
                        Toast.makeText(Login.this, "Login Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}