package com.example.dell_.denglu;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changepassword extends AppCompatActivity {
    private EditText change_account,change_pass,change_passnew;
    private Button ok;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="USER.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ok=(Button)findViewById(R.id.change_confirm);
        change_account=(EditText)findViewById(R.id.change_account);
        change_pass=(EditText)findViewById(R.id.change_pass);
        change_passnew=(EditText)findViewById(R.id.change_passnew);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String account = change_account.getText().toString();
                String pass = change_pass.getText().toString();
                String pass2 = change_passnew.getText().toString();

                db=dbHelper.getReadableDatabase();
                dbHelper=new MyDatabaseHelper(changepassword.this,DATABASE_NAME,null,DATABASE_VERSION);
                Cursor cursor=db.query(TABLE_NAME, new String[]{"account"},"account=?",new String[]{account},null,null,null);
                if(cursor.moveToFirst()){
                    do {
                        String account2 = cursor.getString(cursor.getColumnIndex("account"));
                        if (account.equals(account2)) {
                            ContentValues values = new ContentValues();
                            values.put("password", pass2);
                            db.update("user", values, "account=?", new String[]{account});
                        }
                    }while (cursor.moveToNext());
                    cursor.close();

                }

                if (account.equals("") || pass.equals("") || pass2.equals("")) {
                    new AlertDialog.Builder(changepassword.this).setTitle("警告")
                            .setMessage("信息不能为空")
                            .setPositiveButton("确定", null)
                            .show();
                }


            }
        });

    }
}
