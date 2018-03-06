package com.example.dell_.denglu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.PerformanceTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainLogin extends AppCompatActivity {
    private Button login;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="USER.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABKE_NAME="user";
    private EditText accountedit,passedit;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox remember_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        //跳转到注册
        Button zhuce =(Button)findViewById(R.id.resgiter);
                zhuce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a=new Intent(MainLogin.this,regist.class);
                        startActivity(a);

                    }
                });
        //跳转到修改密码
      /*  Button alter =(Button)findViewById(R.id.alter);
            alter.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent c=new Intent(MainLogin.this,changepassword.class);
                    startActivity(c);
                }
            });*/
        //记住密码
        pref =  PreferenceManager.getDefaultSharedPreferences(this);
        accountedit=(EditText)findViewById(R.id.account);
        passedit=(EditText)findViewById(R.id.pass);
        remember_pass=(CheckBox)findViewById(R.id.remember_pass);
        login= (Button) findViewById(R.id.login);
        boolean isRemember=pref.getBoolean("remember_pass",false);
        if (isRemember){
            //将账号和密码都设置到文本框中
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            accountedit.setText(account);
            passedit.setText(password);
            remember_pass.setChecked(true);

        }

        class LoginListener implements View.OnClickListener{
            public void onClick(View v){
                String account=accountedit.getText().toString();
                String password=passedit.getText().toString();
                if(account.equals("")||password.equals("")){
                    Toast.makeText(MainLogin.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    if (logincheck(account,password)) {

                        Toast.makeText(MainLogin.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(MainLogin.this, contact.class);
                        startActivity(b);
                        editor=pref.edit();
                        if (remember_pass.isChecked()){
                            editor.putBoolean("remember_pass",true);
                            editor.putString("account",account);
                            editor.putString("password",password);
                        }else {
                            editor.clear();
                        }
                        editor.apply();

                    }  else {
                        Toast.makeText(MainLogin.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }login.setOnClickListener(new LoginListener());}
    //登录判断
        public Boolean logincheck(String account,String pass){
            String accounts=account;
            String passwords=pass;
            dbHelper=new MyDatabaseHelper(MainLogin.this,DATABASE_NAME,null,DATABASE_VERSION);
            db=dbHelper.getReadableDatabase();
            try{
                Cursor cursor=db.query(TABKE_NAME,new String[]{"account","password"},"account=?",new String[]{accounts},null,null,null);
                while (cursor.moveToNext()){
                    String password=cursor.getString(cursor.getColumnIndex("password"));
                    if(passwords.equals(password)){

                        return true;
                    }else{return false;}
                }
            }catch (SQLiteException e){CreateTable();}
            return false;
        }
        private void CreateTable(){
            String sql="CREATE TABLE IF NOT EXISTS"+TABKE_NAME+"account varchar(30) primary key,password varchar(30);";
            try{
                db.execSQL(sql);
            }catch (SQLiteException ex){}
        }
    }

