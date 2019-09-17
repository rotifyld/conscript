package com.example.conscript.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.ByteArrayOutputStream;

@Database(entities = {Letter.class}, version = 3, exportSchema = false)
public abstract class LetterRoomDatabase extends RoomDatabase {

    public abstract LetterDao letterDao();

    private static LetterRoomDatabase INSTANCE;

    static LetterRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LetterRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LetterRoomDatabase.class, "letter_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LetterDao mDao;

        PopulateDbAsync(LetterRoomDatabase db) {
            mDao = db.letterDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();

            Bitmap blank = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            blank.eraseColor(Color.WHITE);
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            blank.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            try {
                mDao.insert(new Letter("Blank", ' ', byteArray));
            } finally {
            }
            return null;
        }
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

}
