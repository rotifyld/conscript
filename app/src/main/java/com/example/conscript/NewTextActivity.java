package com.example.conscript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conscript.room.Letter;
import com.example.conscript.room.LetterViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NewTextActivity extends AppCompatActivity {

    private Spinner alphabetSpinner;
    private EditText translationText;
    private TextView availableLetters;

    private List<Letter> allLeters = new ArrayList<>();
    private Map<Character, Bitmap> mDictionary = new HashMap<>();
    private String mScript = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_text);

        alphabetSpinner = findViewById(R.id.spinner);
        translationText = findViewById(R.id.text);
        availableLetters = findViewById(R.id.availableLetters);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>());

        LetterViewModel mLetterViewModel = ViewModelProviders.of(this).get(LetterViewModel.class);
        mLetterViewModel.getAllScripts().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> scripts) {
                adapter.clear();
                adapter.addAll(scripts);
            }
        });
        mLetterViewModel.getAllLetters().observe(this, new Observer<List<Letter>>() {
            @Override
            public void onChanged(List<Letter> letters) {
                allLeters = letters;
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alphabetSpinner.setAdapter(adapter);

        alphabetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<Character, Bitmap> dictionary = new HashMap<>();

                mScript = adapterView.getSelectedItem().toString();

                for (Letter lt : allLeters) {
                    if (lt.getScript().equals(mScript)) {
                        byte[] byteArray = lt.getImage();
                        dictionary.put(lt.getLetter(), BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                    }
                }

                mDictionary = dictionary;
                availableLetters.setHint(dictionary.keySet().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private Bitmap translate(String unfilteredText, Map<Character, Bitmap> dictionary) {

        // filter
        Set<Character> alphabet = dictionary.keySet();
        List<Character> text = new ArrayList<>();
        for (char c : unfilteredText.toCharArray()) {
            if (c == '\n' || c == ' ' || alphabet.contains(c)) text.add(c);
        }

        // compute width/height
        int height = 1;
        int width = 0;
        {
            int currWidth = 0;
            for (char c : text) {
                if (c == '\n') {
                    width = (currWidth > width) ? currWidth : width;
                    currWidth = 0;
                    height++;
                } else {
                    currWidth++;
                }
            }
            width = (currWidth > width) ? currWidth : width;
        }

        // prepare bitmap
        Bitmap randomBitmap = dictionary.entrySet().iterator().next().getValue();
        int panelHeight = randomBitmap.getHeight();
        int panelWidth = randomBitmap.getWidth();
        Bitmap result = Bitmap.createBitmap(panelWidth * width, panelHeight * height, Bitmap.Config.ARGB_8888);
        result.eraseColor(Color.WHITE);

        // put panels
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        int x = 0;
        int y = 0;
        for (char c : text) {
            if (c == ' ') {
                x++;
            } else if (c == '\n') {
                y++;
                x = 0;
            } else {
                canvas.drawBitmap(dictionary.get(c), x * panelWidth, y * panelHeight, paint);
                x++;
            }
        }

        return result;
    }

    private String getTimestamp() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    public void createText(View view) {

        String text = translationText.getText().toString();
        String script = alphabetSpinner.getSelectedItem().toString();


        Bitmap result = translate(text, mDictionary);
        MediaStore.Images.Media.insertImage(getContentResolver(), result, script + getTimestamp(), text);

        Toast toast = Toast.makeText(this, "Text saved to the gallery!", Toast.LENGTH_LONG);
        toast.show();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
