package com.example.kkccbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button ipButton = findViewById(R.id.ipButton);
        final EditText edit_text   = findViewById(R.id.ipAddress);

        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.serverIP = edit_text.getText().toString();
                Toast.makeText(getApplicationContext(), "New IP: " + Util.serverIP, Toast.LENGTH_SHORT).show();
            }
        });

        ImageView home = (ImageView) findViewById(R.id.settingsHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
