package com.example.mubeen.babyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

public class Video_Streaming extends Activity {
Button button;
    WebView webView;
    String ip = "192.168.137.58";
    String piAddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video__streaming);
        button=(Button)findViewById(R.id.button);


          final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                piAddr = "http://"+ip+":8081/";
                webView = (WebView) findViewById(R.id.webView);
                webView.loadUrl(piAddr);
                webView.reload();
            }
        },2500);

    }

    public void home(View v){

        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
