package com.beyondsw.seekcolorpicker;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wensefu on 2017/11/28.
 */

public class SeekColorPicker extends SeekBar {

    private static final int[] COLORS = new int[]{
            0xFF000000,
            0xFF9900FF,
            0xFF0000FF,
            0xFF00FF00,
            0xFF00FFFF,
            0xFFFF0000,
            0xFFFF00FF,
            0xFFFF6600,
            0xFFFFFF00,
            0xFFFFFFFF,
            0xFF000000
    };


    private GradientDrawable mColorsDrawable;
    private Set<OnColorSeekBarChangeListener> mListeners;
    private static final ArgbEvaluator sEvaluator = new ArgbEvaluator();

    public SeekColorPicker(Context context) {
        this(context, null);
    }

    public SeekColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnSeekBarChangeListener(mInternalListener);
        mColorsDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, COLORS);
        setProgressDrawable(mColorsDrawable);
    }

    public interface OnColorSeekBarChangeListener {

        void onColorChanged(SeekColorPicker seekBar, int color, boolean fromUser);

        void onStartTrackingTouch(SeekColorPicker seekBar);

        void onStopTrackingTouch(SeekColorPicker seekBar);
    }

    public void addListener(OnColorSeekBarChangeListener listener) {
        if (mListeners == null) {
            mListeners = new HashSet<>();
        }
        mListeners.add(listener);
    }

    public void removeListener(OnColorSeekBarChangeListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    private int pickColor(int progress) {
        float colorUnit = (float) getMax() / (COLORS.length - 1);
        float index = progress / colorUnit;
        int startColorIndex = (int) index;
        if (startColorIndex == COLORS.length - 1) {
            return COLORS[COLORS.length - 1];
        }
        int endColorIndex = startColorIndex + 1;
        float fraction = index % 1;
        return (int) sEvaluator.evaluate(fraction, COLORS[startColorIndex], COLORS[endColorIndex]);
    }


    public int getColor() {
        return pickColor(getProgress());
    }

    private OnSeekBarChangeListener mInternalListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mListeners != null) {
                for (OnColorSeekBarChangeListener l : mListeners) {
                    l.onColorChanged((SeekColorPicker) seekBar, pickColor(progress), fromUser);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (mListeners != null) {
                for (OnColorSeekBarChangeListener l : mListeners) {
                    l.onStartTrackingTouch((SeekColorPicker) seekBar);
                }
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mListeners != null) {
                for (OnColorSeekBarChangeListener l : mListeners) {
                    l.onStopTrackingTouch((SeekColorPicker) seekBar);
                }
            }
        }
    };

    @Override
    public final void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (l != mInternalListener) {
            return;
        }
        super.setOnSeekBarChangeListener(l);
    }

    @Override
    public final void setProgressDrawable(Drawable d) {
        if (d != mColorsDrawable) {
            return;
        }
        super.setProgressDrawable(d);
    }
}
