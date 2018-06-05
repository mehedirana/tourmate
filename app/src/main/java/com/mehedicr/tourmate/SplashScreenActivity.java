package com.mehedicr.tourmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.splashProgressBar);
        startApp();
/*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        });
        thread.start();*/
    }

    private void startApp() {
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
        }
    }

    public void doWork(){
        for (progress = 20; progress<=100; progress+=20){
            try {
                Thread.sleep(600);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
