package com.zhl.userguideview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

public final class GuideUtils {

    // 在Activity显示最上面
    public static void showGuideAtTop(Activity act, View anchorView, final AnimGuideView.OnGuideListener listener) {
        Activity activity = new WeakReference<Activity>(act).get();
        if (activity == null) return;
        View viewById = activity.findViewById(android.R.id.content);
        if (viewById instanceof FrameLayout) {
            // 创建Anim
            final AnimGuideView animGuideView = new AnimGuideView(activity);
            animGuideView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );
            ((FrameLayout) viewById).addView(animGuideView);
            // 播放动画
            animGuideView.setHighLightView(anchorView);
            animGuideView.setStatusBarHeight(MeasureUtil.getStatuBarHeight(activity));
            animGuideView.startHandsAnimate();
            animGuideView.bringToFront();//放到最前面
            animGuideView.setOnGuideListener(new AnimGuideView.OnGuideListener() {
                @Override
                public void onTouched(final AnimGuideView view, AnimGuideView.TYPE type) {
                    if (listener != null) listener.onTouched(view, type);
                }
            });
        }
    }

    public static void dismissGuideView(final AnimGuideView view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha"
                , 1F, 0.9F, 0.8F, 0.7F, 0.6F, 0.5F, 0.4F, 0.3F, 0.2F, 0.1F, 0F)
                .setDuration(200);
        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (view != null && view.getParent() != null) {
                    ((FrameLayout) view.getParent()).removeView(view);
                    // 设置alpha
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alpha.start();
    }


}
