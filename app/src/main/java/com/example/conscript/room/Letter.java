package com.example.conscript.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "letter_table", primaryKeys = {"script", "letter"})
public class Letter {

    @NonNull
    @ColumnInfo(name = "script")
    private String script;

    @NonNull
    @ColumnInfo(name = "letter")
    private char letter;

    @NonNull
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Letter(@NonNull String script, @NonNull char letter, @NonNull byte[] image) {
        this.script = script;
        this.letter = letter;
        this.image = image;
    }

    @NonNull
    public String getScript() {
        return script;
    }

    @NonNull
    public char getLetter() {
        return letter;
    }

    @NonNull
    public byte[] getImage() {
        return image;
    }
}
