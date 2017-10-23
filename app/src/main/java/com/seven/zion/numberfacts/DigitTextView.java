package com.seven.zion.numberfacts;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Class Created by Stephen on 02-Oct-17.
 */

public class DigitTextView extends FrameLayout {

    private int ANIMATION_DURATION = 1;
    TextView currentTextView, nextTextView;

    public DigitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public int getANIMATION_DURATION() {
        return ANIMATION_DURATION;
    }

    public DigitTextView(Context context) {
        super(context);
        init(context);
    }
    public void setAnimationDuration(int duration)
    {
        ANIMATION_DURATION = duration;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.digit_text_view, this);
        currentTextView = (TextView) getRootView().findViewById(R.id.currentTextView);
        nextTextView = (TextView) getRootView().findViewById(R.id.nextTextView);

        nextTextView.setTranslationY(getHeight());

        setValue(0);
    }

    public void setValue(final int desiredValue) {
        if (currentTextView.getText() == null || currentTextView.getText().length() == 0) {
            currentTextView.setText(String.format(Locale.getDefault(), "%d", desiredValue));
        }

        final int oldValue = Integer.parseInt(currentTextView.getText().toString());

        if (oldValue > desiredValue) {
            nextTextView.setText(String.format(Locale.getDefault(), "%d", oldValue-1));

            currentTextView.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
            nextTextView.setTranslationY(nextTextView.getHeight());
            nextTextView.animate().translationY(0).setDuration(ANIMATION_DURATION).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentTextView.setText(String.format(Locale.getDefault(), "%d", oldValue - 1));
                    currentTextView.setTranslationY(0);
                    if (oldValue - 1 != desiredValue) {
                        setValue(desiredValue);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        } else if (oldValue < desiredValue) {
            nextTextView.setText(String.format(Locale.getDefault(), "%d", oldValue+1));

            currentTextView.animate().translationY(getHeight()).setDuration(ANIMATION_DURATION).start();
            nextTextView.setTranslationY(-nextTextView.getHeight());
            nextTextView.animate().translationY(0).setDuration(ANIMATION_DURATION).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentTextView.setText(String.format(Locale.getDefault(), "%d", oldValue + 1));
                    currentTextView.setTranslationY(0);
                    if (oldValue + 1 != desiredValue) {
                        setValue(desiredValue);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        }
    }
}