//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReadNoteActivity extends AppCompatActivity {
    private TextView tvAuthor, tvTitle, tvDescription, tvPublishTime;
    private Button btnQuiz;
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private View displaySectionView;
    private Note note;
    private UserProfile oldUser;
    private DatabaseReference userReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        tvAuthor = findViewById(R.id.readNoteAuthor);
        tvTitle = findViewById(R.id.readNoteTitle);
        tvDescription = findViewById(R.id.readNoteDescription);
        tvPublishTime = findViewById(R.id.readNotePublishTime);
        btnQuiz = findViewById(R.id.btnQuiz);
        parent = findViewById(R.id.read_note_sections);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        note = (Note) getIntent().getSerializableExtra("serializedNoteObject");

        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAuthor.setText(note.getAuthor());
        tvTitle.setText(note.getTitle());
        tvDescription.setText(note.getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tvPublishTime.setText(dateFormat.format(note.getDatetime()));
        ArrayList<String> headings = note.getHeadings();
        ArrayList<String> contents = note.getContents();
        int noOfSection = note.getNoOfSections();
        for(int i=0;i<noOfSection;i++){
            displaySectionView = layoutInflater.inflate(R.layout.display_section, null);
            Typeface heading_typeface = ResourcesCompat.getFont(displaySectionView.getContext(), R.font.acme);
            Typeface content_typeface = ResourcesCompat.getFont(displaySectionView.getContext(), R.font.alice);
            TextView heading = displaySectionView.findViewById(R.id.tvDisplayHeading);
            heading.setTypeface(heading_typeface);
            String temp_heading = headings.get(i);
            heading.setText(temp_heading);
            TextView content = displaySectionView.findViewById(R.id.tvDisplayContent);
            content.setTypeface(content_typeface);
            String temp_content = contents.get(i);
            content.setText(temp_content);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,60);
            parent.addView(displaySectionView, i, params);
        }

        userReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                oldUser = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ReadNoteActivity.this, DoQuizActivity.class);
                intent.putExtra("serializedNoteObject", note);
                startActivity(intent);
            }
        });
    }

    private void deleteUserNote(){
        ArrayList<Note> notes = oldUser.getNotes();
        int currentNoteIndex = 0;
        for(int i=0;i<notes.size();i++){
            if(notes.get(i).getNoteId().contentEquals(note.getNoteId())){
                currentNoteIndex = i;
                break;
            }
        }
        notes.remove(currentNoteIndex);
        UserProfile newUser = new UserProfile(oldUser.getID(), oldUser.getEmail(), oldUser.getName(), oldUser.getPassword(), notes);
        userReference.setValue(newUser);
        Toast.makeText(ReadNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleteNote: deleteUserNote(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}
