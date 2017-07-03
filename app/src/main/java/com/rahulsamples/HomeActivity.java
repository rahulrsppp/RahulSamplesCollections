package com.rahulsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private static final String LOGTAG = HomeActivity.class.getSimpleName();
    @Bind(R.id.rv_options)
    RecyclerView rv_options;
    private List<String> sampleList;
    private SampleAdapter sampleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAdapter();
        setList();
    }

    private void setList() {
       sampleList.add("Social Login");
        sampleList.add("Brightness Manager");
        sampleList.add("Face Detection");
        sampleAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        sampleList=new ArrayList<>();
        sampleAdapter=new SampleAdapter(this,sampleList);
        rv_options.setAdapter(sampleAdapter);
        rv_options.setHasFixedSize(true);
        rv_options.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        sampleAdapter.setCallBacksSampleList(new SampleAdapter.CallBacksSampleList() {
            @Override
            public void onRowClick(String sample) {
                Intent intent=null;
                if(sample.equalsIgnoreCase("Social Login")) {
                    intent = new Intent(HomeActivity.this, SocialLoginActivity.class);
                    startActivity(intent);
                }else if(sample.equalsIgnoreCase("Brightness Manager")){
                        intent=new Intent(HomeActivity.this,BrightnessActivity.class);
                        startActivity(intent);
                } else if(sample.equalsIgnoreCase("Face Detection")){
                        intent=new Intent(HomeActivity.this,FaceDetectionActivity.class);
                        startActivity(intent);
                }

            }
        });
    }


}
