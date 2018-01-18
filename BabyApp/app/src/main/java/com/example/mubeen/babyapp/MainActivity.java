package com.example.mubeen.babyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import br.com.bloder.magic.view.MagicButton;

public class MainActivity extends Activity {
    MagicButton magicButton;
    int timer = 0;
    TextView main2;
    String ip = "192.168.137.58";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        main2 = (TextView) findViewById(R.id.main2);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/DriftType-Regular.ttf");
        main2.setTypeface(custom);


        new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... params) {
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //second async stared within a asynctask but on the main thread

                        (new AsyncTask<String, String, String>() {

                            @Override
                            protected String doInBackground(String... params) {
                                try {

                                    stopmotion("pi", "raspberry", ip, 22);
                                    // this.cancel(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                        }).execute();


                    }
                });
                return null;
            }


        }.execute(1);

    }

    public static String stopmotion(String username, String password, String hostname, int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);
        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();
/*if(!session.isConnected()){
    Log.d("Session Connecct", "OK");
}*/
        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("sudo service motion stop");
        Log.d("test", "Motion Stopped");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }


    //Start Button
    //start button for executing motion detection
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void start(View v) {
            new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //second async stared within a asynctask but on the main thread

                        (new AsyncTask<String, String, String>() {

                            @Override
                            protected String doInBackground(String... params) {
                                try {
                                    startmotion("pi", "raspberry", ip, 22);
                                    startsound("pi", "raspberry", ip, 22);
                                    // this.cancel(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                        }).execute();


                    }
                });
                return null;
            }
        }.execute(1);
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait...!!");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setProgressDrawable(getDrawable(R.drawable.progressbar_states));
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setProgress(60);
        pd.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (timer <= 100) {
                        Thread.sleep(50);
                        pd.setProgress(timer);
                        if (timer == 100) {

                            Intent intent = new Intent(MainActivity.this, Monitoring.class);
                            startActivity(intent);
                            finish();
                        }
                        timer++;
                    }
                } catch (Exception e) {
                }
            }

        });
        thread.start();


    }

    public static String startmotion(String username, String password, String hostname, int port)
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
        channelssh.setCommand("bash /home/pi/pi- reboot/on_reboot.sh");
        Log.d("test", "Motion Function Started");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();

    }

    //sound function
    public static String startsound(String username, String password, String hostname, int port)
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
        channelssh.setCommand("bash /home/pi/pi-reboot/sound.sh");
        Log.d("test", "Sound Function Started");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();

    }


    //Exit button
    //for shutdown raspberry pi on clicking exit button
    public void exit(View v) {

        AlertDialog.Builder builder=new  AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_WithActionBar));
        builder.setTitle("Warning..!!");
        builder.setMessage("It will Shutdown Raspberry pi");

builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            exit("pi", "raspberry", ip, 22);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
                finish();
                System.exit(0);
            }
        });
      builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
      });
        builder.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
builder.create();

        builder.show();

    }

    //Exit button function
    //function for executing poweroff command for raspberry pi
    public static String exit(String username, String password, String hostname, int port)
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
        //channelssh.setCommand("sudo poweroff");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();


    }
public void about_us(View view){
    Intent intent =new Intent(MainActivity.this,About_us.class);
    startActivity(intent);
    finish();
}

}
