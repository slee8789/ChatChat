package leesc.chatchat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import leesc.chatchat.utils.CommonUtils;

/**
 * Created by Heejeong on 2016-08-23.
 */
public class FriendDB  implements BaseColumns, DB_Constant {

    public static String DATABASE_NAME = "chat_chat.db";
    public static int DATABASE_VERSION = 2;

    public static SQLiteDatabase mDB;
    private static volatile FriendDB dbHandle;
    private static volatile DatabaseHelper dbHelper;


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + FRIEND_TABLE_NAME + " (" + FRIEND_FIELD_FRIEND_ID + " INTEGER PRIMARY KEY NOT NULL,"
                    + FRIEND_FIELD_FRIEND_EMAIL + " TEXT NOT NULL," + FRIEND_FIELD_FRIEND_NAME + " TEXT NOT NULL,"
                    + FRIEND_FIELD_FRIEND_TOKEN + " TEXT," + FRIEND_FIELD_FRIEND_DEVICE_TYPE + " TEXT,"
                    + " FOREIGN KEY (" + FRIEND_FIELD_FRIEND_EMAIL + ") "
                    //+ "REFERENCES " + THREADS_TABLE_NAME + " (" + _ID + ")"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE_NAME);
            onCreate(db);
        }

        public void insert(){
            mDB=getWritableDatabase();
            mDB.execSQL("");
            mDB.close();
        }

        public void update(){
            mDB=getWritableDatabase();
            mDB.execSQL("");
            mDB.close();
        }

        public void delete(){
            mDB=getWritableDatabase();
            mDB.execSQL("");
            mDB.close();
        }
    }

}
