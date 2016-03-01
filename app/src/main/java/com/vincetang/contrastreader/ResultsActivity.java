package com.vincetang.contrastreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtResults;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtResults = (TextView) findViewById(R.id.txtResults);
        txtResults.setText(getResults());

        btnDone = (Button) findViewById(R.id.btnBack);
        btnDone.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
                startActivity(upIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private String getResults() {
        String FILENAME = "results_file";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_data");
        File file = new File(myDir, FILENAME);
        StringBuilder results = new StringBuilder();
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    results.append(line);
                    results.append("\n");
                }
                br.close();
                return results.toString();
            } catch (IOException e) {
                Log.e("FILEREADER", "Failed to read in results file." + e.getMessage());
                e.printStackTrace();

            }
        }
        return "No results";
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
