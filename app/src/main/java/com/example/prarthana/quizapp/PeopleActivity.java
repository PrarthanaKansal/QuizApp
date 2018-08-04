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

import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity {
    LinearLayout peopleContainer;
    ArrayList<People> listOfPeople = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        peopleContainer = findViewById(R.id.pplView);
        
        getPeople();
    }

    private void getPeople() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//       SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
        DatabaseReference myRef = database.getReference("Login");

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
            PeopleActivity.People people = child.getValue(PeopleActivity.People.class);
            listOfPeople.add(people);

        }
        updatePeople();
    }

    private void updatePeople() {
        for(int i=listOfPeople.size()-1; i>=0;i--){
            View view = LayoutInflater.from(this).inflate(R.layout.score_item,null);
            TextView name= view.findViewById(R.id.name);
            TextView email= view.findViewById(R.id.em);

            name.setText("Name: " + listOfPeople.get(i).name);
            email.setText("Email id: " + listOfPeople.get(i).emailId);
            peopleContainer.addView(view);
        }
    }

    public static class People{
        String name;
        String emailId;
    }
}
