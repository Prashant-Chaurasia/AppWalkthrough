package com.example.prashant.appintro;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btn_join_now,btn_join_now1, btn_sign_up;
    private PrefManager prefManager;
    TextView animatetv;

    ArgbEvaluator evaluator;
    int[] colorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_intro);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btn_join_now = (Button) findViewById(R.id.btn_join_now);
       // btn_join_now1 = (Button) findViewById(R.id.btn_join_now1);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        /*btn_join_now1 = (Button) viewPager.findViewById(R.id.btnjoinnow);*/
        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.intro1,
                R.layout.intro2,
                R.layout.intro3,
                R.layout.intro4};
        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();
/*
        final int color1 = ContextCompat.getColor(this, R.color.dot_dark);
        final int color2 = ContextCompat.getColor(this, R.color.bg_screen2);*/
        //final int color3 = ContextCompat.getColor(this, R.color.bg_screen4);

       /* colorList = new int[]{color1, color2};*/

         evaluator = new ArgbEvaluator();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btn_join_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
/*

        btn_join_now1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
*/

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226;",Build.VERSION.SDK_INT));
            }
            else{
                dots[i].setText(Html.fromHtml("&#8226;"));
            }
            dots[i].setTextSize(35);
            dots[i].setTextColor(getColor(R.color.dot_dark));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getColor(R.color.dot_light));
    }
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        boolean animated = false;
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if(position>0) {
                btn_sign_up.setTextColor(getColor(R.color.btn_border));
                if (position == 1) {
                    if (viewPager.getChildAt(1).findViewById(R.id.animate_tv) == null) {
                        animatetv = (TextView) findViewById(R.id.animate_tv);
                        if (animatetv != null) {
                            startAnimation();
                        }
                    } else {
                        animatetv = (TextView) viewPager.getChildAt(1).findViewById(R.id.animate_tv);
                        startAnimation();
                    }
                }

                if (position != 1) {
                    if (viewPager.getChildAt(1).findViewById(R.id.animate_tv) == null) {
                        animatetv = (TextView) findViewById(R.id.animate_tv);
                        if (animatetv != null) {
                            animatetv.setText(R.string.connect);
                        }
                    } else {
                        animatetv = (TextView) viewPager.getChildAt(1).findViewById(R.id.animate_tv);
                        animatetv.setText(R.string.connect);
                    }
                }
            }
            else{
                if (viewPager.getChildAt(1).findViewById(R.id.animate_tv) == null) {
                    animatetv = (TextView) findViewById(R.id.animate_tv);
                    if (animatetv != null) {
                        animatetv.setText(R.string.connect);
                    }
                } else {
                    animatetv = (TextView) viewPager.getChildAt(1).findViewById(R.id.animate_tv);
                    animatetv.setText(R.string.connect);
                }
                btn_join_now.setTextColor(Color.WHITE);
                btn_sign_up.setTextColor(getColor(R.color.dot_dark));
            }

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {


//            if(position==0&&positionOffset>0){
//                //int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 1 ? position : position + 1]);
//
//            }
/*
            if((position==0 && positionOffset>0&&positionOffset<1)||(position==2 && positionOffset>0&&positionOffset<1)){
                animatetv.setText(R.string.connect);
            }*/
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
          /*  if(arg0 == viewPager.SCROLL_STATE_IDLE && viewPager.getCurrentItem()==1 && !animated ){

            }*/

        }
    };
    public void startAnimation(){
        int finalRadius = (int)Math.hypot(animatetv.getWidth(), animatetv.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(animatetv, (int) animatetv.getWidth()/2, (int) animatetv.getHeight()/2, 0, finalRadius);
        animatetv.setBackgroundColor(getColor(R.color.dot_light));
        anim.setDuration(500);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeBackToOriginal();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    public void changeBackToOriginal(){
        int startRadius = (int)Math.hypot(animatetv.getWidth(), animatetv.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(animatetv, (int) animatetv.getWidth()/2, (int) animatetv.getHeight()/2, startRadius,0);
        anim.setDuration(500);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                animatetv.setBackgroundColor(Color.WHITE);
                animatetv.setText(R.string.connected);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}