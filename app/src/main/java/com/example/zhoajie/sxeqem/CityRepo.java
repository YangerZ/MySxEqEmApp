package com.example.zhoajie.sxeqem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class CityRepo {

    SQLiteDatabase db=null;
    public CityRepo(Context context){

        db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

    }

//    public int insert(Student student){
//        //打开连接，写入数据
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put(Student.KEY_age,student.age);
//        values.put(Student.KEY_email,student.email);
//        values.put(Student.KEY_name,student.name);
//        //
//        long student_Id=db.insert(Student.TABLE,null,values);
//        db.close();
//        return (int)student_Id;
//    }

//    public void delete(int student_Id){
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        db.delete(Student.TABLE,Student.KEY_ID+"=?", new String[]{String.valueOf(student_Id)});
//        db.close();
//    }
//    public void update(Student student){
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        ContentValues values=new ContentValues();
//
//        values.put(Student.KEY_age,student.age);
//        values.put(Student.KEY_email,student.email);
//        values.put(Student.KEY_name,student.name);
//
//        db.update(Student.TABLE,values,Student.KEY_ID+"=?",new String[] { String.valueOf(student.student_ID) });
//        db.close();
//    }

    public ArrayList<HashMap<String, String>> getNodeList(){
         db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

        String selectQuery="SELECT "+
                XZQ_NODE.KEY_id+","+
                XZQ_NODE.KEY_name+","+
                XZQ_NODE.KEY_level+","+
                XZQ_NODE.KEY_c_node+","+
                XZQ_NODE.KEY_p_node+","+
                XZQ_NODE.KEY_content +
                " FROM "+XZQ_NODE.TABLE;
        ArrayList<HashMap<String,String>> NodesList=new ArrayList<HashMap<String, String>>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String,String> Nodes=new HashMap<String,String>();
                Nodes.put("nodeid",cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_id)));
                Nodes.put("name",cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_name)));
                NodesList.add(Nodes);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return NodesList;
    }

    /**
     * 根据节点NODEID获取节点具体信息
     * @param curNodeId 节点id
     * @return  返回一个XZQ_NODE内容
     */

    public XZQ_NODE getNodeById(int curNodeId){
        db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

        String selectQuery="SELECT "+
                XZQ_NODE.KEY_id+","+
                XZQ_NODE.KEY_name+","+
                XZQ_NODE.KEY_level+","+
                XZQ_NODE.KEY_c_node+","+
                XZQ_NODE.KEY_p_node+","+
                XZQ_NODE.KEY_content +
                " FROM " + XZQ_NODE.TABLE
                + " WHERE " +
                XZQ_NODE.KEY_c_node + "=?";
        int iCount=0;
        XZQ_NODE xzqNode=null;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(curNodeId)});
        if(cursor.moveToFirst()){
            do{
                xzqNode=new XZQ_NODE();
                xzqNode.id =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_id));
                xzqNode.name =cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_name));
                xzqNode.c_node  =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_c_node));
                xzqNode.p_node =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_p_node));
                xzqNode.level =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_level));
                xzqNode.content  =cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_content));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return xzqNode;
    }

    /**
     * 根据数据等级获取全部节点
     * @param level  数据级别 root 为0
     * @return 返回List Node 集合数据
     */

    public List<XZQ_NODE> getNodesBylevel(int level){
        db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

        String selectQuery="SELECT "+
                XZQ_NODE.KEY_id+","+
                XZQ_NODE.KEY_name+","+
                XZQ_NODE.KEY_level+","+
                XZQ_NODE.KEY_c_node+","+
                XZQ_NODE.KEY_p_node+","+
                XZQ_NODE.KEY_content+
                " FROM " + XZQ_NODE.TABLE
                + " WHERE " +
                XZQ_NODE.KEY_level + "=?";
        int iCount=0;
        XZQ_NODE xzqNode=null;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(level)});
        List<XZQ_NODE> listNODES=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                xzqNode=new XZQ_NODE();
                xzqNode.id =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_id));
                xzqNode.name =cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_name));
                xzqNode.c_node  =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_c_node));
                xzqNode.p_node =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_p_node));
                xzqNode.level =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_level));
                xzqNode.content  =cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_content));
                listNODES.add(xzqNode);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listNODES;
    }

    /**
     * 根据父节点获取去全部子节点
     * @param pId   父节点Id
     * @return 返回List Node 集合数据
     */

    public List<XZQ_NODE> getNodesByParentId(int pId){
        db=SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);

        String selectQuery="SELECT "+
                XZQ_NODE.KEY_id+","+
                XZQ_NODE.KEY_name+","+
                XZQ_NODE.KEY_level+","+
                XZQ_NODE.KEY_c_node+","+
                XZQ_NODE.KEY_p_node+","+
                XZQ_NODE.KEY_content+
                " FROM " + XZQ_NODE.TABLE
                + " WHERE " +
                XZQ_NODE.KEY_p_node + "=?";
        int iCount=0;
        XZQ_NODE xzqNode=null;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(pId)});
        List<XZQ_NODE> listNODES=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                xzqNode=new XZQ_NODE();
                xzqNode.id =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_id));
                xzqNode.name =cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_name));
                xzqNode.c_node  =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_c_node));
                xzqNode.p_node =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_p_node));
                xzqNode.level =cursor.getInt(cursor.getColumnIndex(XZQ_NODE.KEY_level));
                xzqNode.content  =cursor.getString(cursor.getColumnIndex(XZQ_NODE.KEY_content));
                listNODES.add(xzqNode);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listNODES;
    }

}
