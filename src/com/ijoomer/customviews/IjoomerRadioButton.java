package com.ijoomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;

/**
 * This Class Contains All Method Related To IjoomerRadioButton.
 * 
 * @author tasol
 * 
 */
public class IjoomerRadioButton extends RadioButton {

	public IjoomerRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public IjoomerRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IjoomerRadioButton(Context context) {
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

}
