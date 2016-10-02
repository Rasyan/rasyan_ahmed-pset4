package rasyan_native_app.rasyan_ahmed_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rasyan on 1-10-2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDB.db";
    private static int DATABASE_VERSION = 1;
    private static final String TABLE = "items";

    private String todo_id = "text";

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + name_id + " TEXT, " + phone_id + " TEXT)";
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + todo_id + " TEXT)";
        db.execSQL(CREATE_TABLE);
        System.out.println("test oncreate finished");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        DATABASE_VERSION = newVersion;
        onCreate(db);
    }

    public void create(String input) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todo_id, input);
        db.insert(TABLE, null,values);
        db.close();
    }

    public ArrayList<HashMap<String,String>> read() {
        System.out.println("test read started");
        SQLiteDatabase db = getReadableDatabase();
        String query =  "SELECT _id , " + todo_id + " FROM " + TABLE;
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String,String> item = new HashMap<>();
                item.put("id", Long.toString(cursor.getLong(cursor.getColumnIndex("_id"))));
                item.put("todo", cursor.getString(cursor.getColumnIndex(todo_id)));
                data.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        System.out.println("test read finished");
        return  data;
    }

    public void update(int _id, String todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(todo_id, todo);
        db.update(TABLE, contentValues, "_id = " + String.valueOf(_id), null);
        db.close();
    }

    public void delete(long _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = " + _id,null);

        //db.delete(TABLE, _aID = ?, new String[ ] {String.valueOf(_id)} );

        db.close();
    }

    public void deletAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, null, null);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }


}
