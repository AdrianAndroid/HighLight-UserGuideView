package com.zhl.userguideview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zhl.userguideview.userguideview.R;


public class AnimGuideActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animguide);


        findViewById(R.id.ll1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AnimGuideActivity.this, "ll1", Toast.LENGTH_SHORT).show();
                show();
            }
        });
        findViewById(R.id.ll2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimGuideActivity.this, "ll2", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ll3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimGuideActivity.this, "ll3", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ll4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimGuideActivity.this, "ll4", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimGuideActivity.this, "second", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void show() {
        View second = findViewById(R.id.second);
        GuideUtils.showGuideAtTop(this, second, new AnimGuideView.OnGuideListener() {

            @Override
            public void onTouched(AnimGuideView view, AnimGuideView.TYPE type) {
                if (type == AnimGuideView.TYPE.TARGET) {
                    Toast.makeText(AnimGuideActivity.this, "TARGET", Toast.LENGTH_SHORT).show();
                    GuideUtils.dismissGuideView(view);
                } else if (type == AnimGuideView.TYPE.HINT) {
                    Toast.makeText(AnimGuideActivity.this, "HINT", Toast.LENGTH_SHORT).show();
                    GuideUtils.dismissGuideView(view);
                } else if (type == AnimGuideView.TYPE.OUTSIDE) {
                    Toast.makeText(AnimGuideActivity.this, "OUTSIDE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
