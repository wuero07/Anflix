package com.example.anflix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserRepository {
    private UserDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new UserDatabaseHelper(context);
    }

    public void insertUser(String name, int age, String gender) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDatabaseHelper.COLUMN_NAME, name);
        values.put(UserDatabaseHelper.COLUMN_AGE, age);
        values.put(UserDatabaseHelper.COLUMN_GENDER, gender);
        db.insert(UserDatabaseHelper.TABLE_USERS, null, values);
    }

    public Cursor getUserData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(UserDatabaseHelper.TABLE_USERS,
                new String[]{UserDatabaseHelper.COLUMN_NAME, UserDatabaseHelper.COLUMN_AGE, UserDatabaseHelper.COLUMN_GENDER},
                null, null, null, null, null);
    }
}
