package com.example.sensor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    TextView temp, celcius, percent,hum;
    Button btnstop;
    MediaPlayer mediaPlayer;
    String temperature = "0";
    String humidity= "";
    int temp1;
    int length;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp =(TextView) findViewById(R.id.temp);
        celcius = (TextView) findViewById(R.id.celcius);
        percent = (TextView) findViewById(R.id.percent);
        hum = (TextView) findViewById(R.id.hum);
        btnstop = (Button) findViewById(R.id.btnstop);
        mediaPlayer = MediaPlayer.create(this,R.raw.nhacchuong);
        temp1=Integer.valueOf(temperature);

        content();
        music();

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });
    }
    public void content() {
        new DownloadTextTask().execute("https://viettranchi.000webhostapp.com/sensor.html");

        refreshs(1000);
    }
    private  void refreshs(int miliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }

    public  void music(){
        delay(1000);
        if (i==40){
            mediaPlayer.start();
        }
    }

    private  void delay(int miliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                i++;
                music();
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }

    class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return OpenHttpGETConnection(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("DownloadTextTask", result);
            length = result.length();
            length = result.indexOf("<!-- H");
            temperature = result.substring(16,19);
            humidity= result.substring(48, 51);
            temp.setText(temperature);
            hum.setText(humidity);
        }
    }

    private static String OpenHttpGETConnection(String url){
        StringBuilder content = new StringBuilder();
        try
        {
            URL url1 = new URL(url);
            URLConnection urlConnection = url1.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}