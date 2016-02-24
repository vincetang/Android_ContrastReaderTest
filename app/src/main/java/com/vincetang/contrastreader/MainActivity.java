package com.vincetang.contrastreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch modeSwitch;
    private Button btnText1, btnText2, btnText3, btnText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        modeSwitch = (Switch) findViewById(R.id.modeSwitch);

        // Buttons for text passages
        btnText1 = (Button) findViewById(R.id.btnText1);
        btnText2 = (Button) findViewById(R.id.btnText2);
        btnText3 = (Button) findViewById(R.id.btnText3);
        btnText4 = (Button) findViewById(R.id.btnText4);

        modeSwitch.setOnClickListener(this);
        btnText1.setOnClickListener(this);
        btnText2.setOnClickListener(this);
        btnText3.setOnClickListener(this);
        btnText4.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.modeSwitch:
                if (modeSwitch.isChecked())
                    modeSwitch.setText(R.string.contrast_on);
                else
                    modeSwitch.setText(R.string.contrast_off);
                break;
            case R.id.btnText1:
                loadTextActivity((String) btnText1.getText());
                break;
            case R.id.btnText2:
                loadTextActivity((String) btnText2.getText());
                break;
            case R.id.btnText3:
                loadTextActivity((String) btnText3.getText());
                break;
            case R.id.btnText4:
                loadTextActivity((String) btnText4.getText());
                break;
        }
    }

    private void loadTextActivity(String title) {
        Intent intent = new Intent(MainActivity.this, TextActivity.class);
        intent.putExtra("title", title);

        startActivity(intent);
    }

}

