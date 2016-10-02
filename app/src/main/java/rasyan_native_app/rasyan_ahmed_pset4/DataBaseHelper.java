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
 *
 * This is the helper that regulates all interaction with the database,
 * it sets it up and contains the CRUD methods to modify it.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDB.db";
    private static int DATABASE_VERSION = 1;
    private static final String TABLE = "items";

    private String text_id = "text";
    private String checked_id = "checked";

    // constructor that passes information to the super.
    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creates the Table that stores all the data
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + text_id + " TEXT, " + checked_id + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    // upgrades the database to a new version (not used in this app but required)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        DATABASE_VERSION = newVersion;
        onCreate(db);
    }

    // creates a new entry into the database,
    // the new text is added and its checked status is set to false.
    public void create(String input) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(text_id, input);
        values.put(checked_id, "false");
        db.insert(TABLE, null,values);
        db.close();
    }

    // reads the data from the database and puts it into a arraylist of String hashmaps which is returned.
    public ArrayList<HashMap<String,String>> read() {
        ArrayList<HashMap<String,String>> data = new ArrayList<>();

        // gets the data from database and makes a cursor from it which is looped trough in a do while loop
        SQLiteDatabase db = getReadableDatabase();
        String query =  "SELECT _id , " + text_id + " , " + checked_id + " FROM " + TABLE;
        Cursor cursor = db.rawQuery(query, null);

        // loops trough the cursor and for each item in it it stores its values into a hashmap of Strings,
        // that hashmap is then added to the main data arraylist.
        if (cursor.moveToFirst()){
            do {
                HashMap<String,String> item = new HashMap<>();
                item.put("id", Long.toString(cursor.getLong(cursor.getColumnIndex("_id"))));
                item.put("text", cursor.getString(cursor.getColumnIndex(text_id)));
                item.put("checked", cursor.getString(cursor.getColumnIndex(checked_id)));
                data.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  data;
    }

    // updates the database item identified by a suplied id with
    // the new String and check status stored in ContentValues.
    public void update(long _id, String todo, String check) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(text_id, todo);
        contentValues.put(checked_id, check);
        db.update(TABLE, contentValues, "_id = " + _id, null);
        db.close();
    }

    // delete the entry identified by the suplied id from the table.
    public void delete(long _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = " + _id,null);
        db.close();
    }


}
