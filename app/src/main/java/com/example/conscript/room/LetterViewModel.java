package com.example.conscript.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LetterViewModel extends AndroidViewModel {

    private LetterRepository mRepository;

    private LiveData<List<Letter>> mAllLetters;

    private LiveData<List<String>> mAllScripts;

    public LetterViewModel(Application application) {
        super(application);
        mRepository = new LetterRepository(application);
        mAllLetters = mRepository.getAllLetters();
        mAllScripts = mRepository.getAllScripts();
    }

    public LiveData<List<Letter>> getAllLetters() {
        return mAllLetters;
    }

    public LiveData<List<String>> getAllScripts() {
        return mAllScripts;
    }

    public void insert(Letter letter) {
        mRepository.insert(letter);
    }
}
