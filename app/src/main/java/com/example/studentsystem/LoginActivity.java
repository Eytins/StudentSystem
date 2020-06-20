package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsystem.utils.dbHelper;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView userName;
    private EditText             password;
    private Button               login;
    private TextView             register;
    private CheckBox             saveUserName;
    private TextView             forgetPassword;

    String savedName;
    String savedPassword;

    com.example.studentsystem.utils.dbHelper dbHelper;
    String                                   DB_Name = "mydb";
    SQLiteDatabase                           database;
    Cursor                                   cursor;
    boolean                                  flag    = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        final SharedPreferences sharedPreferences  = getSharedPreferences("saveName", MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("savedNameForAutoComplete", MODE_PRIVATE);

        userName = findViewById(R.id.courseName);
        password = findViewById(R.id.courseTeacher);
        login = findViewById(R.id.login);
        register = findViewById(R.id.addCourse);
        saveUserName = findViewById(R.id.checkBox);
        forgetPassword = findViewById(R.id.forgetPassword);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] allUserName = new String[sharedPreferences.getAll().size()]; // sp.getAll().size()返回的是有多少个键值对
                allUserName = sharedPreferences2.getAll().keySet().toArray(new String[0]);
                // sp.getAll()返回一张hash map
                // keySet()得到的是a set of the keys.
                // hash map是由key-value组成的
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        LoginActivity.this,
                        R.layout.namelist_item,
                        allUserName
                );

                userName.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                password.setText(sharedPreferences.getString(userName.getText()
                        .toString(), ""));// 自动输入密码
            }
        });

        savedName = sharedPreferences.getString("savedName", "");
        savedPassword = sharedPreferences.getString("savedPassword", "");
        if (!savedName.equals("") && !savedPassword.equals("")) {
            userName.setText(sharedPreferences.getString("savedName", ""));
            password.setText(sharedPreferences.getString("savedPassword", ""));
            saveUserName.setChecked(true);
        }

        dbHelper = new dbHelper(this, DB_Name, null, 1);
        database = dbHelper.getWritableDatabase();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean admin = false;
                if (userName.getText().toString().trim().equals("admin") &&
                        password.getText().toString().trim().equals("123")) {
                    admin = true;
                }

                if (userName.getText().toString().trim().equals("admin") &&
                        password.getText().toString().trim().equals("123")) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, ManageActivity.class);
                    startActivity(intent);
                }

                cursor = database.query(dbHelper.TB_Name, null, null, null, null, null, "uid ASC");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (userName.getText().toString().trim().equals(cursor.getString(1)) &&
                            password.getText().toString().trim().equals(cursor.getString(2))) {
                        flag = true;
                    }
                    cursor.moveToNext();
                }

                if (flag) {

                    //登陆成功后将用户名保存到sharedPreferences2中
                    sharedPreferences2.edit().putString(userName.getText().toString().trim(), "").apply();

                    Toast.makeText(LoginActivity.this, "欢迎回来，" + userName.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    flag = false;
                    Intent intent = new Intent();
                    intent.putExtra("userName", userName.getText().toString());
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    if (saveUserName.isChecked()) {
                        savedName = userName.getText().toString();
                        savedPassword = password.getText().toString();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("savedName", savedName);
                        editor.putString("savedPassword", savedPassword);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                    }

                } else if (!flag && !admin) {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                }


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //todo:forget password(forgetPassword)

    }

    long firstTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == keyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(LoginActivity.this, "再按一次，程序退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
}