//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizActivityAdapter extends RecyclerView.Adapter <QuizActivityAdapter.NoteViewHolder>{
    Context context;
    ArrayList<Note> notes;

    public QuizActivityAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int i) {
        holder.note_title.setText(notes.get(i).getTitle());
        holder.note_description.setText(notes.get(i).getDescription());
        Log.d("Description", notes.get(i).getTitle());
        final Intent intent = new Intent(context, DoQuizActivity.class);
        intent.putExtra("serializedNoteObject", notes.get(i));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView note_title, note_description;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            note_title = itemView.findViewById(R.id.name_text);
            note_description = itemView.findViewById(R.id.status_text);
        }
    }
}
