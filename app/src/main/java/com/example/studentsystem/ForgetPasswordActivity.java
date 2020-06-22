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

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText userNameOfForget;
    private Button   next1;

    com.example.studentsystem.utils.dbHelper dbHelper;
    String                                   DB_Name = "mydb";
    SQLiteDatabase                           database;
    Cursor                                   cursor;
    boolean                                  flag    = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        userNameOfForget = findViewById(R.id.userNameOfForget);
        next1 = findViewById(R.id.next1);

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new dbHelper(ForgetPasswordActivity.this, DB_Name, null, 1);
                database = dbHelper.getWritableDatabase();

                cursor = database.query(dbHelper.TB_Name, null, null, null, null, null, "uid ASC");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (userNameOfForget.getText().toString().trim().equals(cursor.getString(1))) {
                        flag = true;
                    }
                    cursor.moveToNext();
                }

                if (flag) {
                    Intent intent = new Intent();
                    intent.putExtra("username", userNameOfForget.getText().toString());
                    intent.setClass(ForgetPasswordActivity.this, ForgetPassword2Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "无此用户", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}