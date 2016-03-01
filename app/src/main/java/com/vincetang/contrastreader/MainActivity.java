package com.vincetang.contrastreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch modeSwitch;
    private Button btnText1, btnText2, btnText3, btnText4;
    private int level;
    private boolean contrastOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CSC428 Reading Experiment");

        modeSwitch = (Switch) findViewById(R.id.modeSwitch);

        contrastOn = false;

        // Buttons for text passages
        btnText1 = (Button) findViewById(R.id.btnText1);
        btnText2 = (Button) findViewById(R.id.btnText2);
        btnText3 = (Button) findViewById(R.id.btnText3);
        btnText4 = (Button) findViewById(R.id.btnText4);

        // Switch contrast on or off for the first passage
        modeSwitch.setOnClickListener(this);

        // Add click listeners
        btnText1.setOnClickListener(this);
        btnText2.setOnClickListener(this);
        btnText3.setOnClickListener(this);
        btnText4.setOnClickListener(this);

        // Start at level 1 (experiment goes through 2 levels)
        level = 1;
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

        switch (item.getItemId()) {
            // Show results
            case R.id.action_results:
                Log.d("ACTIONBAR", "action_results pressed");
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
                break;

            // Delete results by deleting the results file
            case R.id.action_clear_results:
                Log.d("ACTIONBAR", "action_clear_results pressed");
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Clear Results")
                        .setMessage("Are you sure you want to delete all stored results?")
                        .setPositiveButton("Clear Results", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eraseResults();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Erase the file containing the results
     */
    private void eraseResults() {
        String FILENAME = "results_file";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_data");
        File file = new File(myDir, FILENAME);
        file.delete();

        Toast.makeText(this,"Results deleted", Toast.LENGTH_SHORT).show();
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
                if (modeSwitch.isChecked()) {
                    modeSwitch.setText(R.string.contrast_on);
                    contrastOn = true;
                } else {
                    modeSwitch.setText(R.string.contrast_off);
                    contrastOn = false;
                }
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
            default:
                break;
        }
    }

    /**
     * Loads the text activity showing the passage with the title passed in
     * @param title: title of the passage to load
     */
    private void loadTextActivity(String title) {
        Intent intent = new Intent(MainActivity.this, TextActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("level", level);
        intent.putExtra("contrastOn", contrastOn);
        startActivity(intent);
    }

}

