package com.vincetang.contrastreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Question> questions;
    private RadioGroup rgAnswers;
    private TextView tvQuestionNumber;
    private TextView tvQuestion;

    private boolean contrastOn;
    private String correctAnswer;
    private int user_score;
    private int skippedCount;
    private int questionIndex;
    private int numQuestions;
    private int level;
    private String title, nextTitle;
    private long time;

    public Button btnSubmit;
    public Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        btnSubmit.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        questions = getIntent().getParcelableArrayListExtra("questions");
        rgAnswers = (RadioGroup) findViewById(R.id.rgAnswers);

        tvQuestionNumber = (TextView) findViewById(R.id.txtQuestionNumber);
        tvQuestion = (TextView) findViewById(R.id.txtQuestion);

        numQuestions = questions.size();

        skippedCount = 0;
        user_score = 0;
        questionIndex = 0;

        Bundle extra = getIntent().getExtras();
        level = extra.getInt("level");
        title = extra.getString("title");
//        contrastOn = extra.getBoolean("contrastOn");
        time = extra.getLong("time");

        getSupportActionBar().setTitle(title + " Quiz");

        showNextQuestion();
    }

    private void showNextQuestion() {
        if (questionIndex < numQuestions) {

            // last question
            btnSubmit.setEnabled(false);
            if (questionIndex == numQuestions - 1) {
                btnSubmit.setText(R.string.btnCompleteQuiz);
            }
            rgAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (!btnSubmit.isEnabled() &&
                            rgAnswers.getCheckedRadioButtonId() != -1) {
                        btnSubmit.setEnabled(true);
                    }
                }
            });
            rgAnswers.removeAllViews();
            String txtQuestionNumber = questions.get(questionIndex).getTitle();
            String txtQuestionText = questions.get(questionIndex).getQuestion();
            correctAnswer = questions.get(questionIndex).getCorrectAnswer();

            tvQuestionNumber.setText(txtQuestionNumber);
            tvQuestion.setText(txtQuestionText);

            addAnswers();

            questionIndex++;
        } else {
            completeQuiz();
        }
    }

    private void completeQuiz() {
        String strMode;

        /*if (contrastOn) {
            strMode = "Contrast On";
        } else {
            strMode = "Contrast Off";
        }*/

        if (level == 1) {
            strMode = "Normal Text";
        } else if (level == 2) {
            strMode = "Bold Keywords + Normal Text";
        } else {
            strMode = "Keywords Only";
        }

        double timeSeconds = time/1000.00;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d-y-HH:mm:ss");
        Date now = new Date();
        String strDate = dateFormat.format(now);

        String result = "Date: " + strDate + "\nPassage: " + title + "\nMode: " + strMode + "\nRead Time (sec): " +
                timeSeconds + "\nQuiz score: " + user_score + "/" + numQuestions + "\nSkipped: " + skippedCount + "\n\n";
        Log.v("Quiz Result", result);

        WriteData writeData = new WriteData();
        writeData.execute(result);
//        Log.d("WRITEFILE", "File written to: " + getFilesDir().toString());

        if (level < 3) {
            level++;

            switch (title.toLowerCase()) {

                //data2.json
                case "the social function of science":
                    nextTitle = "coming of age in samoa";
                    break;
                case "coming of age in samoa":
                    nextTitle = "florence nightingale";
                    break;
                case "florence nightingale":
                    nextTitle = "the social function of science";
                    break;

                // data.json
                case "dolphins":
                    nextTitle = "Opera";
                    break;
                case "opera":
                    nextTitle = "Unsinkable Ship";
                    break;
                case "unsinkable ship":
                    nextTitle = "Dolphins";
                    break;
                case "erosion in america":
                    nextTitle = "Dolphins";
                    break;

            }

            contrastOn = !contrastOn; //don't need this
            Intent intent = new Intent(QuizActivity.this, TextActivity.class);
            intent.putExtra("title", nextTitle);
            intent.putExtra("level", level);
            intent.putExtra("contrastOn", contrastOn);
            startActivity(intent);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Thank you!")
                    .setMessage("You have completed the experiment.\n\nPlease return this device " +
                            "to the experimenter.\n\n Thank you for your participation!")
                    .setCancelable(false)
                    .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(QuizActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                        }
                    }).show();
        }
    }

    private void addAnswers() {
        ArrayList<String> answers = questions.get(questionIndex).getAnswers();
        for (int j = 0; j < answers.size(); j++) {
            addRadioButtonAnswer(answers.get(j));
        }
    }

    private void addRadioButtonAnswer(String answer) {
        RadioButton btnAnswer = new RadioButton(this);
        btnAnswer.setTextSize(18);
        btnAnswer.setText(answer);
        rgAnswers.addView(btnAnswer);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                checkAnswer();
                break;
            case R.id.btnSkip:
                skipQuestion();
                break;
        }
    }

    private void checkAnswer() {
        RadioButton btnAns = (RadioButton) findViewById(rgAnswers.getCheckedRadioButtonId());
        String ans = (String) btnAns.getText();

        if (ans.equalsIgnoreCase(correctAnswer) && user_score < numQuestions) {
                user_score++;
        }
        showNextQuestion();
    }

    private void skipQuestion() {
        new AlertDialog.Builder(this)
                .setTitle("Skip Question")
                .setMessage("Are you sure you want to skip this question?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skippedCount++;
                        showNextQuestion();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public class WriteData extends AsyncTask<String, Void, String> {

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("FILEOUTPUT", "Completed writing to file.");
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected String doInBackground(String... params) {
            String FILENAME = "results_file";
            String strResult = params[0];

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_data");
            myDir.mkdirs();

            File file = new File(myDir, FILENAME);
            try {
                FileWriter fos = new FileWriter(file, true);
                fos.append(strResult);
                Log.d("FILEOUTPUT", strResult);
                Log.d("WRITEFILE", "File written");
                fos.close();
            } catch (FileNotFoundException e) {
                Log.e("FILEOUTPUT", "Could not open file: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("FILEOUTPUT", "Could not write to file: " + e.getMessage());
                e.printStackTrace();
            }

            return strResult;
        }
    }
}
