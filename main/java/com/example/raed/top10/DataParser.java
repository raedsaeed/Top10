package com.example.raed.top10;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raed on 08/08/2017.
 */

class DataParser extends AsyncTask<String, Void, List<FeedEntry>> implements GetXmlData.DataCallBack {

    public interface OnAvailableApplications{
        void onAvailableApps (List<FeedEntry> applications, DownloadStatus status);
    }
    private static final String TAG = "DataParser";
    private ArrayList<FeedEntry> applications;
    private OnAvailableApplications applicationsCallBack;
    private DownloadStatus mStatus;

    public DataParser(OnAvailableApplications callBack) {
        applications = new ArrayList<>();
        applicationsCallBack = callBack;
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    @Override
    protected void onPostExecute(List<FeedEntry> feedEntries) {
        if(feedEntries != null && mStatus == DownloadStatus.OK){
            applicationsCallBack.onAvailableApps(feedEntries,mStatus);
        }
    }

    @Override
    protected List<FeedEntry> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        GetXmlData getXmlData = new GetXmlData(this);
        getXmlData.runOnSameThread(strings[0]);
        return applications;
    }

    @Override
    public boolean getDataCallBack(String data, DownloadStatus downloadStatus) {
        Log.d(TAG, "getDataCallBack: starts");
        boolean status = true;
        boolean inEntry = false;
        FeedEntry currentEntry = null;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(data));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentEntry = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if (inEntry) {
                            if (tagName.equalsIgnoreCase("entry")) {
                                inEntry = false;
                                applications.add(currentEntry);
                            } else if (tagName.equalsIgnoreCase("title")) {
                                currentEntry.setTitle(textValue);
                            } else if (tagName.equalsIgnoreCase("summary")) {
                                currentEntry.setSummary(textValue);
                            } else if (tagName.equalsIgnoreCase("artist")) {
                                currentEntry.setArtist(textValue);
                            } else if (tagName.equalsIgnoreCase("image")) {
                                currentEntry.setImage(textValue);
                            }
                        }
                        break;
                    default:
                }
              eventType = xmlPullParser.next();
            }
            Log.d(TAG, "getDataCallBack: number of entries"+applications.size());
        } catch (Exception e) {
            Log.e(TAG, "getDataCallBack: Error " + e.getMessage());
            mStatus = DownloadStatus.FAILED_OR_EMPTY;
            status = false;
        }
        mStatus = DownloadStatus.OK ;
        return status;
    }
}





















