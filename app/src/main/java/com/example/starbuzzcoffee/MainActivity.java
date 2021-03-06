package com.example.starbuzzcoffee;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;


public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor favoritesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupOptionsListView();
        setupFavoritesListView();
    }

    private void setupOptionsListView() {
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }

    private void setupFavoritesListView(){
        //Populate the list_favorites ListView from a cursor
        ListView listFavorites = (ListView) findViewById(R.id.favorites);
        try{
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("DRINK", new String[] { "_id", "NAME"}, "FAVORITE = 1",
                    null, null, null, null);
            //if the type is not a object like array of string then you can assign it like favorite.
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            favoritesCursor,
                            new String[]{"NAME"},
                            new int[]{android.R.id.text1}, 0);
/*the cursor is created in the activity onCreate()
method, so it gets its data when the activity is created. When the
user navigates through the other activities, TopLevelActivity is
stopped. It???s not destroyed and recreated, so neither is the cursor. */
            listFavorites.setAdapter(favoriteAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
//Navigate to DrinkActivity if a drink is clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int)id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Cursor newCursor = db.query("DRINK", new String[] { "_id", "NAME"}, "FAVORITE = 1",
        null, null, null, null);
        ListView listFavorites = (ListView) findViewById(R.id.favorites);
        CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
        adapter.changeCursor(newCursor);
        favoritesCursor = newCursor;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
}