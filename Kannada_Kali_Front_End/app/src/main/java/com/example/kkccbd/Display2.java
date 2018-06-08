package com.example.kkccbd;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.lang.Math.abs;

public class Display2 extends AppCompatActivity {


    ArrayList<String> data = new ArrayList<String>();
    HashMap<String,String> displaytext = new HashMap<String, String>();
    String identifier;
    int currentIndex = 0;
    String imageName;
    int id;
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    WavRecorder wavRecorder;
    MediaRecorder mediaRecorder;
    TextView textToDisplay;
    String savePath;
    String fileName;
    File file;
    boolean flag = false;
    private TransferUtility transferUtility;
    String line = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_new);
        textToDisplay = (TextView) findViewById(R.id.display_text);
        Intent intent = getIntent();
        identifier= intent.getStringExtra(Util.EXTRA_MESSAGE);

        // imageView.setImageResource(id);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        setDisplaytext();
        if(identifier.equals("animal")) {
            setAnimalData();
            setAbarTitle(ab,"Animals");
        } else if (identifier.equals("bird")) {
            setBirdData();
            setAbarTitle(ab,"Birds");
        } else if (identifier.equals("color")) {
            setColorData();
            setAbarTitle(ab,"Colors");
        } else if (identifier.equals("number")) {
            setNumberData();
            setAbarTitle(ab,"Numbers");
        } else if (identifier.equals("flower")) {
            setFlowerData();
            setAbarTitle(ab,"Flowers");
        } else if (identifier.equals("fruit")) {
            setFruitData();
            setAbarTitle(ab,"Fruits");
        }
        final ImageView imageView = (ImageView) findViewById(R.id.display_picture);




        Context context = imageView.getContext();
        imageName = data.get(0);
        id = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
        imageView.setImageResource(id);
        ImageView rightarrow = (ImageView) findViewById(R.id.rightButton);
        String fetched_text = displaytext.get(imageName);
        textToDisplay.setText(fetched_text);
        rightarrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = imageView.getContext();
                currentIndex = (currentIndex +1) % data.size();
                imageName = data.get(currentIndex);
                String fetched_text = displaytext.get(imageName);
                textToDisplay.setText(fetched_text);
                id = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
                imageView.setImageResource(id);
            }
        });

        ImageView leftarrow = (ImageView) findViewById(R.id.leftButton);
        leftarrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = imageView.getContext();
                currentIndex = (currentIndex + data.size() -1) % data.size();

                imageName = data.get(abs(currentIndex));
                String fetched_text = displaytext.get(imageName);
                textToDisplay.setText(fetched_text);
                id = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
                imageView.setImageResource(id);
            }
        });




        FloatingActionButton speaker = (FloatingActionButton) findViewById(R.id.fab);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.isNetwork(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), "Internet required", Toast.LENGTH_SHORT).show();
                }
                else {
                    String CommonUrl = "https://s3.ap-south-1.amazonaws.com/kannadakalidata/data/";
                    String object = data.get(currentIndex);
                    String Extension = ".wav";
                    String objectURL = CommonUrl+object+Extension;
                    Uri clip = Uri.parse(objectURL);

                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(String.valueOf(clip));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Playing...", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                }
            }
        });


        FloatingActionButton recorder = (FloatingActionButton) findViewById(R.id.recordButton);
        recorder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(flag == true)
                {
                    Toast.makeText(getApplicationContext(), "Already recording!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (!checkPermission())
                {
                    requestPermission();
                }
                else
                {
//                    savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                            idNum + "_" + System.currentTimeMillis() + ".3gp";

                    fileName = "rec_" + imageName + "_" + System.currentTimeMillis() + ".wav";
                    savePath = getApplicationContext().getCacheDir().getAbsolutePath() + "/" + fileName;
                    file = new File(getApplicationContext().getCacheDir(), fileName);
                    wavRecorder = new WavRecorder(savePath);
                    /*mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    mediaRecorder.setOutputFile(savePath);*/

                    try {
                        /*mediaRecorder.prepare();
                        mediaRecorder.start();*/
                        wavRecorder.startRecording();
                        Toast.makeText(getApplicationContext(), "Recording...",
                                Toast.LENGTH_SHORT).show();
                        ImageView img = findViewById(R.id.recordButton);
                        img.setImageResource(R.drawable.stop);
                        flag = true;
                    } catch (IllegalStateException e) {
                        Toast.makeText(getApplicationContext(), "Unable to record",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                return true;
            }
        });

        recorder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (flag == true) {
                    //mediaRecorder.stop();
                    wavRecorder.stopRecording();
                    ImageView img = findViewById(R.id.recordButton);
                    img.setImageResource(R.drawable.recorder);

                    //mediaPlayer = new MediaPlayer();
                    //mediaPlayer.setDataSource(savePath);
                    //mediaPlayer.prepare();

//                        Toast.makeText(getApplicationContext(), "Playing...",
//                                Toast.LENGTH_SHORT).show();
                    //mediaPlayer.start();
                    wavRecorder.startRecording();

                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.display_parent),
                            "Upload?", Snackbar.LENGTH_LONG);
                    mySnackbar.setAction("OK", new Display2.MyUploadListener());
                    mySnackbar.show();

                    flag = false;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Long-press to record",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(Display2.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getApplicationContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+Util.serverIP);
                if(state == TransferState.COMPLETED)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), Evaluation.class);

                            startActivity(intent);
                        }
                    }, 5000);


