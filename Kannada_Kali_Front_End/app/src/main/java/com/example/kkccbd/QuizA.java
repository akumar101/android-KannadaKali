package com.example.kkccbd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizA extends AppCompatActivity {

    String quizStatus = null;
    Integer quizNumber;
    Integer quizScore;
    String correct;
    Context context;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiza);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        quizStatus = intent.getStringExtra(Util.EXTRA_MESSAGE);

        String quizStuff[]= quizStatus.split(" ");

        quizNumber = Integer.parseInt(quizStuff[0]);
        quizScore = Integer.parseInt(quizStuff[1]);

        TextView score = (TextView)findViewById(R.id.quizAScore);
        score.setText("Score: " + String.valueOf(quizScore) + "/" + String.valueOf(quizNumber));


        final List<String> choices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            choices.add(randomNum());
        }
        correct = choices.get(0);
        Collections.shuffle(choices);

        ImageView mainImage = this.findViewById(R.id.quizAMain);
        context = mainImage.getContext();
        id = context.getResources().getIdentifier("image_" + correct, "mipmap", context.getPackageName());
        mainImage.setImageResource(id);



    }

    String randomNum()
    {
        Random rand = new Random();
        int num1 = rand.nextInt(5) + 1;
        int num2 = rand.nextInt(10);

        return String.valueOf(num1 * 1000 + num2);
    }

    void goNext(int i)
    {
        Intent intent;
        Random rand = new Random();
        if(rand.nextInt(2) == 0) {
            intent = new Intent(getApplicationContext(), QuizB.class);
        }
        else {
            intent = new Intent(getApplicationContext(), QuizA.class);
        }        if(i == 1)
        {
            quizScore += 1;
        }
        quizNumber += 1;
        String message = quizNumber + " " + quizScore;
        intent.putExtra(Util.EXTRA_MESSAGE, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
