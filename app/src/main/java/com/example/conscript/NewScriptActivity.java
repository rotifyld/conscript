package com.example.conscript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conscript.room.LetterViewModel;

import java.util.List;

public class NewScriptActivity extends AppCompatActivity {

    public static final String NAME_KEY = "com.conscript.android.newscript.extra.NAME";
    public static final String SYMBOLS_LEFT_KEY = "com.conscript.android.newscript.extra.SYMBOLS_LEFT";
    public static final String SYMBOLS_DRAWN_KEY = "com.conscript.android.newscript.extra.SYMBOLS";

    private EditText mName;
    private EditText mSymbols;

    private LetterViewModel mLetterViewModel;
    private List<String> mScripts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_script);

        mName = findViewById(R.id.name);
        mSymbols = findViewById(R.id.symbols);

        LetterViewModel mLetterViewModel = ViewModelProviders.of(this).get(LetterViewModel.class);
        mLetterViewModel.getAllScripts().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> scripts) {
                mScripts = scripts;
            }
        });
    }

    public void nextActivity(View view) {
        String name = mName.getText().toString().replaceAll("\\s+", "");
        String symbols = mSymbols.getText().toString().replaceAll("\\s+", "");
        if (name.isEmpty() || symbols.isEmpty()) {
            Toast toast = Toast.makeText(this, "Both fields must not be empty.", Toast.LENGTH_LONG);
            toast.show();
        } else if (mScripts.contains(name)) {
            Toast toast = Toast.makeText(this, "Script of given name already exists.", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Intent intent = new Intent(this, DrawActivity.class);
            intent.putExtra(NAME_KEY, name);

            intent.putExtra(SYMBOLS_DRAWN_KEY, "");
            intent.putExtra(SYMBOLS_LEFT_KEY, symbols);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }
}
