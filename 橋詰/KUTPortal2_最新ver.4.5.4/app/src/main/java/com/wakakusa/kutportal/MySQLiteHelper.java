package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by okuda on 2016/10/31.
 * SQLiteデータベース作成用アクティビティ
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    static final int DB_ver = 1;                //DBのバージョン

    //SQL文をString型に保持
    static String DB_name = "wakakusa_ver7.db";

    //テーブル作成のSQL文
    final String  userData = "CREATE TABLE student(" +
            "id TEXT,"   +
            "name TEXT," +
            "birth     TEXT,"+
            "adm       TEXT,"+
            "ug        TEXT,"+
            "dpm       TEXT,"+
            "mjr       TEXT,"+
            "sub1      TEXT,"+
            "sub2      TEXT,"+
            "teacher   TEXT,"+
            "address   TEXT,"+
            "mailaddress TEXT )";

    final String  subjectData = "CREATE TABLE course(" +
            "scode     TEXT,"+
            "subject   TEXT,"+
            "daytime   TEXT,"+
            "teacher   TEXT,"+
            "period    TEXT,"+
            "room      TEXT,"+
            "sj        TEXT,"+
            "sjclass   TEXT  )";

    final String  scoreData =   "CREATE TABLE score(" +
            "scode     TEXT,"+
            "score     TEXT,"+
            "year      TEXT,"+
            "teacher   TEXT,"+
            "period    TEXT )";

    final String  testData  =   "CREATE TABLE test("+
            "scode     TEXT,"+
            "test1     TEXT,"+
            "test2     TEXT  )";

    final String  newsData  =   "CREATE TABLE news("+
            "newscode  TEXT,"+
            "day       TEXT,"+
            "adduser   TEXT,"+
            "address   TEXT,"+
            "title     TEXT,"+
            "content   TEXT,"+
            "neclass   TEXT  )";

    final String loginData =    "CREATE TABLE loginData("+
            "realtime  TEXT,"+
            "limittime      TEXT,"+
            "ara       TEXT, " +
            "sessionID  TEXT,"+
            "response  TEXT"+
            ")";

    //コンストラクタ
    // (コンテクスト[入力は getApplicationContext()もしくはthis],データベースファイル名)
    public MySQLiteHelper(Context context){
        //コンテクスト,データベースの名前, ??? , バージョン
        super(context, DB_name, null, DB_ver);
    }

    //データベースの作成（自動で起動）
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userData);
        db.execSQL(subjectData);
        db.execSQL(scoreData);
        db.execSQL(testData);
        db.execSQL(newsData);
        db.execSQL(loginData);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}

class DatabaseWriter {
    //private SQLiteDatabase read;
    SQLiteDatabase write;
    static MySQLiteHelper helper = null;
    String[] property;
    String Table_name;

    final String[] tableName = {"student", "course","score", "test", "news","loginData"};
    final String[] student_property = {"id","name", "birth","adm","ug","dpm","mjr","sub1","sub2","teacher","address","mailaddress"};
    final String[] course_property = {"scode", "subject", "daytime","teacher","period","room","sj","sjclass"};
    final String[] score_property = {"scode","score","year","period"};
    final String[] test_property = {"scode","test1","test2"};
    final String[] news_property = {"newscode","day","adduser","address","title","content","neclass"};
    final String[] time_property = {"realtime","limittime","ara","sessionID","response"};

    //コンストラクタ
    public DatabaseWriter(Context context, String table) {
        Table_name = table;
        helper = new MySQLiteHelper(context);
        write = helper.getWritableDatabase();
        if(Table_name.equals(tableName[0])) property = student_property;
        else if(Table_name.equals(tableName[1])) property = course_property;
        else if(Table_name.equals(tableName[2])) property = score_property;
        else if(Table_name.equals(tableName[3])) property = test_property;
        else if(Table_name.equals(tableName[4])) property = news_property;
        else if(Table_name.equals(tableName[5])) property = time_property;
    }

    //データベースに入れる値を順番に入れる
    public void writeDB(JSONObject obj) throws JSONException {
        ContentValues cvalue = new ContentValues();
        for(String i : property)  cvalue.put(i, obj.getString(i));
        write.insert(this.Table_name, null, cvalue);

    }

    public void deleteDB() {
        //データベースの削除
        write.delete(this.Table_name, null, null);
    }

    //a=属性名　"値"
    public void deleteDB2(String a, String b) {
        //データベースの削除
        write.delete(this.Table_name,a+"= '"+ b + "'", null);
    }

    //str1属性がold_wordの時、その場所のstr2属性値をnew_wordに書き換える
    public void update(String str1, String str2, String old_word, String new_word) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(str2, new_word);
        write.update(this.Table_name, updateValues, str1 + "=?", new String[]{old_word});

    }
}

//データベースの中に格納されているデータを画面に表示されるためのクラス
class DatabaseReader {
    private SQLiteDatabase read;
    private String Table_name;
    static MySQLiteHelper helper = null;

    public DatabaseReader(Context context, String table) {
        Table_name = table;
        helper = new MySQLiteHelper(context);
        read = helper.getWritableDatabase();
    }

    //属性名、数
    public String readDB(String[] table, int n) {
        String num = null;
        if(n != 0) num = String.valueOf(n);

        //データの検索結果はCursor型で返される
        // queryメソッドの実行例
        Cursor c = read.query(this.Table_name, table, null,
                null, null, null, num);
        //データベースのデータを読み取って表示する。
        //startManagingCursor(c);
        String str = "";
        while (c.moveToNext()) {
            for(String i : table)
                str += c.getString(c.getColumnIndex(i)) + "\n";
        }
        c.close();
        return str;

    }

    //table=取り出す属性　where=条件内容 param=条件内容
    public String readDB2(String[] table, String where, String[] param) {

        //データの検索結果はCursor型で返される
        // queryメソッドの実行例
        Cursor c =read.query(this.Table_name,table,where,param,
                null, null, null,null);

        //データベースのデータを読み取って表示する。
        //startManagingCursor(c);

        String str = "";
        while (c.moveToNext()) {
            for(String i : table) str += c.getString(c.getColumnIndex(i))  + "\n";

        }
        c.close();
        return str;
    }

    //item=取り出す属性　where=条件内容 param=条件内容の値　table = "テーブル"
    public String readDB3(String[] item, String where, String[] param, String table) {

        //データの検索結果はCursor型で返される
        // queryメソッドの実行例
        Cursor c =read.query(table,item, where,param,
                null, null, null,null);

        //データベースのデータを読み取って表示する。
        //startManagingCursor(c);

        String str = "";
        while (c.moveToNext()) {
            for(String i : item) str += c.getString(c.getColumnIndex(i))  + "\n";

        }
        c.close();
        return str;
    }

}
