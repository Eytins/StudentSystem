package com.example.studentsystem.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    public String TB_Name = "userinfo";
    public String TB_Name2 = "course";
    public String TB_Name3 = "courseStudentSelected";

    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TB_Name
                + "(uid integer primary key autoincrement,"
                + "username varchar,"
                + "password varchar,"
                + "hometown varchar,"
                + "picture varchar"
                + ")");

        sqLiteDatabase.execSQL("create table if not exists " + TB_Name2
                + "(cid integer primary key autoincrement,"
                + "courseName varchar,"
                + "courseTeacher varchar,"
                + "courseTime varchar"
                + ")");

        sqLiteDatabase.execSQL("create table if not exists " + TB_Name3
                + "(cid integer primary key autoincrement,"
                + "courseName varchar,"
                + "student varchar"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TB_Name);
        sqLiteDatabase.execSQL("drop table if exists " + TB_Name2);
        sqLiteDatabase.execSQL("drop table if exists " + TB_Name3);
        onCreate(sqLiteDatabase);
    }
}
