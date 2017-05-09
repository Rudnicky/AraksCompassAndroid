package com.example.arakjel.arakscompass.controls;


import android.content.Context;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.arakjel.arakscompass.R;


public class MenuControl extends RelativeLayout {

    //region Variables
    // Animation arguments
    // [0] - ToXDelta, [1] - ToYDelta, [2] - Duration
    private final int[] firstMenuItem = { 100, 700, 300 };
    private final int[] secondMenuItem = { 410, 570, 400 };
    private final int[] thirdMenuItem = { 650, 350, 500 };
    private final int[] fourthMenuItem = { 750, 50, 600 };

    // animation vars
    private TranslateAnimation animation;
    private TranslateAnimation animationBack;
    private TranslateAnimation animation2;
    private TranslateAnimation animationBack2;
    private TranslateAnimation animation3;
    private TranslateAnimation animationBack3;
    private TranslateAnimation animation4;
    private TranslateAnimation animationBack4;

    // primitive vars
    private Context mContext;
    private boolean isMenuVisible;
    private long mLastClickTime = 0;
    private int mLeftMargin = 10;
    private int mTopMargin = 10;

    // ui vars
    public ImageView item1;
    public ImageView item2;
    public ImageView item3;
    public ImageView item4;

    public HamburgerMenuDrawable hamburgerMenuDrawable;
    public View view;
    //endregion

    //region Ctor
    public MenuControl(Context context) {
        super(context);
        init(context);
    }

    public MenuControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    //endregion

    //region Listeners
    private void setMainListener() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                    hamburgerMenuDrawable.toggle();
                    startAnimation();
                }
            }
        });
    }
    //endregion

    //region Methods
    private void init(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_control, this );

        initialize();
        setHamburgerMenu();
        setAnimation();
    }

    private void initialize() {
        item1 = (ImageView) findViewById(R.id.item1);
        item2 = (ImageView) findViewById(R.id.item2);
        item3 = (ImageView) findViewById(R.id.item3);
        item4 = (ImageView) findViewById(R.id.item4);
        setVisibility();
    }

    private void setVisibility() {
        item1.setVisibility(View.GONE);
        item2.setVisibility(View.GONE);
        item3.setVisibility(View.GONE);
        item4.setVisibility(View.GONE);
    }

    public void changeColor(int color) {
        hamburgerMenuDrawable.mLinePaint.setColor(color);
        hamburgerMenuDrawable.mBackgroundPaint.setColor(color);
        hamburgerMenuDrawable.invalidateSelf();
    }

    private void setHamburgerMenu() {
        view = findViewById(R.id.view);
        hamburgerMenuDrawable = new HamburgerMenuDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width),
                ContextCompat.getColor(mContext, R.color.colorSea), ContextCompat.getColor(mContext, R.color.colorGray), ContextCompat.getColor(mContext, R.color.colorSea));

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ViewCompat.setBackground(view, hamburgerMenuDrawable);
        } else {
            view.setBackground(hamburgerMenuDrawable);
        }

        setMainListener();
    }
    //endregion

    //region Animations
    private void setAnimation() {
        setFirstItemAnimation();
        setSecondItemAnimation();
        setThirdItemAnimation();
        setFourthItemAnimation();
    }

    public void startAnimation() {
        if (isMenuVisible) {
            item1.startAnimation(animationBack);
            item2.startAnimation(animationBack2);
            item3.startAnimation(animationBack3);
            item4.startAnimation(animationBack4);
            isMenuVisible = false;
        } else if (!isMenuVisible) {
            item1.startAnimation(animation);
            item2.startAnimation(animation2);
            item3.startAnimation(animation3);
            item4.startAnimation(animation4);
            isMenuVisible = true;
        }
    }

    private void setFirstItemAnimation() {


        animation = new TranslateAnimation(mLeftMargin, firstMenuItem[0], mTopMargin, firstMenuItem[1]);
        animation.setDuration(firstMenuItem[2]);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                item1.setVisibility(View.VISIBLE);
                item1.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item1, firstMenuItem[0], firstMenuItem[1], true);
                item1.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationBack = new TranslateAnimation(0, -firstMenuItem[0] + mLeftMargin, 0, -firstMenuItem[1] + mTopMargin);
        animationBack.setDuration(firstMenuItem[2]);
        animationBack.setFillAfter(false);
        animationBack.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item1, 0, 0, false);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setSecondItemAnimation() {
        animation2 = new TranslateAnimation(mLeftMargin, secondMenuItem[0], mTopMargin, secondMenuItem[1]);
        animation2.setDuration(secondMenuItem[2]);
        animation2.setFillAfter(false);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                item2.setVisibility(View.VISIBLE);
                item2.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item2, secondMenuItem[0], secondMenuItem[1], true);
                item2.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationBack2 = new TranslateAnimation(0, -secondMenuItem[0] + mLeftMargin, 0, -secondMenuItem[1] + mTopMargin);
        animationBack2.setDuration(secondMenuItem[2]);
        animationBack2.setFillAfter(false);
        animationBack2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item2, 0, 0, false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setThirdItemAnimation() {
        animation3 = new TranslateAnimation(mLeftMargin, thirdMenuItem[0], mTopMargin, thirdMenuItem[1]);
        animation3.setDuration(thirdMenuItem[2]);
        animation3.setFillAfter(false);
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                item3.setVisibility(View.VISIBLE);
                item3.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item3, thirdMenuItem[0], thirdMenuItem[1], true);
                item3.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationBack3 = new TranslateAnimation(0, -thirdMenuItem[0] + mLeftMargin, 0, -thirdMenuItem[1] + mTopMargin);
        animationBack3.setDuration(thirdMenuItem[2]);
        animationBack3.setFillAfter(false);
        animationBack3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item3, 0, 0, false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setFourthItemAnimation() {
        animation4 = new TranslateAnimation(mLeftMargin, fourthMenuItem[0], mTopMargin, fourthMenuItem[1]);
        animation4.setDuration(fourthMenuItem[2]);
        animation4.setFillAfter(false);
        animation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                item4.setVisibility(View.VISIBLE);
                item4.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item4, fourthMenuItem[0], fourthMenuItem[1], true);
                item4.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationBack4 = new TranslateAnimation(0, -fourthMenuItem[0] + mLeftMargin, 0, -fourthMenuItem[1] + mTopMargin);
        animationBack4.setDuration(fourthMenuItem[2]);
        animationBack4.setFillAfter(false);
        animationBack4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMenuItem(item4, 0, 0, false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setMenuItem(View view, int toXDelta, int toYDelta, boolean isVisible) {
        view.clearAnimation();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getWidth(),
                view.getHeight());
        lp.setMargins(toXDelta, toYDelta, 0, 0);
        view.setLayoutParams(lp);
        if (!isVisible) {
            view.setVisibility(View.INVISIBLE);
        }
    }
    //endregion
}
