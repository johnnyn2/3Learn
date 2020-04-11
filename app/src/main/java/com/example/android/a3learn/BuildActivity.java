//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuildActivity extends AppCompatActivity {
    private EditText title, description;
    private ArrayList<EditText> etHeadings, etContents;
    private ArrayList<String>str_headings, str_contents;
    private Button btnAdd, btnBuild;
    private ArrayList<Button>btnDeletes;
    private int noOfSection;
    private static final int SECTION_NO = 10;
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private View sectionView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);

        getSupportActionBar().setTitle("Build Notes");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.etTitle);
        description = findViewById(R.id.etDescription);
        noOfSection = 1; //counter for the section no.
        btnAdd = findViewById(R.id.btnAdd);
        btnBuild = findViewById(R.id.btnBuild);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BuildActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        parent = findViewById(R.id.inflated_sections);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sectionView = layoutInflater.inflate(R.layout.section, null);
        etHeadings = new ArrayList<EditText>();
        etContents = new ArrayList<EditText>();
        btnDeletes = new ArrayList<Button>();
        Typeface heading_typeface = ResourcesCompat.getFont(sectionView.getContext(), R.font.acme);
        Typeface content_typeface = ResourcesCompat.getFont(sectionView.getContext(), R.font.alice);
        Typeface btnDelete_typeface = ResourcesCompat.getFont(sectionView.getContext(), R.font.nunito_bold);
        TextView tvHeading = sectionView.findViewById(R.id.tvHeading);
        TextView tvContent = sectionView.findViewById(R.id.tvContent);
        tvHeading.setTypeface(heading_typeface);
        tvContent.setTypeface(heading_typeface);
        etHeadings.add((EditText) sectionView.findViewById(R.id.etHeading));
        etHeadings.get(0).setTypeface(content_typeface);
        etContents.add((EditText) sectionView.findViewById(R.id.etContent));
        etContents.get(0).setTypeface(content_typeface);
        btnDeletes.add((Button) sectionView.findViewById(R.id.btnDeleteSection));
        btnDeletes.get(0).setTypeface(btnDelete_typeface);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,60);
        parent.addView(sectionView, 0, params);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etHeadings.size() < SECTION_NO) {
                    sectionView = layoutInflater.inflate(R.layout.section, null);
                    Typeface heading_typeface = ResourcesCompat.getFont(sectionView.getContext(), R.font.acme);
                    Typeface content_typeface = ResourcesCompat.getFont(sectionView.getContext(), R.font.alice);
                    Typeface btnDelete_typeface = ResourcesCompat.getFont(sectionView.getContext(), R.font.nunito_bold);
                    TextView tvHeading = sectionView.findViewById(R.id.tvHeading);
                    TextView tvContent = sectionView.findViewById(R.id.tvContent);
                    tvHeading.setTypeface(heading_typeface);
                    tvContent.setTypeface(heading_typeface);
                    etHeadings.add((EditText) sectionView.findViewById(R.id.etHeading));
                    etHeadings.get(etHeadings.size()-1).setTypeface(content_typeface);
                    etContents.add((EditText) sectionView.findViewById(R.id.etContent));
                    etContents.get(etContents.size()-1).setTypeface(content_typeface);
                    btnDeletes.add((Button) sectionView.findViewById(R.id.btnDeleteSection));
                    btnDeletes.get(btnDeletes.size()-1).setTypeface(btnDelete_typeface);
                    btnDeletes.get(btnDeletes.size() - 1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                int index = btnDeletes.indexOf(v);
                                parent.removeViewAt(index);
                                etHeadings.remove(index);
                                etContents.remove(index);
                                btnDeletes.remove(index);
                                if(etHeadings.size()<SECTION_NO){
                                    btnAdd.setEnabled(true);
                                }
                                noOfSection--;
                        }
                    });
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,60);
                    parent.addView(sectionView, noOfSection, params);
                    noOfSection++;
                }
                if(etHeadings.size() >= SECTION_NO){
                    btnAdd.setEnabled(false);
                }
            }
        });

        btnBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Intent intent = new Intent(BuildActivity.this, BuildQuizActivity.class);
                    setNoteData(intent);
                    startActivity(intent);
                }
            }
        });

        str_headings = new ArrayList<String>();
        str_contents = new ArrayList<String>();

    }

    private void setNoteData(Intent intent){
        String authorName = userProfile.getName();
        String authorID = userProfile.getID();
        String note_title = title.getText().toString();
        String note_description = description.getText().toString();
        for(int i=0;i<etHeadings.size();i++){
            str_headings.add(etHeadings.get(i).getText().toString());
            Log.d("Heading "+ i, str_headings.get(i));
            str_contents.add(etContents.get(i).getText().toString());
            Log.d("Content "+ i, str_contents.get(i));
        }
        int noOfSections = etHeadings.size();
        Note note = new Note(authorName, authorID, note_title, note_description, str_headings, str_contents, noOfSections);
        intent.putExtra("serializedNoteObject", note);
    }

    private boolean validate(){
        boolean isValid = true;
        if(title.getText().toString().isEmpty()){
            Toast.makeText(BuildActivity.this, "Title should not be empty!", Toast.LENGTH_SHORT).show();
            isValid= false;
            return isValid;
        }
        if(description.getText().toString().isEmpty()){
            Toast.makeText(BuildActivity.this, "Description should not be empty!", Toast.LENGTH_SHORT).show();
            isValid= false;
            return isValid;
        }
        for(int i=0;i<etHeadings.size();i++){
            if(etHeadings.get(i).getText().toString().isEmpty()){
                Toast.makeText(BuildActivity.this, "Heading of section "+ (i+1) +" should not be empty!", Toast.LENGTH_SHORT).show();
                isValid = false;
                return isValid;
            }else if(etContents.get(i).getText().toString().isEmpty()){
                Toast.makeText(BuildActivity.this, "Content of section "+ (i+1) +" should not be empty!", Toast.LENGTH_SHORT).show();
                isValid = false;
                return isValid;
            }
        }
        return isValid;
    }


}
