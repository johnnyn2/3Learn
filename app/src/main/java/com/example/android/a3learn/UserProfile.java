//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import java.util.ArrayList;

public class UserProfile {
    private String ID, Email, Name, Password;
    private ArrayList<Note> notes;

    //default constructor for function ordering
    public UserProfile(){

    }

    public UserProfile(String userID, String userEmail, String userName, String userPassword) {
        this.ID = userID;
        this.Email = userEmail;
        this.Name = userName;
        this.Password = userPassword;
    }

    public UserProfile(String userID, String userEmail, String userName, String userPassword, ArrayList<Note>notes){
        this.ID = userID;
        this.Email = userEmail;
        this.Name = userName;
        this.Password = userPassword;
        this.notes = notes;
    }

    public ArrayList<Note> getNotes(){
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
