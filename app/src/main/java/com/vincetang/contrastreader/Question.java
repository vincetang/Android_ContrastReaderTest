package com.vincetang.contrastreader;

import java.util.ArrayList;

/**
 * Created by Vince on 16-02-23.
 */
public class Question {
    public String title;
    public String question;
    public ArrayList<String> answers;
    public String correctAnswer;

    public Question(String number, String question, ArrayList<String> answers, String correctAnswer) {
        this.title = number;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
