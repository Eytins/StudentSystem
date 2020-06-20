package com.example.studentsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsystem.utils.dbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listOfCourseInStudent;
    private ListView listOfCourseInStudent2;


    dbHelper       dbHelper;
    String         DB_Name = "mydb";
    SQLiteDatabase database;
    Cursor         cursor;
    boolean        dbFlag  = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent       intent   = getIntent();
        final String userName = intent.getStringExtra("userName");

        listOfCourseInStudent = findViewById(R.id.listOfCourseInStudent);
        listOfCourseInStudent2 = findViewById(R.id.listOfCourseInStudent2);
        showCoursesOnListView(userName);

        listOfCourseInStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("提示");
                builder.setMessage("您是否要选择这门课程");
                builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //执行数据库中选择，然后显示一遍
                        dbHelper = new dbHelper(MainActivity.this, DB_Name, null, 1);
                        database = dbHelper.getWritableDatabase();
                        final String courseName = ((TextView) view.findViewById(R.id.textView)).getText().toString();
                        //todo:添加到数据库3中
                        ContentValues values = new ContentValues();
                        values.put("courseName", courseName);
                        values.put("student", userName);
                        long rowId = database.insert(dbHelper.TB_Name3, null, values);
                        if (rowId == -1) {
                            Toast.makeText(MainActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "选择课程成功", Toast.LENGTH_SHORT).show();
                        }
                        showCoursesOnListView(userName);
                    }
                });
                builder.create().show();
            }
        });


    }

    public void showCoursesOnListView(String userName) {
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        database = dbHelper.getWritableDatabase();
        cursor = database.query(dbHelper.TB_Name2, null, null, null, null, null, "cid ASC");
        cursor.moveToFirst();
        List<Map<String, Object>> list = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseName", cursor.getString(1));
            map.put("courseTeacher", cursor.getString(2));
            map.put("courseTime", cursor.getString(3));
            list.add(map);
            cursor.moveToNext();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                MainActivity.this,
                list, R.layout.manage, new String[]{"courseName", "courseTeacher", "courseTime"},
                new int[]{R.id.textView, R.id.textView2, R.id.textView3}
        );
        listOfCourseInStudent.setAdapter(adapter);

        //显示已选课程
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        database = dbHelper.getWritableDatabase();
        String   selection     = "student=?";
        String[] selectionArgs = {userName};
        cursor = database.query(dbHelper.TB_Name3, null, selection, selectionArgs, null, null, "cid ASC");
        cursor.moveToFirst();
        List<Map<String, Object>> list2 = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseName", cursor.getString(1));
            list2.add(map);
            cursor.moveToNext();
        }

        SimpleAdapter adapter2 = new SimpleAdapter(
                MainActivity.this,
                list2, R.layout.student_course, new String[]{"courseName"},
                new int[]{R.id.textView10}
        );
        listOfCourseInStudent2.setAdapter(adapter2);

    }
}