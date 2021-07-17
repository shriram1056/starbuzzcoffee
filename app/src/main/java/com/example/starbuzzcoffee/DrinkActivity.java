package com.example.starbuzzcoffee;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = Objects.requireNonNull(getIntent().getExtras()).getInt(EXTRA_DRINKID);
        /* multiple methods:activity has getIntent which returns intent,intent has get extras,
        get extras return bundle ,bundle extends baseBundle,baseBundle has get int*/

        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?", new String[]{Integer.toString(drinkId)}, null, null,
                    null);
            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);// you can use bool like this

                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                CheckBox favorite = findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();
        } /* cursor is where the record are stored here.but you can make it point to a particular record.
             this is true even if there is only one record. */ catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    public void onFavoriteClicked(View view) {
        int drinkId = Objects.requireNonNull(getIntent().getExtras()).getInt(EXTRA_DRINKID);
        new UpdateDrinkTask().execute(drinkId);
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues drinkValues;
// the Integer is something the user passes to the asyncTask
        protected void onPreExecute() {
            CheckBox favorite = findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper starbuzzDatabseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues, "_id=?", new String[]{Integer.toString(drinkId)});
                db.close();
                return true;
            }
            catch(SQLiteException e){
                return false;
            }
        }
        protected void onPostExecute(Boolean success){
            if(!success){
                Toast toast = Toast.makeText(DrinkActivity.this,"Database unavailable",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}