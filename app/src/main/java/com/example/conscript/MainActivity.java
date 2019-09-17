package com.example.conscript;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String POPUP_TEXT = "(also: <i>con-script</i>, or <i>constructed script</i>) - " +
            "artificially created writing system, usually for artistic purposes. " +
            "The most popular probably being <a href='https://en.wikipedia.org/wiki/Tengwar'>Tengwar</a> created by <b>J. R. R. Tolkien</b>.<br><br>" +
            "In the app you can first create you own writing system, and then generate texts that are automatically exported to the gallery. " +
            "Current version only allows for creation of <b>alphabets</b>. " +
            "It may however, sometime in the future, be expanded to feature creation of abugidas, abjads, syllabaries and logographic writing systems.<br><br>" +
            "If you are interested you may check <a href='https://www.omniglot.com/'>Omniglot</a> - " +
            "online encyclopedia of writing systems and languages and its huge <a href='https://www.omniglot.com/conscripts/conlangs.htm'>list of constructed scripts for constructed languages</a>.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNewScript(View view) {
        Intent intent = new Intent(this, NewScriptActivity.class);
        startActivity(intent);
    }

    public void createNewText(View view) {
        Intent intent = new Intent(this, NewTextActivity.class);
        startActivity(intent);
    }

    public void showPopup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set title for AlertDialog
        builder.setTitle("Conscript");

        //Set body message of Dialog
        builder.setMessage(Html.fromHtml(POPUP_TEXT));

        //// Is dismiss when touching outside?
        builder.setCancelable(true);

        //Negative Button
        builder.setNegativeButton("BACK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
