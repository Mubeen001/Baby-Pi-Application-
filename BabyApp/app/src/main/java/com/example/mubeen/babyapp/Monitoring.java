package com.example.mubeen.babyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class Monitoring extends AppCompatActivity {

    DotProgressBar dotProgressBar;
    String ip = "192.168.137.58";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_monitoring);

        dotProgressBar=(DotProgressBar)findViewById(R.id.dot_progress_bar);

// or you can use builder

        new DotProgressBarBuilder(this)
                .setDotAmount(5)
                .setStartColor(Color.BLACK)
                .setAnimationDirection(DotProgressBar.LEFT_DIRECTION)
                .build();


    }
    @Override
    protected void onPause(){
      //  Toast.makeText(this,"onPaused",Toast.LENGTH_SHORT).show();
        this.finish();
        super.onPause();
    }

  //live stream button
    public void watch(View v){

            new AsyncTask<Integer, Void, Void>() {
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        streaming("pi", "raspberry", ip, 22);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(1);
        Intent intent=new Intent(this,Video_Streaming.class);
        startActivity(intent);
        finish();
       // System.exit(0);
    }
   //'stopAll.sh' will stop both motion detection and sound sensor and start live streaming
    public static String streaming(String username,String password,String hostname,int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("bash /home/pi/pi-reboot/stopAll.sh");
        Log.d("test","Streaming Started");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();

    }

    //stop Button
    //stop button for executing command on raspberry pi and move back to main activity
    public void stop(View v){
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    streaming("pi", "raspberry", ip, 22);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();


    }





}
