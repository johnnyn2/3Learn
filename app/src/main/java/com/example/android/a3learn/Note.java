//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Note implements Serializable {
    private String noteId, author,authorId, title, description;
    private ArrayList<String> headings, contents;
    private ArrayList<String> questions, answers;
    private int noOfSections;
    private Date datetime;

    public Note(){

    }

    public Note(String author, String authorId, String title, String description, ArrayList<String> headings, ArrayList<String> contents, int noOfSections) {
        this.author = author;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.headings = headings;
        this.contents = contents;
        this.noOfSections = noOfSections;
    }

    public Note(String noteId, String author, String authorId, String title, String description, ArrayList<String> headings, ArrayList<String> contents, int noOfSections, Date datetime) {
        this.noteId = noteId;
        this.author = author;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.headings = headings;
        this.contents = contents;
        this.noOfSections = noOfSections;
        this.datetime = datetime;
    }

    public Note(String noteId, String author, String authorId, String title, String description, ArrayList<String> headings, ArrayList<String> contents, ArrayList<String> questions, ArrayList<String> answers, int noOfSections, Date datetime) {
        this.noteId = noteId;
        this.author = author;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.headings = headings;
        this.contents = contents;
        this.questions = questions;
        this.answers = answers;
        this.noOfSections = noOfSections;
        this.datetime = datetime;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getHeadings() {
        return headings;
    }

    public void setHeadings(ArrayList<String> headings) {
        this.headings = headings;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public int getNoOfSections() {
        return noOfSections;
    }

    public void setNoOfSections(int noOfSections) {
        this.noOfSections = noOfSections;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
