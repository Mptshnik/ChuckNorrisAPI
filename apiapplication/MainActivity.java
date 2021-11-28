package com.example.apiapplication;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity
{
    private Button _button;
    private ImageView _jokeImage;
    private TextView _jokeText;
    private String _joke;
    private String _imageUrl;
    private String _createdAt;
    private String _jokeID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _button = findViewById(R.id.GetJokeButton);
        _jokeText = findViewById(R.id.JokeText);

        _button.setOnClickListener(view -> new JokeLoader().execute());
    }

   private class JokeLoader extends AsyncTask<Void, Void, Void>
   {

       @Override
       protected Void doInBackground(Void... voids)
       {
           String json = getJson("https://api.chucknorris.io/jokes/random");

           try
           {
               JSONObject obj = new JSONObject(json);
               _joke = obj.getString("value");
               _jokeID = obj.getString("id");
               _createdAt = obj.getString("created_at");
               _imageUrl = obj.getString("icon_url");
           }
           catch (JSONException e)
           {
               e.printStackTrace();
           }

           return null;
       }

       @Override
       protected void onPostExecute(Void unused) {
           super.onPostExecute(unused);

           if(!_joke.equals(""))
           {
               _jokeText.setText("Шутка: " + _joke + "\nДата создания: " + _createdAt
                       + "\nID шутки: " + _jokeID);
           }
       }

       @Override
       protected void onPreExecute() {
           super.onPreExecute();

           _joke = "";
           _jokeText.setText("Хохма загружается");
       }

       private String getJson(String link)
       {
           String data = "";

           try
           {
               URL url = new URL(link);
               HttpURLConnection connection = (HttpURLConnection)url.openConnection();

               if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
               {
                   BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                   data = reader.readLine();

                   connection.disconnect();
               }

           }
           catch (IOException e)
           {
               e.printStackTrace();
           }

           return data;
       }


   }

}