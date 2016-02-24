package com.vincetang.contrastreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;

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
                    modeSwitch.setText("Contrast Mode On");
                else
                    modeSwitch.setText("Contrast Mode Off");
                break;
            case R.id.btnText1:
                loadTextActivity("Text 1");
                break;
            case R.id.btnText2:
                loadTextActivity("Text 2");
                break;
            case R.id.btnText3:
                loadTextActivity("Text 3");
                break;
            case R.id.btnText4:
                loadTextActivity("Text 4");
                break;
        }
    }

    private void loadTextActivity(String title) {
        Intent intent = new Intent(MainActivity.this, TextActivity.class);
        intent.putExtra("data", title);

        startActivity(intent);
    }
}
