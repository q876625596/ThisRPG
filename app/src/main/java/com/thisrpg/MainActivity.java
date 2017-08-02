package com.thisrpg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.allen.library.SuperTextView;
import com.thisrpg.scene.LoadingActivity;

public class MainActivity extends AppCompatActivity {

    private SuperTextView about;
    private SuperTextView option;
    private SuperTextView collection;
    private SuperTextView start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        about = (SuperTextView) findViewById(R.id.about);
        option = (SuperTextView) findViewById(R.id.option);
        collection = (SuperTextView) findViewById(R.id.collection);
        start = (SuperTextView) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoadingActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }


}
