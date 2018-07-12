package com.example.amit.flagquiz;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        String result=null;
        URL url;
        HttpURLConnection urlConnection=null;
        try {
            url=new URL(strings[0]);
            urlConnection=(HttpURLConnection)url.openConnection();
            InputStream in=urlConnection.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            int data;
            while((data=reader.read())>0)
            {
                char current =(char)data;
                result+=current;
                //data=reader.read();
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //MainActivity.mResult=s;
        //Log.i("Results:",s);
    }
}
