package com.vincetang.contrastreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView txtData;
    private String rawJSON;
    public JSONArray passages;
    private Button btnDone;
    private  ArrayList<Question> passage_questions;
    private int userScore, level;
//    private boolean contrastOn;
    private String title;
    private long timeStart, timeEnd, time;

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

        // Show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        txtData = (TextView) findViewById(R.id.txtData);
        txtData.setMovementMethod(new ScrollingMovementMethod());

        // Get any information passed in
        Bundle extras = getIntent().getExtras();

        // This is used when the 2nd or later passage is loaded
        if (extras != null) {
            title = extras.getString("title");
            userScore = extras.getInt("userScore");
            level = extras.getInt("level");
//            contrastOn = extras.getBoolean("contrastOn");

            getSupportActionBar().setTitle(title);

            rawJSON = readJSONFile();
            Passage passage = parseJSON(title);
            if (passage != null) {
                switch (level) {
                    case 1: // Level 1: Normal Text
                        txtData.setText(Html.fromHtml(passage.text).toString());
                        break;
                    case 2: // Level 2: Bold Keywords + Normal Text
                        txtData.setText(Html.fromHtml(passage.text));
                        break;
                    case 3: // Level 3: Bold Keywords Only
                        // Match all spans with class "keyword" and insert display:non
                        //String pattern_old = "(<span\\s+.*?class=\"keywords\".*?)(>)";
                        String pattern = "<span\\s+.*?class=\"nonkeywords\".*?>.*?</span>";
                        Pattern p = Pattern.compile(pattern);
                        Matcher m = p.matcher(passage.text);

                        if (m.find()) {
                            //String result = m.replaceAll("\n" + m.group(1) + " style=\"visibility:hidden;display:none\"" + ">");
                            String result = m.replaceAll("<br><br>");
                            txtData.setText(Html.fromHtml(result));
                        } else {
                            // no keywords - just set normal text with bolded keywords
                            txtData.setText(Html.fromHtml(passage.text));
                        }
                        break;

                }
            }

        } else {
            Toast.makeText(this, "An Error Ocurred", Toast.LENGTH_LONG).show();
        }

        String message;
        // Message for dialog box to start timer and let user begin
        if (level == 2) {
            message = "You have completed the first passage! This is the second exercise.\n\n" +
                    "Press Start when you're ready to begin reading.\n\n" +
                    "Press Done at the bottom of the screen when you have finished reading.";
        } else if (level == 3) {
            message = "You have completed the second passage! This is the last exercise.\n\n" +
                    "Press Start when you're ready to begin reading.\n\n" +
                    "Press Done at the bottom of the screen when you have finished reading.\n\n";
        } else {
            message = "Welcome to the reading exercise!\n" +
                    "This is the first of three passages.\n\n" +
                    "Press Start when you're ready to begin reading.\n\n" +
                    "Press Done at the bottom of the screen when you have finished reading.";
        }

        // Dialog box that prompts user and starts timer for reading
        new AlertDialog.Builder(this)
                .setTitle("Passage " + level + " of 3")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeStart = System.currentTimeMillis();
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Back button returns to MainActivity
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
                startActivity(upIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This function reads in a JSON file named data (raw resource) and returns
     * it as a string
     * @return sb: the JSON data represented as a string
     */
    private String readJSONFile() {
        // Read in the JSON file and write it to a string
        InputStream is = getResources().openRawResource(R.raw.data2);
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

    /**
     * Reads JSON data and grabs the node that is a passage with the title passed in. This
     * method reads in the JSON objects and creates a Passage object
     * @param passageTitle the title of the passage to get text from
     * @return a Passage object with the title passed in
     */
    private Passage parseJSON(String passageTitle) {
        // parse JSON
        if (rawJSON != null) {
            try {
                JSONObject jsonRootObject = new JSONObject(rawJSON);
                passages = jsonRootObject.getJSONArray(TAG_PASSAGES);


                // iterate over passages
                for (int i = 0; i < passages.length(); i++) {
                    JSONObject passage = passages.getJSONObject(i);

                    title = passage.getString(TAG_TITLE);

                    // only parse the rest if it's the right passage
                    if (title.equalsIgnoreCase(passageTitle)) {
                        String text = passage.getString(TAG_TEXT);
                        // for each passage, create a new array of questions
                        passage_questions = new ArrayList<Question>();

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
                // Reader has completed the passage
                // Stop the timer and start the quiz in QuizActivity
                timeEnd = System.currentTimeMillis();
                time = timeEnd - timeStart;
                // pass questions to QuizActivity
                Intent intent = new Intent(TextActivity.this, QuizActivity.class);
                intent.putParcelableArrayListExtra("questions", passage_questions);
                intent.putExtra("level", level);
                intent.putExtra("title",title);
//                intent.putExtra("contrastOn", contrastOn);
                intent.putExtra("time", time);
                startActivity(intent);

                break;
            default:
                break;
        }
    }
}
