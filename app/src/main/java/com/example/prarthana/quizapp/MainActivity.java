package com.example.prarthana.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity {
    TextView startTest;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.nav_login){
                    String title= (String) item.getTitle();
                    if(title.equals("Logout")){
                        SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
                        SharedPreferences.Editor editor= preferences.edit();
                        editor.putInt("Login",0);
                        editor.clear();
                        editor.commit();
                        checkLogin();
                        item.setTitle("Login");
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }
                }
                else if(id==R.id.score){
                    Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                    startActivity(intent);

                }
                else if(id==R.id.friend){
                    Intent intent = new Intent(MainActivity.this,FriendActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        checkLogin();

        startTest = findViewById(R.id.startTest);
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
                if(preferences.getInt("Login",0)==1){
                    Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }



            }
        });

        FirebaseInstanceId.getInstance().getToken();
    }

    private void checkLogin() {
        SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
        if(preferences.getInt("Login",0)==1){
            View header= navigationView.getHeaderView(0);
            TextView name= header.findViewById(R.id.name);
            TextView email= header.findViewById(R.id.email);
            name.setText(preferences.getString("name",""));
            email.setText(preferences.getString("Email",""));

            Menu menuNav = navigationView.getMenu();
            MenuItem login= menuNav.findItem(R.id.nav_login);

            login.setTitle("Logout");

        }
        else{
            View header= navigationView.getHeaderView(0);
            TextView name= header.findViewById(R.id.name);
            TextView email= header.findViewById(R.id.email);
            name.setText("Name");
            email.setText("Email id");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }
}
