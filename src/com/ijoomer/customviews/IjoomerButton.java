package com.ijoomer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;

/**
 * This Class Contains All Method Related To IjoomerButton.
 * 
 * @author tasol
 * 
 */
public class IjoomerButton extends Button {

	public IjoomerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public IjoomerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IjoomerButton(Context context) {
		super(context);
		init(context);
	}

	private void init(Context mContext) {

		try {
			if (IjoomerApplicationConfiguration.getFontFace() != null) {
				if (getTypeface() != null) {
					setTypeface(IjoomerApplicationConfiguration.getFontFace(), getTypeface().getStyle());
				} else {
					setTypeface(IjoomerApplicationConfiguration.getFontFace());
				}
			} else {
				Typeface tf = Typeface.createFromAsset(mContext.getAssets(), IjoomerApplicationConfiguration.getFontNameWithPath());
				if (getTypeface() != null) {
					setTypeface(tf, getTypeface().getStyle());
				} else {
					setTypeface(IjoomerApplicationConfiguration.getFontFace());
				}
				IjoomerApplicationConfiguration.setFontFace(tf);
			}
		} catch (Throwable e) {
		}
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                 getBackground().setColorFilter(Color.parseColor("#80FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                invalidate();

                break;
            }
            case MotionEvent.ACTION_UP: {
                getBackground().clearColorFilter();
                invalidate();
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
