package com.example.prarthana.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    ArrayList<Quiz> listOfQuiz = new ArrayList<>();

    TextView timer;
    int Time;
    Timer t;
    TimerTask timerTask;

    TextView question;
    RadioGroup radioGroup;
    RadioButton op1;
    RadioButton op2;
    RadioButton op3;
    RadioButton op4;

    ProgressBar progressBar;
    LinearLayout container;

    TextView next;
    TextView endQuiz;

    String myAnswer=null;

    int quizPosition=0;
    int score=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        timer = findViewById(R.id.timer);
 //       startTimer();

        question = findViewById(R.id.question);
        radioGroup = findViewById(R.id.radioGroup);
        op1= findViewById(R.id.option1);
        op2=findViewById(R.id.option2);
        op3= findViewById(R.id.option3);
        op4=findViewById(R.id.option4);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        container = findViewById(R.id.container);
        container.setVisibility(View.GONE);

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.cancel();
                if(myAnswer!=null && listOfQuiz.get(quizPosition).answer.equals(myAnswer)){
                    score++;

                }
                quizPosition++;
                if(quizPosition<listOfQuiz.size()){

                    radioGroup.clearCheck();
                    startQuiz();

                }
                else{
                    endTest();
                }
            }
        });
        endQuiz = findViewById(R.id.end);

        
        getQuestions();
    }

    private void startQuiz() {
        question.setText(listOfQuiz.get(quizPosition).question);
        op1.setText(listOfQuiz.get(quizPosition).option1);
        op2.setText(listOfQuiz.get(quizPosition).option2);
        op3.setText(listOfQuiz.get(quizPosition).option3);
        op4.setText(listOfQuiz.get(quizPosition).option4);

        startTimer();

    }

    private void startTimer() {
        Time=20;
        t = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.setText("Time Left:"+Time);
                        if(Time>0){
                            Time--;
                        }
                        else{
                            t.cancel();
                            if(myAnswer!=null && listOfQuiz.get(quizPosition).answer.equals(myAnswer)){
                                score++;
                            }
                            quizPosition++;
                            if(quizPosition<listOfQuiz.size()){
                                radioGroup.clearCheck();
                                startQuiz();
                            }
                            else{
                                endTest();
                            }
                            
                        }
                    }
                });

            }
        };
        t.schedule(timerTask,0,1000);
    }

    private void endTest() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
        DatabaseReference myRef = database.getReference("Scores/"+ preferences.getString("id",""));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());

        SimpleDateFormat currentDate= new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);


        Map<String,String> map = new HashMap<>();
        map.put("Date",thisDate);
        map.put("Time",strDate);
        map.put("Score", String.valueOf(score));

        myRef.push().setValue(map);


        Intent intent = new Intent(QuizActivity.this,ScoreActivity.class);
        intent.putExtra("score",score);
        startActivity(intent);
        finish();
        //Log.e("Score", String.valueOf(score));

    }


    private void getQuestions() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("qUIZ");
        
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
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);

        for(DataSnapshot child: dataSnapshot.getChildren()){
            Quiz quiz=child.getValue(Quiz.class);
            listOfQuiz.add(quiz);
          //  Log.e("abc","def");
        }
        startQuiz();
    }

    public void onNextButtonClicked(View view) {
        int id=view.getId();
        switch (id){
            case R.id.option1:
                myAnswer="option1";
                break;
            case R.id.option2:
                myAnswer="option2";
                break;
            case R.id.option3:
                myAnswer="option3";
                break;
            case R.id.option4:
                myAnswer="option4";
                break;


        }
    }

    public static class Quiz{
        String question;
        String option1;
        String option2;
        String option3;
        String option4;
        String answer;
    }
}
