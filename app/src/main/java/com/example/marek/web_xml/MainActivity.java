package com.example.marek.web_xml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //let's parse some xml
        //for now let's hard-code a string of xml
        String myXML = "<links> <twitter>http://twitter.com</twitter> <fb>http://facebook.com</fb></links>";
        try {
            //get an instance of the XmlPullParserFactory (uses a factory pattern itself!)
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //set the factory to be namespace aware
            factory.setNamespaceAware(true);
            //get an instance of XmlPullParser from the factory
            XmlPullParser xpp = factory.newPullParser();

            //tell the parser where to get data from
            //note we can pass any Reader class to the parser
            //in this case, just do something simple with the string above
            xpp.setInput(new StringReader(myXML));
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

    }
}
