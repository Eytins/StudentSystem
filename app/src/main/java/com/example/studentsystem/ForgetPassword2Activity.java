package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentsystem.utils.dbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgetPassword2Activity extends AppCompatActivity {

    private EditText homelandOfForget2;
    private Button   next2;

    com.example.studentsystem.utils.dbHelper dbHelper;
    String                                   DB_Name = "mydb";
    SQLiteDatabase                           database;
    Cursor                                   cursor;
    boolean                                  flag    = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password2);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        homelandOfForget2 = findViewById(R.id.homelandOfForget2);
        next2 = findViewById(R.id.next2);

        final Intent intent = getIntent();


        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new dbHelper(ForgetPassword2Activity.this, DB_Name, null, 1);
                database = dbHelper.getWritableDatabase();
                String   selection     = "username=?";
                String[] selectionArgs = {intent.getStringExtra("username")};
                cursor = database.query(dbHelper.TB_Name, null, selection, selectionArgs, null, null, "uid ASC");
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    if (homelandOfForget2.getText().toString().trim().equals(cursor.getString(3))) {
                        flag = true;
                    }
                    cursor.moveToNext();
                }

                if (flag) {
                    Intent intent2 = new Intent();
                    intent2.putExtra("homeland", homelandOfForget2.getText().toString());
                    intent2.putExtra("username", intent.getStringExtra("username"));
                    intent2.setClass(ForgetPassword2Activity.this, ForgetPassword3Activity.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(ForgetPassword2Activity.this, "不对！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}