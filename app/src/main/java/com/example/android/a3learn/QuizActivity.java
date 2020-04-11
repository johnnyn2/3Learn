//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private RecyclerView notesList;
    //private FirebaseRecyclerAdapter<Note, NotesViewHolder> firebaseRecyclerAdapter;
    private ArrayList<Note> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSupportActionBar().setTitle("My Notes");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notesList = findViewById(R.id.quiz_notes_list);
        notesList.setHasFixedSize(true);
        notesList.setLayoutManager(new LinearLayoutManager(this));
        notesList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        notes = new ArrayList<Note>();


        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("notes");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Note note = dataSnapshot1.getValue(Note.class);
                    notes.add(note);
                }
                QuizActivityAdapter adapter = new QuizActivityAdapter(QuizActivity.this, notes);
                notesList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
