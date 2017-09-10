package com.example.raed.top10;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FeedDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        activateActionBar(true);
        Intent intent = getIntent();
        FeedEntry entry = (FeedEntry) intent.getSerializableExtra(SEND_FEED);
        if(entry != null){

            Resources resources = getResources();
            TextView title = (TextView) findViewById(R.id.feed_title);
            String feedTitle = resources.getString(R.string.feed_title_text,entry.getTitle());
            title.setText(feedTitle);

            TextView artist = (TextView) findViewById(R.id.feed_artist);
            String feedArtist = resources.getString(R.string.feed_artist_text, entry.getArtist());
            artist.setText(feedArtist);

            TextView summary = (TextView) findViewById(R.id.feed_summary);
            String feedSummary = resources.getString(R.string.feed_summary_text, entry.getSummary());
            summary.setText(feedSummary);

            ImageView feedPhoto = (ImageView)findViewById(R.id.feed_image);
            Picasso.with(getApplicationContext()).load(entry.getImage())
                    .error(R.drawable.imageholder)
                    .placeholder(R.drawable.imageholder)
                    .into(feedPhoto);
        }
    }

}
