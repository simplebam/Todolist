package com.yueyue.todolist.modules.splash.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.yueyue.todolist.R;
import com.yueyue.todolist.modules.main.MainActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    @BindView(R.id.splash_container)
    View container;

    @BindView(R.id.iv_logo)
    ImageView mIvLogo;

    @BindView(R.id.tv_name)
    TextView mTvName;

    private TextView[] mTvArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        //GradientDrawable 动态设置背景的使用 - CSDN博客
        //           http://blog.csdn.net/qq_35522272/article/details/60871677
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorPrimaryDark)
                });
        container.setBackground(gd);
        container.setClickable(false);

        mTvArr = new TextView[]{
                (TextView) findViewById(R.id.tv_t1),
                (TextView) findViewById(R.id.tv_o1),
                (TextView) findViewById(R.id.tv_d),
                (TextView) findViewById(R.id.tv_o2),
                (TextView) findViewById(R.id.tv_l),
                (TextView) findViewById(R.id.tv_i),
                (TextView) findViewById(R.id.tv_s),
                (TextView) findViewById(R.id.tv_t2)
        };
        mTvArr[0].post(new Runnable() {
            @Override
            public void run() {
                for (TextView t : mTvArr) {
                    t.setVisibility(View.VISIBLE);
                    startTextAnim(t);
                }
            }
        });
    }


    private void startTextAnim(TextView textView) {
        Random r = new Random();
        int x = r.nextInt(ScreenUtils.getScreenWidth() * 4 / 3);
        int y = r.nextInt(ScreenUtils.getScreenHeight() * 4 / 3);
        float s = r.nextFloat() + 4.0f;
        //translation:平移  scale:缩放  alpha:透明度   rotate:旋转
        ValueAnimator tranY = ObjectAnimator.ofFloat(textView, "translationY", y - textView.getY(), 0);
        ValueAnimator tranX = ObjectAnimator.ofFloat(textView, "translationX", x - textView.getX(), 0);
        ValueAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", s, 1.0f);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", s, 1.0f);
        ValueAnimator alpha = ObjectAnimator.ofFloat(textView, "alpha", 0.0f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1800);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(tranX)
                .with(tranY)
                .with(scaleX)
                .with(scaleY)
                .with(alpha);
        if (R.id.tv_t2 == textView.getId()) {
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onTextAnimFinish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        animatorSet.start();
    }


    private void onTextAnimFinish() {
        ValueAnimator alpha = ObjectAnimator.ofFloat(mIvLogo, "alpha", 0.0f, 1.0f);
        alpha.setDuration(1000);
        ValueAnimator alphaN = ObjectAnimator.ofFloat(mTvName, "alpha", 0.0f, 1.0f);
        alphaN.setDuration(1000);
        ValueAnimator tranY = ObjectAnimator.ofFloat(mIvLogo, "translationY", -mIvLogo.getHeight() / 3, 0);
        tranY.setDuration(1000);
        ValueAnimator waitTime = ObjectAnimator.ofInt(0, 100);
        waitTime.setDuration(1000);
        waitTime.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        launchMainActivity();
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIvLogo.setVisibility(View.VISIBLE);
                mTvName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animatorSet.play(alpha)
                .with(alphaN)
                .with(tranY)
                .before(waitTime);
        animatorSet.start();
    }

    private void launchMainActivity() {
        MainActivity.launch(SplashActivity.this);
        finish();
    }


}
