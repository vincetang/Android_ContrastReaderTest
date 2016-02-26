package com.vincetang.contrastreader;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Question> questions;
    private RadioGroup rgAnswers;
    private TextView tvQuestionNumber;
    private TextView tvQuestion;

    private int questionIndex;
    private int numQuestions;
    public Button btnDone;
    public Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quiz");

        btnDone = (Button) findViewById(R.id.btnDone);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        btnDone.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        questions = getIntent().getParcelableArrayListExtra("questions");
        rgAnswers = (RadioGroup) findViewById(R.id.rgAnswers);

        tvQuestionNumber = (TextView) findViewById(R.id.txtQuestionNumber);
        tvQuestion = (TextView) findViewById(R.id.txtQuestion);

        numQuestions = questions.size();
        questionIndex = 0;

        showNextQuestion(questionIndex);
    }

    private void showNextQuestion(int index) {
        if (index < numQuestions) {
            String txtQuestionNumber = questions.get(index).getTitle();
            String txtQuestionText = questions.get(index).getQuestion();

            tvQuestionNumber.setText(txtQuestionNumber);
            tvQuestion.setText(txtQuestionText);

            ArrayList<String> answers = questions.get(index).getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                addRadioButtonAnswer(answers.get(j));
            }
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
            case R.id.btnDone:
                break;
            case R.id.btnSkip:
                break;
        }
    }
}
