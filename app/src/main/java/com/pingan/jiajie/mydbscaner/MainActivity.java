package com.pingan.jiajie.mydbscaner;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private static final String ROOT_PATH = "/";
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;
    private View view;
    private EditText editText;
    private File file;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getDir("", MODE_WORLD_READABLE);
        //显示文件列表
        showFileDir("/data/data/com.pingan.wetalk/");
        Log.e("jj",this.getDir("",MODE_WORLD_READABLE).toString());

    }
    private void showFileDir(String path){
        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        file = new File(path);
        File[] files = file.listFiles();

        //如果当前目录不是根目录
        if (!ROOT_PATH.equals(path)){
            names.add("@1");
            paths.add(ROOT_PATH);

            names.add("@2");
            paths.add(file.getParent());
        }
        //添加所有文件
        for (File f : files){
            names.add(f.getName());
            paths.add(f.getPath());
        }
        this.setListAdapter(new MyAdapter(this, names, paths));
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String path = paths.get(position);
        file = new File(path);
        // 文件存在并可读
        if (file.exists() && file.canRead()){
            if (file.isDirectory()){
                //显示子目录及文件
                showFileDir(path);
            }
            else{
                //处理文件

            }
        }
        //没有权限
        else{
            Resources res = getResources();
            new AlertDialog.Builder(this).setTitle("Message")
                    .setMessage("没有权限")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        super.onListItemClick(l, v, position, id);
    }

    //打开文件
    private void openFile(File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);

        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);
    }
    //获取文件mimetype
    private String getMIMEType(File file){
        String type = "";
        String name = file.getName();
        //文件扩展名
        String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")){
            type = "audio";
        }
        else if(end.equals("mp4") || end.equals("3gp")) {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg") || end.equals("bmp") || end.equals("gif")){
            type = "image";
        }
        else {
            //如果无法直接打开，跳出列表由用户选择
            type = "*";
        }
        type += "/*";
        return type;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (file.getParent()!=null)
            showFileDir(file.getParent());
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
}