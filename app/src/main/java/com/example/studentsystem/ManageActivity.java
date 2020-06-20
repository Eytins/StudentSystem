package com.example.studentsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsystem.utils.dbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageActivity extends AppCompatActivity {

    private EditText courseName;
    private EditText courseTeacher;
    private EditText courseTime;
    private Button   addCourse;
    private ListView listOfCourseInManage;

    dbHelper       dbHelper;
    String         DB_Name = "mydb";
    SQLiteDatabase database;
    Cursor         cursor;
    boolean        dbFlag  = true;

    private static String[] PERMISSIONS_STORGE      = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //权限的请求编码
    private static int      REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(ManageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ManageActivity.this, PERMISSIONS_STORGE, REQUEST_PERMISSION_CODE);
            }
        }

        courseName = findViewById(R.id.courseName);
        courseTeacher = findViewById(R.id.courseTeacher);
        courseTime = findViewById(R.id.courseTime);
        addCourse = findViewById(R.id.addCourse);
        listOfCourseInManage = findViewById(R.id.listOfCourseInManage);


        //创建链接，并打开数据库
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        database = dbHelper.getWritableDatabase();

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name    = courseName.getText().toString().trim();
                String teacher = courseTeacher.getText().toString().trim();
                String time    = courseTime.getText().toString().trim();

                if (name.equals("") || teacher.equals("") || time.equals("")) {
                    Toast.makeText(ManageActivity.this, "数据不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    cursor = database.query(dbHelper.TB_Name2, null, null, null, null, null, null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        if (name.equals(cursor.getString(1))) {
                            dbFlag = false;
                        }
                        cursor.moveToNext();
                    }

                    if (dbFlag) {
                        values.put("courseName", name);
                        values.put("courseTeacher", teacher);
                        values.put("courseTime", time);
                        long rowId = database.insert(dbHelper.TB_Name2, null, values);
                        if (rowId == -1) {
                            Toast.makeText(ManageActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ManageActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            //todo:最后把这里复原
                            /*courseName.setText("");
                            courseTeacher.setText("");
                            courseTime.setText("");*/
                        }
                    } else {
                        Toast.makeText(ManageActivity.this, "课程名称已存在", Toast.LENGTH_SHORT).show();
                        dbFlag = true;
                    }
                }

                showCoursesOnListView();
            }
        });

        listOfCourseInManage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater layoutInflater = LayoutInflater.from(ManageActivity.this);
                View           editCourse     = layoutInflater.inflate(R.layout.edit_course, null);
                TextView courseName = editCourse.findViewById(R.id.textView6);

                String name = ((TextView) view.findViewById(R.id.textView)).getText().toString();
                courseName.setText(name);

                AlertDialog.Builder builder = new AlertDialog.Builder(ManageActivity.this);
                builder.setView(editCourse);
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo:将两个editText中的内容的修改执行到数据库

                    }
                });

                builder.show();
            }
        });

        showCoursesOnListView();
    }

    public void showCoursesOnListView() {
        dbHelper = new dbHelper(this, DB_Name, null, 1);
        database = dbHelper.getWritableDatabase();
        cursor = database.query(dbHelper.TB_Name2, null, null, null, null, null, "cid ASC");
        cursor.moveToFirst();
        List<Map<String, Object>> list = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            cursor.getString(1);
            cursor.getString(2);
            cursor.getString(3);

            Map<String, Object> map = new HashMap<>();
            map.put("courseName", cursor.getString(1));
            map.put("courseTeacher", cursor.getString(2));
            map.put("courseTime", cursor.getString(3));
            list.add(map);
            cursor.moveToNext();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                ManageActivity.this,
                list, R.layout.manage, new String[]{"courseName", "courseTeacher", "courseTime"},
                new int[]{R.id.textView, R.id.textView2, R.id.textView3}
        );
        listOfCourseInManage.setAdapter(adapter);

    }
}