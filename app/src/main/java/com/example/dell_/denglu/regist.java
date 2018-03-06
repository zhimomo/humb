package com.example.dell_.denglu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class regist extends AppCompatActivity {
    private EditText register_edit,pass_edit,passcheck_edit;
    private Button confirm;
    private MyDatabaseHelper dbHelper;
    private static final String DATABASE_NAME="USER.db";
    private static final String TABLE_NAME="user";
    private static final int DATABASE_VERSION=1;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        register_edit=(EditText)findViewById(R.id.register_edit);
        pass_edit=(EditText)findViewById(R.id.pass_edit);
        passcheck_edit=(EditText)findViewById(R.id.passcheck_edit);
        confirm=(Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String account = register_edit.getText().toString();
                String password = pass_edit.getText().toString();
                String password2 = passcheck_edit.getText().toString();

                dbHelper=new MyDatabaseHelper(regist.this,DATABASE_NAME,null,DATABASE_VERSION);
                db =  dbHelper.getReadableDatabase();
                Cursor cursor=db.query(TABLE_NAME, new String[]{"account"},null,null,null,null,null);
                if(cursor.moveToFirst()) {
                    do {
                        String account2=cursor.getString(cursor.getColumnIndex("account"));
                        if (account.equals(account2)) {
                            new AlertDialog.Builder(regist.this).setTitle("")
                                    .setMessage("账号已存在")
                                    .setPositiveButton("确定",null)
                                    .show();
                        }
                    }while (cursor.moveToNext());
                    cursor.close();
                }


                if (account.equals("") || password.equals("") || password2.equals("")) {
                    Toast.makeText(regist.this, "注册信息不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(password2))
                {

                    db.execSQL("insert into user (account,password) values(?,?)",new String[]{account,password});

                    Toast.makeText(regist.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent d=new Intent(regist.this,MainLogin.class);
                    startActivity(d);
                }
                else
                {
                    Toast.makeText(regist.this,"两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
