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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DoQuizActivity extends AppCompatActivity {
    private ArrayList<TextView> tvQuizQuestionAns;
    private ArrayList<Button> btnQuizShowAns;
    private ViewGroup parent;
    private LayoutInflater layoutInflater;
    private View displayQuestionView;
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_quiz);

        parent = findViewById(R.id.do_quiz_sections);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        note = (Note) getIntent().getSerializableExtra("serializedNoteObject");

        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvQuizQuestionAns = new ArrayList<TextView>();
        btnQuizShowAns = new ArrayList<Button>();
        ArrayList<String>questions = note.getQuestions();
        ArrayList<String>answers = note.getAnswers();

        for(int i=0;i<questions.size();i++){
            displayQuestionView = layoutInflater.inflate(R.layout.quiz, null);
            TextView tvQuizQuestionNo = displayQuestionView.findViewById(R.id.tvQuizQuestionNo);
            TextView tvQuizQuestion = displayQuestionView.findViewById(R.id.tvQuizQuestion);
            tvQuizQuestionAns.add((TextView) displayQuestionView.findViewById(R.id.tvQuizQuestionAns));
            btnQuizShowAns.add((Button) displayQuestionView.findViewById(R.id.btnQuizShowAns));
            Typeface heading_typeface = ResourcesCompat.getFont(displayQuestionView.getContext(), R.font.acme);
            Typeface content_typeface = ResourcesCompat.getFont(displayQuestionView.getContext(), R.font.alice);
            tvQuizQuestionNo.setText("Question "+(i+1));
            tvQuizQuestion.setTypeface(heading_typeface);
            tvQuizQuestion.setText(questions.get(i));
            tvQuizQuestionAns.get(i).setTypeface(content_typeface);
            tvQuizQuestionAns.get(i).setText(answers.get(i));
            btnQuizShowAns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvQuizQuestionAns.get(btnQuizShowAns.indexOf(v)).setVisibility(View.VISIBLE);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,60);
            parent.addView(displayQuestionView, i, params);
        }
    }
}
