package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class ForgetPassword3Activity extends AppCompatActivity {

    private EditText passwordOfForget3;
    private Button   next3;

    com.example.studentsystem.utils.dbHelper dbHelper;
    String                                   DB_Name = "mydb";
    SQLiteDatabase                           database;
    Cursor                                   cursor;
    boolean                                  flag    = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password3);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        passwordOfForget3 = findViewById(R.id.passwordOfForget3);
        next3 = findViewById(R.id.next3);

        final Intent intent = getIntent();

        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new dbHelper(ForgetPassword3Activity.this, DB_Name, null, 1);
                database = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("password", passwordOfForget3.getText().toString().trim());
                String   whereClause = "username=?";
                String[] whereArgs   = new String[]{intent.getStringExtra("username")};
                database.update(dbHelper.TB_Name, values, whereClause, whereArgs);

                Toast.makeText(ForgetPassword3Activity.this, "修改成功", Toast.LENGTH_SHORT);
                Intent intent1 = new Intent();
                intent1.setClass(ForgetPassword3Activity.this, LoginActivity.class);
                startActivity(intent1);
                ForgetPassword3Activity.this.finish();
            }
        });
    }
}