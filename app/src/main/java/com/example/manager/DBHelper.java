package com.example.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String databaseName = "Password.db";


    public DBHelper(@Nullable Context context) {
        super(context, "Password.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table allusers(email TEXT primary key, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE app_accounts(Id_app INTEGER PRIMARY KEY AUTOINCREMENT, nama_app TEXT)");
        db.execSQL("CREATE TABLE akun(id_akun INTEGER PRIMARY KEY AUTOINCREMENT, email_akun TEXT, username_akun TEXT, password_akun TEXT)");

        insertAppData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists allusers");
        onCreate(db);
    }

    public boolean insertData(String email, String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = MyDatabase.insert("allusers", null, contentValues);
        return result != -1;
    }

    public void insertAppData(SQLiteDatabase db) {
        String[] appNames = {
                "Social Media", "Bank", "E-Commerce", "Game", "Transportation"
        };

        ContentValues contentValues = new ContentValues();
        for (String appName : appNames) {
            contentValues.put("nama_app", appName);
            // Pastikan hanya menambahkan data jika belum ada di tabel
            db.insertWithOnConflict("app_accounts", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM allusers WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean checkUserPass(String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery(
                "SELECT * FROM allusers WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean insertAccount(String email_akun, String username_akun, String password_akun) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email_akun", email_akun);
        contentValues.put("username_akun", username_akun);
        contentValues.put("password_akun", password_akun);

        long result = MyDatabase.insert("akun", null, contentValues);
        return result != -1;
    }

    public boolean updateAccount(String email, String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username_akun", username);
        contentValues.put("password_akun", password);

        // Update akun berdasarkan email
        int result = MyDatabase.update("akun", contentValues, "email_akun = ?", new String[]{email});
        return result > 0; // Mengembalikan true jika ada baris yang diperbarui
    }

    public List<akun> getAllAccounts() {
        List<akun> accounts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Query from akun table, not app_accounts, as email_akun, username_akun, and password_akun belong in the akun table
        Cursor cursor = db.rawQuery("SELECT email_akun, username_akun, password_akun FROM akun", null);

        if (cursor.moveToFirst()) {
            do {
                akun account = new akun();
                account.setEmail_akun(cursor.getString(cursor.getColumnIndex("email_akun")));
                account.setUsername_akun(cursor.getString(cursor.getColumnIndex("username_akun")));
                account.setPassword_akun(cursor.getString(cursor.getColumnIndex("password_akun")));
                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accounts;
    }

    public List<String> getAccountTypes() {
        List<String> accountTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM app_accounts", null);

        if (cursor.moveToFirst()) {
            do {
                String appName = cursor.getString(cursor.getColumnIndexOrThrow("nama_app"));
                accountTypes.add(appName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accountTypes;
    }

    public boolean deleteAccount(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        int result = MyDatabase.delete("akun", "email_akun = ?", new String[]{email});
        return result > 0;
    }
}
