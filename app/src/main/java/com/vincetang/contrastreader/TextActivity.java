package com.vincetang.contrastreader;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TextActivity extends AppCompatActivity {

    public TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtData = (TextView) findViewById(R.id.txtData);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String data = extras.getString("data");
            txtData.setText(data);
            Toast.makeText(this, "The data is : " + data, Toast.LENGTH_LONG);
        }

    }

}
