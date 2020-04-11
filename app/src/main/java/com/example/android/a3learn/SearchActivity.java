//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search Notes");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Notes");


        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        mResultList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                firebaseUserSearch(searchText);

            }
        });

    }

    private void firebaseUserSearch(String searchText) {
        Query firebaseSearchQuery;
        if(searchText.isEmpty())
            firebaseSearchQuery = mUserDatabase.limitToFirst(100);
        else
            firebaseSearchQuery = mUserDatabase.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(firebaseSearchQuery, Note.class).build();

        FirebaseRecyclerAdapter<Note, NotesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Note, NotesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Note model) {
                holder.note_title.setText(model.getTitle());
                holder.note_description.setText(model.getDescription());
                Log.d("Description", model.getTitle());
                final Intent intent = new Intent(SearchActivity.this, NotePreviewActivity.class);
                intent.putExtra("noteID", model.getNoteId());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);
                NotesViewHolder notesViewHolder = new NotesViewHolder(view);
                return notesViewHolder;
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    // View Holder Class

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView note_title, note_description;

        public NotesViewHolder(View itemView) {
            super(itemView);
            note_title = itemView.findViewById(R.id.name_text);
            note_description = itemView.findViewById(R.id.status_text);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SearchKey", mSearchField.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String searchKey = savedInstanceState.getString("SearchKey");
        mSearchField.setText(searchKey);
        firebaseUserSearch(searchKey);
    }
}
