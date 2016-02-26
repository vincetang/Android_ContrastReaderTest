package com.vincetang.contrastreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Question> questions;
    private RadioGroup rgAnswers;
    private TextView tvQuestionNumber;
    private TextView tvQuestion;
    private String correctAnswer;
    public int user_score;


    private int questionIndex;
    private int numQuestions;
    public Button btnSubmit;
    public Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quiz");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        btnSubmit.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        questions = getIntent().getParcelableArrayListExtra("questions");
        rgAnswers = (RadioGroup) findViewById(R.id.rgAnswers);

        tvQuestionNumber = (TextView) findViewById(R.id.txtQuestionNumber);
        tvQuestion = (TextView) findViewById(R.id.txtQuestion);

        numQuestions = questions.size();

        user_score = 0;
        questionIndex = 0;

        showNextQuestion();
    }

    private void showNextQuestion() {
        if (questionIndex < numQuestions) {
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
        Toast.makeText(this,"Quiz completed", Toast.LENGTH_LONG).show();
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

        if (ans.equalsIgnoreCase(correctAnswer)) {
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
}
