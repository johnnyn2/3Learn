//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
import java.util.Date;

public class NotePreviewActivity extends AppCompatActivity {
    private TextView tvAuthor, tvTitle, tvDescription, tvPublishDate;
    private Button btnGet;
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private View displaySectionView;
    private Note note;
    private UserProfile oldUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_preview);

        getSupportActionBar().setTitle("Note Preview");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAuthor = findViewById(R.id.tvAuthor);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvPublishDate = findViewById(R.id.tvPublishDate);
        btnGet = findViewById(R.id.btnGet);
        parent = findViewById(R.id.fetched_sections);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Bundle extras = getIntent().getExtras();
        String noteID = extras.getString("noteID");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(noteID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                note = dataSnapshot.getValue(Note.class);
                String title = note.getTitle();
                String description = note.getDescription();
                Date publishDate = note.getDatetime();
                String author = note.getAuthor();
                ArrayList<String> headings = note.getHeadings();
                ArrayList<String> contents = note.getContents();
                int noOfSection = note.getNoOfSections();
                tvAuthor.setText(author);
                tvTitle.setText(title);
                tvDescription.setText(description);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tvPublishDate.setText(dateFormat.format(publishDate));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                oldUser = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Note> notes;
                if(oldUser.getNotes()==null){
                    notes = new ArrayList<Note>();
                }else{
                    notes = oldUser.getNotes();
                }
                boolean isContained = false;
                for(int i=0;i<notes.size();i++){
                    if(notes.get(i).getNoteId().contentEquals(note.getNoteId()))
                        isContained = true;
                }
                if(!isContained) {
                    notes.add(note);
                    UserProfile newUser = new UserProfile(oldUser.getID(), oldUser.getEmail(), oldUser.getName(), oldUser.getPassword(), notes);
                    userReference.setValue(newUser);
                }
                Toast.makeText(NotePreviewActivity.this, "Note obtained", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
