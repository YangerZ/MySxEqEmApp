package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zhoajie.sxeqem.DBManager;
import com.example.zhoajie.sxeqem.DEMO_NODE;
import com.example.zhoajie.sxeqem.EarthArchiveNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class ArchiveEarthquake {
    SQLiteDatabase db=null;
    public ArchiveEarthquake(Context context){

        db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

    }

    public List<EarthArchiveNode> getNearArchivelBylevel(double level){
        db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

        String selectQuery="SELECT "+
                EarthArchiveNode.KEY_id+","+
                EarthArchiveNode.KEY_year+","+
                EarthArchiveNode.KEY_date+","+
                EarthArchiveNode.KEY_position+","+
                EarthArchiveNode.KEY_time+","+
                EarthArchiveNode.KEY_level+","+
                EarthArchiveNode.KEY_death1+","+
                EarthArchiveNode.KEY_death2+","+
                EarthArchiveNode.KEY_death3+","+
                EarthArchiveNode.KEY_destroy1+","+
                EarthArchiveNode.KEY_destroy2+","+
                EarthArchiveNode.KEY_destroy3+","+
                EarthArchiveNode.KEY_destroy4+","+
                EarthArchiveNode.KEY_lost+
                " FROM " + EarthArchiveNode.TABLE+
                " WHERE " + EarthArchiveNode.KEY_level+" Between "
                + String.valueOf(level-0.2)+" and "+String.valueOf(level+0.2);
        //" order by  m  limit 0,2";
//                + " WHERE " +
//                DEMO_NODE.KEY_level + "=?";
        int iCount=0;
        EarthArchiveNode demoNode=null;
        Cursor cursor=db.rawQuery(selectQuery,null);
        List<EarthArchiveNode> listNODES=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                demoNode=new EarthArchiveNode();

                demoNode.id =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_id));
                demoNode.year =cursor.getString(cursor.getColumnIndex(EarthArchiveNode.KEY_year));
                demoNode.date  =cursor.getString(cursor.getColumnIndex(EarthArchiveNode.KEY_date));
                demoNode.position=cursor.getString(cursor.getColumnIndex(EarthArchiveNode.KEY_position));
                demoNode.time =cursor.getString(cursor.getColumnIndex(EarthArchiveNode.KEY_time));
                demoNode.level =cursor.getDouble(cursor.getColumnIndex(EarthArchiveNode.KEY_level));
                demoNode.death1  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_death1));
                demoNode.death2  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_death2));
                demoNode.death3  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_death3));

                demoNode.destroy1  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_destroy1));
                demoNode.destroy2  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_destroy2));
                demoNode.destroy3  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_destroy3));
                demoNode.destroy4  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_destroy4));

                demoNode.lost  =cursor.getInt(cursor.getColumnIndex(EarthArchiveNode.KEY_lost));

                listNODES.add(demoNode);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listNODES;
    }
}
