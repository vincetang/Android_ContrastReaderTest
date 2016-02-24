package com.vincetang.contrastreader;

import java.util.ArrayList;

/**
 * Created by Vince on 16-02-23.
 */
public class Passage {
    public String title;
    public String text;
    public ArrayList<Question> questions;

    public Passage(String title, String text, ArrayList<Question> questions) {
        this.title = title;
        this.text = text;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
