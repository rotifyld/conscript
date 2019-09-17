package com.example.conscript.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LetterRepository {

    private LetterDao mLetterDao;
    private LiveData<List<Letter>> mAllLetters;
    private LiveData<List<String>> mAllScripts;


    LetterRepository(Application application) {
        LetterRoomDatabase db = LetterRoomDatabase.getDatabase(application);
        mLetterDao = db.letterDao();
        mAllLetters = mLetterDao.getAllLetters();
        mAllScripts = mLetterDao.getAllScripts();
    }

    LiveData<List<Letter>> getAllLetters() {
        return mAllLetters;
    }

    LiveData<List<String>> getAllScripts() {
        return mAllScripts;
    }

    public void insert(Letter letter) {
        new insertAsyncTask(mLetterDao).execute(letter);
    }

    private static class insertAsyncTask extends AsyncTask<Letter, Void, Void> {

        private LetterDao mAsyncTaskDao;

        insertAsyncTask(LetterDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Letter... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

