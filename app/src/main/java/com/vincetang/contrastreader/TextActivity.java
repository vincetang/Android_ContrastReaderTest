package com.vincetang.contrastreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView txtData;
    private String rawJSON;
    public JSONArray passages;
    private Button btnDone;

    //JSON Node names
    private static final String TAG_PASSAGES = "passages";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT = "text";
    private static final String TAG_QUESTION_NUMBER = "question_number";
    private static final String TAG_QUESTIONS = "questions";
    private static final String TAG_QUESTION = "question";
    private static final String TAG_ANSWERS = "answers";
    private static final String TAG_CORRECT_ANSWER = "correct_answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        txtData = (TextView) findViewById(R.id.txtData);
        txtData.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String title = extras.getString("title");
            getSupportActionBar().setTitle(title);
            rawJSON = readJSONFile();
            Passage passage = parseJSON(title);
            if (passage != null)
                txtData.setText(passage.text);

        } else {
            Toast.makeText(this, "An Error Ocurred", Toast.LENGTH_LONG).show();
        }
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

    private String readJSONFile() {
        // Read the json file for the text with the given title
        InputStream is = getResources().openRawResource(R.raw.data);
        InputStreamReader isr = new InputStreamReader(is);

        int charRead;
        char[] inputBuffer = new char[500];
        StringBuilder sb = new StringBuilder();

        try {
            while (( charRead = isr.read(inputBuffer)) != -1) { // -1 indicates end of stream
                sb.append(String.copyValueOf(inputBuffer, 0, charRead));
            }
            Log.d("ParseJSON", "JSON read complete: " + sb);
        } catch (IOException e) {
            Log.d("ParseJSON", "IO Exception reading json: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    private Passage parseJSON(String passageTitle) {
        // parse JSON
        if (rawJSON != null) {
            try {
                JSONObject jsonRootObject = new JSONObject(rawJSON);
                passages = jsonRootObject.getJSONArray(TAG_PASSAGES);


                // iterate over passages
                for (int i = 0; i < passages.length(); i++) {
                    JSONObject passage = passages.getJSONObject(i);

                    String title = passage.getString(TAG_TITLE);

                    // only parse the rest if it's the right passage
                    if (title.equalsIgnoreCase(passageTitle)) {
                        String text = passage.getString(TAG_TEXT);
                        // for each passage, create a new array of questions
                        ArrayList<Question> passage_questions = new ArrayList<Question>();

                        JSONArray questions = passage.getJSONArray(TAG_QUESTIONS);
                        for (int j = 0; j < questions.length(); j++) {
                            JSONObject question = questions.getJSONObject(j);

                            String question_number = question.getString(TAG_QUESTION_NUMBER);
                            String askedQuestion = question.getString(TAG_QUESTION);
                            String correctAnswer = question.getString(TAG_CORRECT_ANSWER);

                            // For each question create a new list of answers
                            ArrayList<String> answer_list = new ArrayList<String>();

                            JSONArray answers = question.getJSONArray(TAG_ANSWERS);
                            for (int k = 0; k < answers.length(); k++) {
                                String answer = answers.getString(k);
                                answer_list.add(answer);
                            }
                            Question questionObject = new Question(question_number, askedQuestion, answer_list, correctAnswer);

                            passage_questions.add(questionObject);

                        }
                        return new Passage(title, text, passage_questions);
                    }
                }
            } catch (JSONException e) {
                Log.d("ParseJSON", "Error parsing JSON Object: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                //Load the quiz
                break;
            default:
                break;
        }
    }
}
