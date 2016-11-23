package com.example.mohamed.movi_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 07/11/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

        private static final int DATABASE_VERSION = 1;
        // Database Name
        private static final String DATABASE_NAME = "MoviesManager";
        // Movies table name
        private static final String TABLE_MOVIES = "movies";

    //columns names
    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_POSTER="poster";
    private static final String KEY_YEAR="year";
    private static final String KEY_RATE="rate";
    private static final String KEY_OVER_VIEW="overview";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        copyDatabase(context,DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MOVIES_TABLE = " CREATE TABLE " + TABLE_MOVIES + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY , " + KEY_TITLE + " TEXT , "
                + KEY_POSTER + " TEXT , " + KEY_YEAR + " TEXT , " + KEY_RATE +
                " TEXT , " + KEY_OVER_VIEW+" TEXT )";


        db.execSQL(CREATE_MOVIES_TABLE);

        Log.e("TABLE NAME ::::::  ",CREATE_MOVIES_TABLE );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        // Create tables again
        onCreate(db);

    }

    public void addMovie(Movi_info movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,movie.getId());
        values.put(KEY_TITLE,movie.getTitle());
        values.put(KEY_POSTER, movie.getPoster());
        Log.e("put path indatabase",movie.getPoster());
        values.put(KEY_YEAR,movie.getRelese_year());
        values.put(KEY_RATE,movie.getUser_rating());
        values.put(KEY_OVER_VIEW, movie.getOver_view());
        // Inserting Row
        db.insert(TABLE_MOVIES, null, values);
        Log.e("data has been","saved");
        db.close(); // Closing database connection
    }

    public Movi_info getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIES, new String[] { KEY_ID,
                        KEY_TITLE, KEY_POSTER,KEY_YEAR,KEY_RATE,KEY_OVER_VIEW }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Movi_info movie = new Movi_info(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),
                cursor.getString(4),cursor.getString(5));
        // return Movie
        return movie;
    }

    public List<String> getAllPosters() {
        List<String> posterList = new ArrayList<>();
        // Select All Query
        Log.e("Enter in get all","posters");
        String selectQuery = "SELECT "+KEY_POSTER+" FROM " + TABLE_MOVIES+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String poster;
                poster=cursor.getString(0);
                // Adding poster path to list
                posterList.add(poster);
                Log.e("favoriet posters",poster);
            } while (cursor.moveToNext());
        }

        // return posters list
        return posterList;
    }
    public List<Integer> getAllID() {
        List<Integer> ids = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int  id;
                id=cursor.getInt(0);
                // Adding poster path to list
                ids.add(id);
            } while (cursor.moveToNext());
        }

        // return posters list
        return ids;
    }
/*
    public void deleteMovi(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    */
    // Copy to sdcard for debug use
    public static void copyDatabase(Context c, String DATABASE_NAME) {
        String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        Log.d("testing", " testing db path " + databasePath);
        Log.d("testing", " testing db exist " + f.exists());

        if (f.exists()) {
            try {

                File directory = new File("/mnt/sdcard/DB_DEBUG");
                if (!directory.exists())
                    directory.mkdir();

                myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME);
                myInput = new FileInputStream(databasePath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
            } catch (Exception e) {
            } finally {
                try {
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

}
