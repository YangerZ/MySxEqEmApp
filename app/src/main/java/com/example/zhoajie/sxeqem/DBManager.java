package com.example.zhoajie.sxeqem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
/**
 * Created by Administrator on 2017/9/19.
 */

public class DBManager {
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "jzyj.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.example.zhoajie.sxeqem";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置

    private SQLiteDatabase database;
    private Context context;

    DBManager(Context context) {
        this.context = context;
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            File fdb=new File(dbfile);
            //把程序的物理路径下的.db文件通过app重新写入到手机上的数据库
            //会删除之前手机原有的数据库，重新添加一个数据库
            if(fdb.exists())
            {
                boolean isDel= SQLiteDatabase.deleteDatabase(fdb);

            }
            if (!fdb.exists()) {

                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.jzyj); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

//do something else here<br>
    public void closeDatabase() {
        this.database.close();
    }
}
