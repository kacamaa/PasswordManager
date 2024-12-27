package com.example.manager;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class akunAdapter extends ArrayAdapter<akun> {
    private Context context;
    private List<akun> accounts;

    public akunAdapter(Context context, List<akun> accounts) {
        super(context, R.layout.single, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single, parent, false);
        }

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView sosmed = convertView.findViewById(R.id.sosmed);
        TextView username = convertView.findViewById(R.id.txtusername);
        TextView password = convertView.findViewById(R.id.txtpassword);

        akun account = accounts.get(position);

        // Bind data
        text1.setText(account.getEmail_akun());
        sosmed.setText(account.getNama_app()); // Pastikan nama_app ada di data
        username.setText(account.getUsername_akun());
        password.setText(account.getPassword_akun());

        return convertView;
    }
}
