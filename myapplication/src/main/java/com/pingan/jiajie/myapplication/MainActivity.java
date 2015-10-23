package com.pingan.jiajie.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private final static String SWORD="SWORD";
    //声明五个控件对象
    Button createDatabase=null;
    Button updateDatabase=null;
    Button insert=null;
    Button update=null;
    Button query=null;
    Button delete=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews() {
        //根据控件ID得到控件
        createDatabase = (Button) this.findViewById(R.id.createDatabase);
        updateDatabase = (Button) this.findViewById(R.id.updateDatabase);
        insert = (Button) this.findViewById(R.id.insert);
        update = (Button) this.findViewById(R.id.update);
        query = (Button) this.findViewById(R.id.query);
        delete = (Button) this.findViewById(R.id.delete);
        //添加监听器
        createDatabase.setOnClickListener(this);
        updateDatabase.setOnClickListener(this);
        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //判断所触发的被监听控件，并执行命令
        switch(v.getId()){
            //创建数据库
            case R.id.createDatabase:
                //创建一个DatabaseHelper对象
                DatabaseHelper dbHelper1 = new DatabaseHelper(this, "test_db");
                //取得一个只读的数据库对象
                SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
                break;
            //更新数据库
            case R.id.updateDatabase:
                Intent  intent=new Intent(this,AndroidDatabaseManager.class);
                startActivity(intent);
//                DatabaseHelper dbHelper2 = new DatabaseHelper(this, "test_db", 2);
//                SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
                break;
            //插入数据
            case R.id.insert:
                //创建存放数据的ContentValues对象
                ContentValues values = new ContentValues();
                //像ContentValues中存放数据
                values.put("id", 1);
                values.put("name","zhangsan");
                DatabaseHelper dbHelper3 = new DatabaseHelper(this, "test_db");
                SQLiteDatabase db3 = dbHelper3.getWritableDatabase();
                //数据库执行插入命令
                db3.insert("user", null, values);
                break;
            //更新数据信息
            case R.id.update:
                DatabaseHelper dbHelper4 = new DatabaseHelper(this, "test_db");
                SQLiteDatabase db4 = dbHelper4.getWritableDatabase();
                ContentValues values2 = new ContentValues();
                values2.put("name", "xiaosan");
                db4.update("user", values2, "id=?", new String[]{"1"});
                break;
            //查询信息
            case R.id.query:
                DatabaseHelper dbHelper5 = new DatabaseHelper(this, "test_db");
                SQLiteDatabase db5 = dbHelper5.getReadableDatabase();
                //创建游标对象
                Cursor cursor = db5.query("user", new String[]{"id","name"}, "id=?", new String[]{"1"}, null, null, null, null);
                //利用游标遍历所有数据对象
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    //日志打印输出
                    Log.i(SWORD, "query-->" + name);
                }
                break;
            //删除记录
            case R.id.delete:
                DatabaseHelper dbHelper6 = new DatabaseHelper(this,"test_db");
                SQLiteDatabase db6 = dbHelper6.getWritableDatabase();
                db6.delete("user", "id=?", new String[]{"1"});
                break;
            default:
                Log.i(SWORD,"error");
                break;
        }
    }
}