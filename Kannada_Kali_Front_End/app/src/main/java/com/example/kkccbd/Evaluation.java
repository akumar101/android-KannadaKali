package com.example.kkccbd;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Evaluation extends AppCompatActivity {

    String bucket = "kannadakaliresults";
    File downloadFromS3;
    TextView textView;
    StringBuilder text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Evaluation");
        TransferUtility transferUtility = Util.getTransferUtility(getApplicationContext());
        downloadFromS3 = new File(getApplicationContext().getFilesDir().getAbsolutePath(),"res.txt");
        textView = (TextView) findViewById(R.id.test_id);
        TransferObserver transferObserver = transferUtility.download(
                bucket,     /* The bucket to download from */
                "res.txt",    /* The key for the object to download */
                downloadFromS3        /* The file to download the object to */
        );
        transferObserverListener(transferObserver);

    }
    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                if(state == TransferState.COMPLETED) {


                    text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(downloadFromS3));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                    }
                    catch (IOException e) {
                        //You'll need to add proper error handling here
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(text);
                            textView.setTextSize(160);
                        }
                    }, 5000);

                } else {
                    textView.setText("Evaluating");
                    textView.setTextSize(50);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
               // int percentage = (int) (bytesCurrent/bytesTotal * 100);

            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }

        });
    }
}