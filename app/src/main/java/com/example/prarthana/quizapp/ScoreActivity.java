package com.example.prarthana.quizapp;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScoreActivity extends AppCompatActivity {
    TextView score;
    ArrayList<Score> listOfScores = new ArrayList<>();
    LinearLayout scoresContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.score);
        scoresContainer = findViewById(R.id.scoreView);

        //score.setText("Final Score: "+ getIntent().getIntExtra("score",0));
        getScores();
    }

    private void getScores() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
        DatabaseReference myRef = database.getReference("Scores/"+preferences.getString("id",""));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parseData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void parseData(DataSnapshot dataSnapshot) {

        for(DataSnapshot child: dataSnapshot.getChildren()){
            Score score = child.getValue(Score.class);
            listOfScores.add(score);

        }
        updateScores();
    }

    private void updateScores() {
        for(int i=listOfScores.size()-1; i>=0;i--){
            View view = LayoutInflater.from(this).inflate(R.layout.score_item,null);
            TextView sc= view.findViewById(R.id.sc);
            TextView t= view.findViewById(R.id.t);
            TextView d= view.findViewById(R.id.d);
            sc.setText("Score: " + listOfScores.get(i).Score);
            t.setText("Time: " + listOfScores.get(i).Time);
            d.setText("Date: "+ listOfScores.get(i).Date);
            scoresContainer.addView(view);
        }
    }

    public static class Score{
        String Date;
        String Time;
        String Score;
    }
}