// Instantiate the RequestQueue.
                }
                //textView.setText("statechange " + state);
                Toast.makeText(getApplicationContext(), "Upload: " + state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/(bytesTotal+1) * 100);

            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
                Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public class MyUploadListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(Util.isNetwork(getApplicationContext()))
            {
                transferUtility = Util.getTransferUtility(getApplicationContext());
                TransferObserver observer = transferUtility.upload(
                        "kannadakaliapp",
                        fileName,
                        file
                );

                transferObserverListener(observer);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Internet required",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setFruitData() {
        data.add("apple");
        data.add("orange");
        data.add("pineapple");
    }

    private void setFlowerData() {
        data.add("hibiscus");
        data.add("jasmine");
        data.add("kankambara");
        data.add("rose");
    }

    private void setNumberData() {
        data.add("number_nine");
        data.add("number_seven");
        data.add("number_two");
    }

    private void setColorData() {
        data.add("purple");
        data.add("orange_color");
        data.add("white");
        data.add("yellow");
    }

    private void setBirdData() {
        data.add("woodpecker");
        data.add("crow");
        data.add("kingfisher");
    }


    private void setAnimalData() {
        data.add("duck");
        data.add("porcupine");
        data.add("wolf");
        data.add("rhinoceros");
    }


    private void setAbarTitle(android.support.v7.app.ActionBar ab,String title) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ab.setTitle(s);
    }

    private void setDisplaytext() {
        displaytext.put("apple","Sebu");
        displaytext.put("orange","Kithale");
        displaytext.put("pineapple","Ananas");
        displaytext.put("duck","Bathukoli");
        displaytext.put("porcupine","Mulluhandi");
        displaytext.put("wolf","Tola");
        displaytext.put("rhinoceros","Khadgambrga");
        displaytext.put("purple","Nerale");
        displaytext.put("orange_color","Kesari");
        displaytext.put("white","Bili");
        displaytext.put("yellow","Haladi");
        displaytext.put("woodpecker","Marakutiga");
        displaytext.put("crow","Kage");
        displaytext.put("kingfisher","Minculi");
        displaytext.put("hibiscus","Dasavala");
        displaytext.put("jasmine","Mallige");
        displaytext.put("kankambara","Kargambraga");
        displaytext.put("rose","Gulabi");
        displaytext.put("number_nine","Ombattu");
        displaytext.put("number_seven","Elu");
        displaytext.put("number_two","Yeredu");
    }

}
