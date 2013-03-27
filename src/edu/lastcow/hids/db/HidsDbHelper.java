package edu.lastcow.hids.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/13/13
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class HidsDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_WHITE_LIST = "white_list";


    private static final String DATABASE_NAME = "tu_hids.db";
    private static final int DATABASE_VERSION = 1;

    public HidsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v(this.getClass().getName(), "Creating database");
        sqLiteDatabase.execSQL("create table white_list ( _id integer primary key autoincrement, whitename text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
