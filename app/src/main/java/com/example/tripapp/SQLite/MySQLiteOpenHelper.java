package com.example.tripapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tripapp.Bean.User;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "trip.db";
    public static final String TABLE_NAME = "user";
    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 插入用户
    // 成功返回插入的行ID，失败返回-1
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // 根据用户名查询用户
    // 成功返回用户对象，失败返回null
    public User queryUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        User user = null;
        if(cursor != null){
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String passWord = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                user = new User(id, userName, passWord);
                cursor.close();
            }
        }

        db.close();
        return user;
    }

    // 查询用户
    // 成功返回用户对象，失败返回null
    public User queryUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        User user = null;
        if(cursor != null){
            if ( cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String passWord = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                user = new User(id, userName, passWord);
                cursor.close();
            }
        }


        db.close();
        return user;
    }
}
