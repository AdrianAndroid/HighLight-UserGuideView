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
                Toast.makeText(AnimGuideActivity.this, "ll1", Toast.LENGTH_SHORT).show();
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


        AnimGuideView animGuideView = findViewById(R.id.animGuideView);
        animGuideView.setVisibility(View.VISIBLE);


        View second = findViewById(R.id.iv4);
        animGuideView.setHighLightView(second);
        animGuideView.setStatusBarHeight(MeasureUtil.getStatuBarHeight(this));
        animGuideView.startHandsAnimate();
        animGuideView.setOnGuideListener(new AnimGuideView.OnGuideListener() {

            @Override
            public void onTouched(AnimGuideView.TYPE type) {
                if (type == AnimGuideView.TYPE.TARGET) {
                    Toast.makeText(AnimGuideActivity.this, "TARGET", Toast.LENGTH_SHORT).show();
                } else if (type == AnimGuideView.TYPE.HINT) {
                    Toast.makeText(AnimGuideActivity.this, "HINT", Toast.LENGTH_SHORT).show();
                } else if (type == AnimGuideView.TYPE.OUTSIDE) {
                    Toast.makeText(AnimGuideActivity.this, "OUTSIDE", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 1. 先把背景涂黑
        // 2。 找到那个View的位置，画圆
        // 3. 提示的小手 （上下动起来）
        // 4. 我知道了

    }
}
