package com.example.dell_.denglu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;

public class contact extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    List<String> contactsList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ListView contactsView =(ListView)findViewById(R.id.contact_view);
        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactsList);
        contactsView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            readContacts();
        }
      /*  contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{


                Intent f=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ num));
                startActivity(f);}catch (SecurityException e){e.printStackTrace();}
            }
        });*/
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }*/

    private void readContacts(){
        Cursor cursor=null;
        try{
            //查询联系人数据
            cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if(cursor !=null){
                while (cursor.moveToNext()){
                    //姓名
                    String displayName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //手机号
                    String number =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor !=null){
                cursor.close();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }else {
                    Toast.makeText(this,"联系人获取失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

}
