package com.example.tidytime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static final String DATABASE_NAME = "TidyTime.db"; // Nama database
    private static final int DATABASE_VERSION = 2; // Versi database

    // Tabel pengguna
    private static final String USER_TABLE_NAME = "user";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel pengguna
        String createUserTable = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUserTable);

    }

    // Fungsi untuk menyimpan data pengguna
    public boolean insertUser(String email, String password) {
        if (email == null || password == null) return false; // Validasi
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);

        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Fungsi untuk memperbarui password pengguna
    public boolean updatePassword(String email, String newPassword) {
        if (email == null || newPassword == null) return false; // Validasi
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PASSWORD, newPassword);

        int rows = db.update(USER_TABLE_NAME, contentValues, COL_EMAIL + "=?", new String[]{email});
        return rows > 0;
    }

    // Fungsi untuk memvalidasi pengguna berdasarkan email dan password
    public boolean checkUser(String email, String password) {
        if (email == null || password == null) return false; // Validasi
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, null,
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    // Fungsi untuk menghapus pengguna berdasarkan email
    public boolean deleteUser(String email) {
        if (email == null) return false; // Validasi
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(USER_TABLE_NAME, COL_EMAIL + "=?", new String[]{email});
        return rows > 0;
    }
}
