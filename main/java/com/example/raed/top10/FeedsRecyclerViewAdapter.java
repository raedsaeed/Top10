package com.example.raed.top10;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Raed on 08/08/2017.
 */

public class FeedsRecyclerViewAdapter extends RecyclerView.Adapter<FeedsRecyclerViewAdapter.FeedsHolder>{
    private static final String TAG = "FeedsRecyclerViewAdapter";
    private Context mContext;
    private List<FeedEntry> mFeeds;

    public FeedsRecyclerViewAdapter(Context context, List<FeedEntry> feeds) {
        mContext = context;
        mFeeds = feeds;
    }

    @Override
    public FeedsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.browse,parent,false);
        return new FeedsHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedsHolder holder, int position) {
        if((mFeeds == null ) || (mFeeds.size() == 0 )){
            holder.title.setText(R.string.download_failed);
            holder.thumbnail.setImageResource(R.drawable.imageholder);
        }else {
            FeedEntry feedEntry = mFeeds.get(position);
            holder.title.setText(feedEntry.getTitle());
            Picasso.with(mContext).load(feedEntry.getImage())
                    .error(R.drawable.imageholder)
                    .placeholder(R.drawable.imageholder)
                    .into(holder.thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return ((mFeeds != null )&& (mFeeds.size() != 0 )? mFeeds.size() : 1);
    }

    public void loadData (List<FeedEntry> feeds){
        mFeeds = feeds;
        notifyDataSetChanged();
    }

    public FeedEntry getFeed (int position){
        return ((mFeeds != null ) && (mFeeds.size() != 0 ) ? mFeeds.get(position) : null );
    }
    static class FeedsHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FeedsHolder";
        ImageView thumbnail;
        TextView title ;

        public FeedsHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
