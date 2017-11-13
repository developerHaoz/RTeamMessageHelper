package com.hans.alpha.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author Frederico Silva (fredericojssilva@gmail.com)
 * modify by changelcai on 2016/1/23.
 */
public class ShowTipsBuilder {
	TipsView mTipsView;

	public ShowTipsBuilder(Activity activity) {
		this.mTipsView = new TipsView(activity);
	}

	/**
	 * Set highlight view. All view will be highlighted
	 * 
	 * @param v
	 *            Target view
	 * @return ShowTipsBuilder
	 */
	public ShowTipsBuilder setTarget(View v) {
		this.mTipsView.setTarget(v);
		return this;
	}

	/**
	 * Set highlighted view with custom center and radius
	 * 
	 * @param v
	 *            Target View
	 * @param x
	 *            circle center x according target
	 * @param y
	 *            circle center y according target
	 * @param radius
	 * @return
	 */
	public ShowTipsBuilder setTarget(View v, int x, int y, int radius) {
		mTipsView.setTarget(v, x, y, radius);

		return this;
	}

	public TipsView build() {
		return mTipsView;
	}

	public ShowTipsBuilder setTitle(String text) {
		this.mTipsView.setTitle(text);
		return this;
	}

	public ShowTipsBuilder setDescription(String text) {
		this.mTipsView.setDescription(text);
		return this;
	}

	public ShowTipsBuilder displayOneTime(int showtipId) {
		this.mTipsView.setDisplayOneTime(true);
		this.mTipsView.setDisplayOneTimeID(showtipId);
		return this;
	}

	public ShowTipsBuilder setCallback(TipsView.TipsViewInterface callback) {
		this.mTipsView.setCallback(callback);
		return this;
	}

	public ShowTipsBuilder setDelay(int delay) {
		mTipsView.setDelay(delay);
		return this;
	}

	public ShowTipsBuilder setTitleColor(int color) {
		mTipsView.setTitle_color(color);
		return this;
	}

	public ShowTipsBuilder setDescriptionColor(int color) {
		mTipsView.setDescription_color(color);
		return this;
	}

	public ShowTipsBuilder setBackgroundColor(int color) {
		mTipsView.setBackground_color(color);
		return this;
	}

	public ShowTipsBuilder setCircleColor(int color) {
		mTipsView.setCircleColor(color);
		return this;
	}

	public ShowTipsBuilder setButtonText(String text) {
		this.mTipsView.setButtonText(text);
		return this;
	}

	public ShowTipsBuilder setCloseButtonColor(int color){
		this.mTipsView.setButtonColor(color);
		return this;
	}
	public ShowTipsBuilder setCloseButtonTextColor(int color){
		this.mTipsView.setButtonTextColor(color);
		return this;
	}
	public ShowTipsBuilder setButtonBackground(Drawable drawable){
		this.mTipsView.setCloseButtonDrawableBG(drawable);
		return this;
	}

	/**
	 * Set transparecy for background layer. 0-255 range
	 * @param alpha
	 * @return ShowTipsbuilder
	 */
	public ShowTipsBuilder setBackgroundAlpha(int alpha) {
		this.mTipsView.setBackground_alpha(alpha);
		return this;
	}
}
