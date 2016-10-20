package com.example.marek.web_xml;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    //use me to store headlines
    Vector<String> headlines;
    //similarly for urls.. etc.

    /**
     * this will be called from your other code when you want to perform the
     * task in the background
     */
    private class DownloadAndParseXML extends AsyncTask<URL, Integer, Long >{
        protected Long doInBackground(URL... urls){
            int count = urls.length;
            long num = 0;
            for(int i = 0; i < count; i++){
                //call our downloadAndParse function
                num += downloadAndParse(urls[i]);
                //handle cancelling the download
                if(isCancelled()) break;
            }
            return num;
        }

        /**
         * this gets called by the framework to update your activity on the
         * task progress
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress){
            System.out.println("progress: " + progress[0]);
        }

        /**
         * onPostExecute gets called once your background task finishes
         * here you want to update your UI when the task is done
         * ex. update the headlines list in the lab 6
         */
         protected void onPostExecute(Long result){
             System.out.println("AsyncTask Complete");
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open a URL connection and parse the xml that is returned
        URL url = null;
        try{
            url = new URL("http://www.cbc.ca/cmlink/rss-topstories");
        }catch (Exception e){
            e.printStackTrace();
        }
        //actually start the download by instantiating our AsyncTask and calling execute
        new DownloadAndParseXML().execute(url);

    }

    /**
     * this function is called from our AsyncTask in order to actually download and parse
     * the XML
     * @param url
     * @return
     */
    public long downloadAndParse(URL url){
        //let's parse some xml
        try {
            //open a network socket
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());

            //get an instance of the XmlPullParserFactory (uses a factory pattern itself!)
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //set the factory to be namespace aware
            factory.setNamespaceAware(true);
            //get an instance of XmlPullParser from the factory
            XmlPullParser xpp = factory.newPullParser();

            //tell the parser where to get data from
            //note we can pass any Reader class to the parser
            //in this case, just do something simple with the string above
            xpp.setInput(in);
            //events are how the XmlPullParser communicates information to your method
            int event = xpp.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                //process events
                if(event == XmlPullParser.START_DOCUMENT){
                    System.out.println("Reached the start of the document");
                }else if(event == XmlPullParser.START_TAG){
                    System.out.println("Reached the start of tag:" + xpp.getName());
                }else if(event == XmlPullParser.END_TAG){
                    System.out.println("Reached the end of tag:" + xpp.getName());
                }else if(event == XmlPullParser.TEXT){
                    System.out.println("Found text:" + xpp.getText());
                }

                //get the next event (tag) from the document
                event = xpp.next();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return 1;
    }

}
