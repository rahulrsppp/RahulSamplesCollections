package com.rahulsamples;

import android.content.Context;
import android.widget.ScrollView;

/**
 * Created by Admin on 9/8/2017.
 */

public class MyScroll extends ScrollView {

    MyListener listener;
    public MyScroll(Context context ) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        listener.onDone();

    }

    public void  setListener(MyListener listener){
        this.listener=listener;
    }
}
