package com.example.raed.top10;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements DataParser.OnAvailableApplications, AppsMenuListener.OnItemPressedListener{
    private static final String TAG = "MainActivity";
    private String feed_url = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feed_limit = 25;
    private static final String STATE_URL = "stateUrl";
    private static final String STATE_LIMIT = "stateLimit";
    private String cachedUrl = "INVALIDATED";
    private FeedsRecyclerViewAdapter feedsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateActionBar(false);
        if(savedInstanceState != null){
            feed_url = savedInstanceState.getString(STATE_URL);
            feed_limit = savedInstanceState.getInt(STATE_LIMIT);
        }
        onDownload(String.format(feed_url,feed_limit));

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,new LinearLayoutManager(this).getOrientation());
        recycler.addItemDecoration(itemDecoration);
        AppsMenuListener menuListener = new AppsMenuListener(this,recycler,this);
        recycler.addOnItemTouchListener(menuListener);
        feedsAdapter = new FeedsRecyclerViewAdapter(this,new ArrayList<FeedEntry>());
        recycler.setAdapter(feedsAdapter);
        Log.d(TAG, "onCreate: ends");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(feed_limit == 25){
            menu.findItem(R.id.menu_top_25).setChecked(true);
        } else  menu.findItem(R.id.menu_top_50).setChecked(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.menu_free_apps:
                feed_url = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.menu_paid_apps:
                feed_url = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.menu_top_25:
            case R.id.menu_top_50:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    feed_limit = 75 - feed_limit;
                }
                break;
            case R.id.menu_refresh:
                cachedUrl = "INVALIDATED";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        onDownload(String.format(feed_url,feed_limit));
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URL,feed_url);
        outState.putInt(STATE_LIMIT,feed_limit);
        super.onSaveInstanceState(outState);
    }

    private void onDownload(String url){
        Log.d(TAG, "onDownload: starts");
        if(!url.equalsIgnoreCase(cachedUrl)){
            DataParser parser = new DataParser(this);
            parser.execute(url);
            cachedUrl = url;
        }else Log.d(TAG, "onDownload: Url not changing ");
    }

    @Override
    public void onAvailableApps(List<FeedEntry> applications, DownloadStatus status) {
        Log.d(TAG, "onAvailableApps: number of Apps = " + applications.size() + " Status "+ status);
        if(status == DownloadStatus.OK){
            feedsAdapter.loadData(applications);
        }
        else Log.d(TAG, "onAvailableApps: Error downloading apps");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Intent intent = new Intent(this,FeedDetailActivity.class);
        intent.putExtra(SEND_FEED,feedsAdapter.getFeed(position));
        startActivity(intent);
    }
}
