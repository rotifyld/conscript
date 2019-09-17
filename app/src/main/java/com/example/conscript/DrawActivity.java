package com.example.conscript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conscript.room.Letter;
import com.example.conscript.room.LetterViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.UUID;

import static com.example.conscript.NewScriptActivity.NAME_KEY;
import static com.example.conscript.NewScriptActivity.SYMBOLS_DRAWN_KEY;
import static com.example.conscript.NewScriptActivity.SYMBOLS_LEFT_KEY;

public class DrawActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private LetterViewModel letterViewModel;

    private String currentLetter;
    private String symbolsDrawn;
    private String symbolsLeft;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        letterViewModel = ViewModelProviders.of(this).get(LetterViewModel.class);
        drawingView = findViewById(R.id.draw_view);

        intent = getIntent();
        symbolsDrawn = intent.getStringExtra(SYMBOLS_DRAWN_KEY);
        symbolsLeft = intent.getStringExtra(SYMBOLS_LEFT_KEY);

        assert symbolsLeft != null;
        currentLetter = symbolsLeft.substring(0, 1);
        TextView drawTheLetter = findViewById(R.id.letter);
        drawTheLetter.setText(String.format("Draw the letter > %s <", currentLetter));
    }

    private String trimNotIn(String trimmed, String set) {
        String s = trimmed;
        while (!s.isEmpty()) {
            if (!set.contains(s.substring(0, 1))) {
                return s;
            }
            s = s.substring(1);
        }
        return "";
    }

    public void drawNext(View view) {
        symbolsDrawn = symbolsDrawn + currentLetter;
        symbolsLeft = trimNotIn(symbolsLeft, symbolsDrawn);

        drawingView.setDrawingCacheEnabled(true);
        Bitmap bitmap = drawingView.getDrawingCache();
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();
        drawingView.destroyDrawingCache();

        intent.putExtra(currentLetter, byteArray);
        intent.putExtra(SYMBOLS_DRAWN_KEY, symbolsDrawn);
        intent.putExtra(SYMBOLS_LEFT_KEY, symbolsLeft);

        if (symbolsLeft.isEmpty()) {
            finalizeCreatingAlphabet();

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }

    private void finalizeCreatingAlphabet() {

        // save to db
        String scriptName = intent.getStringExtra(NAME_KEY);
        for (char c : symbolsDrawn.toCharArray()) {
            byte[] image = intent.getByteArrayExtra(c + "");
            assert scriptName != null;
            assert image != null;
            letterViewModel.insert(new Letter(scriptName, c, image));
        }

        Toast toast = Toast.makeText(this, "Successfully created new script!", Toast.LENGTH_LONG);
        toast.show();
    }

    public void draw(View view) {
        drawingView.draw();
    }

    public void erase(View view) {
        drawingView.erase();
    }

    public void clear(View view) {
        drawingView.clear();
    }
}
