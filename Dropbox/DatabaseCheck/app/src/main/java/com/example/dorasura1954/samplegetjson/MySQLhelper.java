package com.example.dorasura1954.samplegetjson;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by okuda on 2016/10/31.
 * SQLiteデータベース作成用アクティビティ
 */
public class MySQLhelper extends SQLiteOpenHelper {

    static final String DB_name = "wkks2.db";    //DBの名前(ファイル名)
    static final int DB_ver = 1;                //DBのバージョン

    //SQL文をString型に保持
    static String Table_name = "sample_Table";
    static final String DROP_TABLE = "drop table mytable;";
    //テーブル作成のSQL文
    final String Create_Table_SQL = "CREATE TABLE "+ Table_name + " ( "+
            "word TEXT , " +
            "num TEXT )";

    //コンストラクタ
    // (コンテクスト[入力は getApplicationContext()もしくはthis],作成するテーブルの名前)
    public MySQLhelper(Context context){

        //コンテクスト,データベースの名前, ??? , バージョン
        super(context, DB_name, null, DB_ver);
    }



    //データベースの作成（自動で起動）
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_SQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}


class Database {
    private SQLiteDatabase read;
    private SQLiteDatabase write;
    private MySQLhelper helper;

    //コンストラクタ
    public Database(Context context) {
        helper = new MySQLhelper(context);
        read = helper.getWritableDatabase();
        write = helper.getReadableDatabase();
    }

    //データベースに入れる値を順番に入れる
    public void writeDB(String input1, String input2) {
        ContentValues cvalue = new ContentValues();
        cvalue.put("word", input1);
        cvalue.put("num", input2);
        write.insert(helper.Table_name, null, cvalue);

    }

    public String readDB() {
        //データの検索結果はCursor型で返される
        // queryメソッドの実行例
        Cursor c = read.query(helper.Table_name, new String[]{"word", "num"}, null,
                null, null, null, null);
        //データベースのデータを読み取って表示する。
        //startManagingCursor(c);
        String str = "データベース一覧\n";
        while (c.moveToNext()) {
            str += c.getString(c.getColumnIndex("word")) + ":" +
                    c.getString(c.getColumnIndex("num")) + "\n";

        }

        return str;
    }

    public void deleteDB(){
        //データベースの削除
        write.delete(helper.Table_name,null,null);
    }

    //str1属性がold_wordの時、その場所のstr2属性値をnew_wordに書き換える
    public void update(String str1, String old_word, String str2, String new_word){
        ContentValues updateValues = new ContentValues();
        updateValues.put(str2, new_word);
        write.update(helper.Table_name, updateValues, str1+"=?", new String[] { old_word });

    }

}
