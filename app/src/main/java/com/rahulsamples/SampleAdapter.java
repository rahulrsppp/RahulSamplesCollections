
package com.rahulsamples;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * created by vinda on 12/06/2017.
 */

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    private final Activity activity;
    private String sample;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_sample)
        TextView tv_sample;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /*
       Callback to listen the row click
    */
    CallBacksSampleList callBacksSampleList;

    /*
     job list data
  */
    List<String> sampleList;


    public SampleAdapter(Activity activity,List<String> sampleList) {
        this.activity = activity;
        this.sampleList = sampleList;
    }

    /**
     * @param callBacksSampleList:: the callback interface as param
     */
    public void setCallBacksSampleList(CallBacksSampleList callBacksSampleList) {
        this.callBacksSampleList = callBacksSampleList;
    }



    /**
     * @param parent::   the view group
     * @param viewType:: the type
     * @return view holder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sample, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * @param holder::   the holder
     * @param position:: the position of recycler view
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        sample = sampleList.get(position);

        holder.tv_sample.setText(sample);

        setDrawable(holder,holder.getAdapterPosition());

        holder.tv_sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBacksSampleList.onRowClick(sampleList.get(holder.getAdapterPosition()));
            }
        });
    }


    private void setDrawable(ViewHolder holder, int adapterPosition) {

        if(adapterPosition==sampleList.size()) {
            holder.tv_sample.setBackground(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.background_curved_edges_pink,null));
        }else if(adapterPosition %2==0){
            holder.tv_sample.setBackground(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.background_curved_edges_blue,null));

        }else if(adapterPosition %3==0){
            holder.tv_sample.setBackground(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.background_curved_edges_green,null));
        }else if(adapterPosition% 5==0){
            holder.tv_sample.setBackground(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.background_curved_edges_yellow,null));
        }else {
            holder.tv_sample.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.background_curved_edges_red, null));
        }

    }

    /**
     * @return the count of items in recycler view
     */
    @Override
    public int getItemCount() {
        return sampleList.size();
    }

    /**
     * the interface that listens the click on recycler view items
     */
    public interface CallBacksSampleList {
        void onRowClick(String sample);
    }

}
