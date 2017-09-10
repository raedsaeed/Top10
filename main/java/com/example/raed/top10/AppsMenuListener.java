package com.example.raed.top10;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Raed on 09/08/2017.
 */

class AppsMenuListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "AppsMenuListener";

    public interface OnItemPressedListener {
        void onItemClick (View view,int position);
    }

    private OnItemPressedListener mListener;
    private GestureDetectorCompat mDetectorCompat ;

    public AppsMenuListener(Context context, final RecyclerView recyclerView, OnItemPressedListener listener) {
        mListener = listener;
        mDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if(mListener != null && childView != null ){
                    mListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mDetectorCompat != null ){
            boolean result = mDetectorCompat.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent() returned: " + result);
        }
        return false;
    }
}
