//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BuildQuizActivity extends AppCompatActivity {
    private Button btnBuildNote, btnAddQuestion;
    private ArrayList<TextView> tvQuestions;
    private ArrayList<EditText> etQuestions, etAnswers;
    private ArrayList<String>str_questions, str_answers;
    private ArrayList<Button>btnDeletes;
    private int noOfQuestion;
    FirebaseDatabase firebaseDatabase;
    Note noteWithoutIDAndTime;
    private static final int QUESTION_NO = 10;
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private View questionView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_quiz);

        firebaseDatabase = FirebaseDatabase.getInstance();

        btnBuildNote = findViewById(R.id.btnBuildNote);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        noOfQuestion = 1;
        parent = findViewById(R.id.inflated_questions);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        questionView = layoutInflater.inflate(R.layout.question, null);
        Typeface heading_typeface = ResourcesCompat.getFont(questionView.getContext(), R.font.acme);
        Typeface content_typeface = ResourcesCompat.getFont(questionView.getContext(), R.font.alice);
        Typeface btnDelete_typeface = ResourcesCompat.getFont(questionView.getContext(), R.font.nunito_bold);
        tvQuestions = new ArrayList<TextView>();
        etQuestions = new ArrayList<EditText>();
        etAnswers = new ArrayList<EditText>();
        btnDeletes = new ArrayList<Button>();
        tvQuestions.add((TextView) questionView.findViewById(R.id.tvQuestion));
        tvQuestions.get(0).setTypeface(heading_typeface);
        etQuestions.add((EditText) questionView.findViewById(R.id.etQuestion));
        etQuestions.get(0).setTypeface(content_typeface);
        TextView tvAnswer = questionView.findViewById(R.id.tvAnswer);
        tvAnswer.setTypeface(heading_typeface);
        etAnswers.add((EditText) questionView.findViewById(R.id.etAnswer));
        etAnswers.get(0).setTypeface(content_typeface);
        btnDeletes.add((Button) questionView.findViewById(R.id.btnDeleteQuestion));
        btnDeletes.get(0).setTypeface(btnDelete_typeface);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,60);
        parent.addView(questionView, 0, params);
        noteWithoutIDAndTime = (Note) getIntent().getSerializableExtra("serializedNoteObject");

        str_questions = new ArrayList<String>();
        str_answers = new ArrayList<String>();

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etQuestions.size() < QUESTION_NO) {
                    questionView = layoutInflater.inflate(R.layout.question, null);
                    Typeface heading_typeface = ResourcesCompat.getFont(questionView.getContext(), R.font.acme);
                    Typeface content_typeface = ResourcesCompat.getFont(questionView.getContext(), R.font.alice);
                    Typeface btnDelete_typeface = ResourcesCompat.getFont(questionView.getContext(), R.font.nunito_bold);
                    tvQuestions.add((TextView) questionView.findViewById(R.id.tvQuestion));
                    tvQuestions.get(tvQuestions.size()-1).setTypeface(heading_typeface);
                    etQuestions.add((EditText) questionView.findViewById(R.id.etQuestion));
                    etQuestions.get(etQuestions.size()-1).setTypeface(content_typeface);
                    TextView tvAnswer = questionView.findViewById(R.id.tvAnswer);
                    tvAnswer.setTypeface(heading_typeface);
                    etAnswers.add((EditText) questionView.findViewById(R.id.etAnswer));
                    etAnswers.get(etAnswers.size()-1).setTypeface(content_typeface);
                    btnDeletes.add((Button) questionView.findViewById(R.id.btnDeleteQuestion));
                    btnDeletes.get(btnDeletes.size()-1).setTypeface(btnDelete_typeface);
                    btnDeletes.get(btnDeletes.size() - 1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index = btnDeletes.indexOf(v);
                            parent.removeViewAt(index);
                            tvQuestions.remove(index);
                            etQuestions.remove(index);
                            etAnswers.remove(index);
                            btnDeletes.remove(index);
                            if(etQuestions.size()<QUESTION_NO){
                                btnAddQuestion.setEnabled(true);
                            }
                            noOfQuestion--;
                            setQuestionNo();
                        }
                    });
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,60);
                    parent.addView(questionView, noOfQuestion, params);
                    noOfQuestion++;
                }
                if(etQuestions.size() >= QUESTION_NO){
                    btnAddQuestion.setEnabled(false);
                }
                setQuestionNo();
            }
        });

        getSupportActionBar().setTitle(noteWithoutIDAndTime.getTitle());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnBuildNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    sendNoteData();
                    Toast.makeText(BuildQuizActivity.this, "Note uploaded!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setQuestionNo();
    }

    private void setQuestionNo(){
        for(int i=0;i<tvQuestions.size();i++){
            tvQuestions.get(i).setText("Question "+(i+1));
        }
    }

    private boolean validate(){
        boolean isValid = true;
        for(int i=0;i<etQuestions.size();i++){
            if(etQuestions.get(i).getText().toString().isEmpty()){
                Toast.makeText(BuildQuizActivity.this, "Question of question "+(i+1)+" should not be empty!", Toast.LENGTH_SHORT).show();
                isValid = false;
                return isValid;
            }else if(etAnswers.get(i).getText().toString().isEmpty()){
                Toast.makeText(BuildQuizActivity.this, "Answer of question "+(i+1)+" should not be empty!", Toast.LENGTH_SHORT).show();
                isValid = false;
                return isValid;
            }
        }
        return isValid;
    }

    private void sendNoteData(){
        DatabaseReference databaseReference =  firebaseDatabase.getReference("Notes");
        String id = databaseReference.push().getKey();
        Date currentTime = Calendar.getInstance().getTime();
        String authorName = noteWithoutIDAndTime.getAuthor();
        String authorID = noteWithoutIDAndTime.getAuthorId();
        String note_title = noteWithoutIDAndTime.getTitle();
        String note_description = noteWithoutIDAndTime.getDescription();
        ArrayList<String>str_headings = noteWithoutIDAndTime.getHeadings();
        ArrayList<String>str_contents = noteWithoutIDAndTime.getContents();
        for(int i=0;i<etQuestions.size();i++){
            str_questions.add(etQuestions.get(i).getText().toString());
            str_answers.add(etAnswers.get(i).getText().toString());
        }
        int noOfSections = noteWithoutIDAndTime.getNoOfSections();
        Note full_version_note = new Note(id, authorName, authorID, note_title, note_description, str_headings, str_contents, str_questions, str_answers, noOfSections, currentTime);
        databaseReference.child(id).setValue(full_version_note);
    }
}
