package com.example.raed.top10;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {NOT_INITIALIZED, STARTED, PROCESSING, FAILED_OR_EMPTY, OK}

/**
 * Created by Raed on 05/08/2017.
 */

 class GetXmlData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetXmlData";
    private DataCallBack mCallBack;
    private DownloadStatus mStatus;

    public GetXmlData(DataCallBack dataCallBack){
        mCallBack = dataCallBack;
        mStatus = DownloadStatus.NOT_INITIALIZED;
    }

    interface DataCallBack {
        boolean getDataCallBack (String data, DownloadStatus downloadStatus);
    }

    public void runOnSameThread (String url){
        Log.d(TAG, "runOnSameThread: starts");
        if(url != null){
            String data = doInBackground(url);
            onPostExecute(data);
        }
        Log.d(TAG, "runOnSameThread: ends");
    }
    @Override
    protected void onPostExecute(String s) {
        if(s != null && mStatus == DownloadStatus.OK) {
            mCallBack.getDataCallBack(s, mStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        String data = getRawData(strings[0]);
        if(data == null){
            Log.d(TAG, "doInBackground: Error downloading");
            mStatus = DownloadStatus.FAILED_OR_EMPTY;
            return null;
        }
        mStatus = DownloadStatus.OK;
        return data;
    }


    private String getRawData(String url){
        Log.d(TAG, "getRawData: starts");
        StringBuilder result = new StringBuilder();
        {
            try {
                URL urlPath = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlPath.openConnection();
                mStatus = DownloadStatus.STARTED ;
                int response=connection.getResponseCode();
                Log.d(TAG, "getRawData: response code " + response);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                int charReads ;
                char [] bufferdReads = new char[500];
                while (true){
                    charReads = reader.read(bufferdReads);
                    if(charReads < 0){
                        break;
                    }
                    if(charReads > 0){
                        result.append(String.copyValueOf(bufferdReads,0,charReads));
                    }
                }
                reader.close();
                return result.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "getRawData: Invalid url " + e.getMessage() );
            } catch (IOException e) {
                Log.e(TAG, "getRawData: Read data"+e.getMessage() );
            }catch (SecurityException e){
                Log.e(TAG, "getRawData: Internet permissions "+e.getMessage() );
            }
        }
        mStatus = DownloadStatus.FAILED_OR_EMPTY;
        return  null;
    }
}
