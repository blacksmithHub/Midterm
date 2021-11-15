package com.example.libor.midterm;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    EditText username;
    Button login;
    Intent act;
    Animation onClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.txtName);
        login = (Button) findViewById(R.id.btnLogin);

        onClick = AnimationUtils.loadAnimation(this, R.anim.alpha);

        act = new Intent(MainActivity.this, Game.class);

        login();
        username();
    }
    private void username()
    {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(username.length() != 0) {
                } else {
                    if(username.getText().length() == 0) {
                        username.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void login()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.startAnimation(onClick);
                if(username.length() != 0)
                {
                    act.putExtra("name",username.getText().toString());
                    startActivity(act);
                    MainActivity.super.finish();
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Missing field");
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
                    alert.show();
                }
            }
        });
    }
}
