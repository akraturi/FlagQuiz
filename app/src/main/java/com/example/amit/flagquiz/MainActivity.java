package com.example.amit.flagquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.CalendarContract.CalendarCache.URI;

public class MainActivity extends AppCompatActivity {

    private String mResult;
    private ArrayList mUrlList=new ArrayList<String>();
    private ArrayList mCountryList=new ArrayList<String>();
    private int mChoosenCountry=0;
    private ImageView mFlagImageView;
    private String mAnswers[]=new String[4];
    private int mCorrectAns=0;
   // private RadioGroup mRadioGroup;
    private Button mRadioButton1,mRadioButton2,mRadioButton3,mRadioButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mRadioGroup=findViewById(R.id.radiogroup);
        mRadioButton1=findViewById(R.id.radiobutton1);
        mRadioButton2=findViewById(R.id.radiobutton2);
        mRadioButton3=findViewById(R.id.radiobutton3);
        mRadioButton4=findViewById(R.id.radiobutton4);
        mFlagImageView = findViewById(R.id.flagimageView);


        DownloadTask downloadTask=new DownloadTask();
        try {
            mResult=downloadTask.execute("http://flagpedia.net/").get();
            Log.i("Results:",mResult);
            String splitResult[]=mResult.split("<h1 class=\"hp\">Flags of countries</h1>");
            Pattern p=Pattern.compile("src=\"(.*?)\"");
            Matcher m=p.matcher(splitResult[1]);
           // mUrlList=new ArrayList<String>();
            while(m.find())
            {
                if(m.group(1).endsWith(".png"))
                {   String temp="http://";
                    for(int i=2;i<m.group(1).length();i++)
                    {
                        temp+=m.group(1).charAt(i);
                    }

                    mUrlList.add(temp);
                }
                //System.out.println(m.group(1));
            }
            p=Pattern.compile("alt=\"(.*?)\"");
            m=p.matcher(splitResult[1]);
            //mCountryList=new ArrayList<String>();
            while(m.find())
            {    mCountryList.add(m.group(1));
                //System.out.println(m.group(1));
            }
            Log.i("countries:,urls:",Integer.toString(mUrlList.size())+" "+Integer.toString(mCountryList.size()));




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        newQuestion();
    }
    public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void click(View view)
    {
         if(view.getTag().toString().equals(Integer.toString(mCorrectAns)))
         {
             Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
         }
         else
         {
             Toast.makeText(getApplicationContext(),"Incorrect! "+"it was"+mAnswers[mCorrectAns],Toast.LENGTH_SHORT).show();
         }
         newQuestion();
    }
    public void newQuestion()
    {   try {
        Random random = new Random();
        mChoosenCountry = random.nextInt(mUrlList.size());
       /* ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap bitmap = imageDownloader.execute(mUrlList.get(mChoosenCountry).toString()).get();
        Log.i("url::", mUrlList.get(mChoosenCountry).toString());
        mFlagImageView.setImageBitmap(bitmap);*/
        Picasso.with(getApplicationContext()).load(mUrlList.get(mChoosenCountry).toString()).into(mFlagImageView);
        mCorrectAns = random.nextInt(4);

        int incorrect=0;
        for (int i = 0; i < 4; i++) {
            if (i == mCorrectAns) {
                mAnswers[i] = mCountryList.get(mChoosenCountry).toString();
            } else {
                incorrect = random.nextInt(mUrlList.size());

                while (incorrect == mCorrectAns) {
                    incorrect = random.nextInt(mUrlList.size());
                }
                mAnswers[i] = mCountryList.get(incorrect).toString();
            }
        }
        mRadioButton1.setText(mAnswers[0]);
        mRadioButton2.setText(mAnswers[1]);
        mRadioButton3.setText(mAnswers[2]);
        mRadioButton4.setText(mAnswers[3]);
    }catch(Exception e)
     {
        e.printStackTrace();
     }
    }
    /*public boolean alreadyPresent(int incorrect)
    {
        for(int i=0;i<4;i++)
        {
            if(mCountryList.get(incorrect)==mAnswers[i])
            {
                return true;
            }
        }
        return false;
    }*/


}
