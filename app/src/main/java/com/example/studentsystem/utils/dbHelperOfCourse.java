package com.example.studentsystem.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelperOfCourse extends SQLiteOpenHelper {

    public String TB_Name = "course";

    public dbHelperOfCourse(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists " + TB_Name
                + "(cid integer primary key autoincrement,"
                + "courseName varchar,"
                + "courseTeacher varchar,"
                + "courseTime varchar"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TB_Name);
        onCreate(db);
    }



}
