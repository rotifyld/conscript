package com.example.conscript.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LetterDao {
    @Insert
    void insert(Letter letter);

    @Query("DELETE FROM letter_table")
    void deleteAll();

    @Query("SELECT DISTINCT script from letter_table ORDER BY script ASC")
    LiveData<List<String>> getAllScripts();

    @Query("SELECT * from letter_table")
    LiveData<List<Letter>> getAllLetters();
}
