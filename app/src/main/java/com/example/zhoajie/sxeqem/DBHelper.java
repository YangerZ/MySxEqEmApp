package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/9/18.
 */

public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    //每次你对数据表进行编辑，添加时候，你都需要对数据库的版本进行提升

    //数据库版本
    private static final int DATABASE_VERSION=4;

    //数据库名称
    private static final String DATABASE_NAME="jzyj.db";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       //创建数据表
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ XZQ_NODE.TABLE+"("
                +XZQ_NODE.KEY_id+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +XZQ_NODE.KEY_name+" TEXT, "
                +XZQ_NODE.KEY_c_node+" INTEGER, "
                +XZQ_NODE.KEY_p_node+" INTEGER, "
                +XZQ_NODE.KEY_content+" TEXT, "
                +XZQ_NODE.KEY_level+" INTEGER)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+ XZQ_NODE.TABLE);

        //再次创建表
        onCreate(db);
    }
}
