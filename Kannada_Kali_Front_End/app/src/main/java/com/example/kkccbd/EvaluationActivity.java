package com.example.kkccbd;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class EvaluationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Evaluation");
        //ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8cff59")));
             // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

    }
}
