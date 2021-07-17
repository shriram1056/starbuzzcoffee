package com.example.starbuzzcoffee;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "Star buzz"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); //the 3rd parameter is related to cursor
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0,DB_VERSION);
    }
/*An empty database is created on the device the first time it needs to be used, and then the SQLite helper’s
 onCreate() method is called.this method has the database created as an argument */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db,oldVersion,DB_VERSION);
    }

    private static void insertDrink(SQLiteDatabase db, String name,
                                    String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
//You usually create a new ContentValues object for each row of data you want to create.
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
        //the middle parameter is usually null and actually, used if you want to assign empty row
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, "
                + "DESCRIPTION TEXT, "
                + "IMAGE_RESOURCE_ID INTEGER);"); /*AUTOINCREMENT means that
            when we store a new row in the table, SQLite will automatically generate a
            unique integer for it. */
        insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte);
        insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam", R.drawable.cappuccino);
        insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter);

        }
        if(oldVersion < 2){
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }
}
//Code to add the extra column
}