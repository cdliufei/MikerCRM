package com.mnker.crm.widget.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * ViewScaleUtil eg: View view = (View) findViewById(R.id.viewID);
 * ViewScaleUtil.scaleProcess(view,layout_params_type); TextView textView =
 * (TextView) findViewById(R.id.textViewID);
 * ViewScaleUtil.scaleProcessTextView(textView,layout_params_type);
 */
public class ViewScaleUtil {

	public static float sScale = 1.0f;
	public static int height;
	public static int width;
	// ///////////////////////////////////////////////////////
	// Screen Params
	public static final int BASE_SCREEN_WIDTH = 720;
	public static final int BASE_SCREEN_HEIGHT = 1280;
	// ///////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////
	// LayoutParams Type
	public static final int LAYOUT_PARAMS_TYPE_LINEAR_LAYOUT = 0;
	public static final int LAYOUT_PARAMS_TYPE_RELATIVE_LAYOUT = 1;
	public static final int LAYOUT_PARAMS_TYPE_FRAME_LAYOUT = 2;

	public static final int LAYOUT_PARAMS_WIDTH = 0;
	public static final int LAYOUT_PARAMS_HEIGHT = 1;
	public static final int LAYOUT_PARAMS_TOP_MARGIN = 2;
	public static final int LAYOUT_PARAMS_LEFT_MARGIN = 3;
	public static final int LAYOUT_PARAMS_BOTTOM_MARGIN = 4;
	public static final int LAYOUT_PARAMS_RIGHT_MARGIN = 5;
	public static final int LAYOUT_PARAMS_COUNT = 6;

	// ///////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////
	// set screen scale value
	public static void setScale(Activity context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		ViewScaleUtil.sScale = (float) width / BASE_SCREEN_WIDTH;
		// System.out.println("scale == " + sScale + "  " + width + "  " +
		// height + "  " + displayMetrics.density + "  " +
		// displayMetrics.densityDpi);
	}

	// ///////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////
	// scale views
	public static int[] scaleProcess(View view, int paramType) {
		if (view == null || sScale == 1.0f)
			return null;
		int[] orgLayoutParams = new int[LAYOUT_PARAMS_COUNT];
		switch (paramType) {
		case LAYOUT_PARAMS_TYPE_LINEAR_LAYOUT:
			android.widget.LinearLayout.LayoutParams llp = (android.widget.LinearLayout.LayoutParams) view
					.getLayoutParams();
			orgLayoutParams[LAYOUT_PARAMS_WIDTH] = llp.width;
			orgLayoutParams[LAYOUT_PARAMS_HEIGHT] = llp.height;
			orgLayoutParams[LAYOUT_PARAMS_TOP_MARGIN] = llp.topMargin;
			orgLayoutParams[LAYOUT_PARAMS_LEFT_MARGIN] = llp.leftMargin;
			orgLayoutParams[LAYOUT_PARAMS_BOTTOM_MARGIN] = llp.bottomMargin;
			orgLayoutParams[LAYOUT_PARAMS_RIGHT_MARGIN] = llp.rightMargin;
			// Size
			if (llp.width > 0)
				llp.width = (int) Math.ceil(llp.width * sScale);
			if (llp.height > 0)
				llp.height = (int) Math.ceil(llp.height * sScale);
			// Margin
			llp.topMargin = (int) Math.ceil(llp.topMargin * sScale);
			llp.leftMargin = (int) Math.ceil(llp.leftMargin * sScale);
			llp.bottomMargin = (int) Math.ceil(llp.bottomMargin * sScale);
			llp.rightMargin = (int) Math.ceil(llp.rightMargin * sScale);
			// Padding
			int paddingTop = (int) Math.ceil(view.getPaddingTop() * sScale);
			int paddingLeft = (int) Math.ceil(view.getPaddingLeft() * sScale);
			int paddingRight = (int) Math.ceil(view.getPaddingRight() * sScale);
			int paddingBottom = (int) Math.ceil(view.getPaddingBottom()
					* sScale);
			view.setPadding(paddingLeft, paddingTop, paddingRight,
					paddingBottom);
			view.setLayoutParams(llp);
			break;
		case LAYOUT_PARAMS_TYPE_RELATIVE_LAYOUT:
			android.widget.RelativeLayout.LayoutParams rlp = (android.widget.RelativeLayout.LayoutParams) view
					.getLayoutParams();
			orgLayoutParams[LAYOUT_PARAMS_WIDTH] = rlp.width;
			orgLayoutParams[LAYOUT_PARAMS_HEIGHT] = rlp.height;
			orgLayoutParams[LAYOUT_PARAMS_TOP_MARGIN] = rlp.topMargin;
			orgLayoutParams[LAYOUT_PARAMS_LEFT_MARGIN] = rlp.leftMargin;
			orgLayoutParams[LAYOUT_PARAMS_BOTTOM_MARGIN] = rlp.bottomMargin;
			orgLayoutParams[LAYOUT_PARAMS_RIGHT_MARGIN] = rlp.rightMargin;
			// Size
			if (rlp.width > 0)
				rlp.width = (int) Math.ceil(rlp.width * sScale);
			if (rlp.height > 0)
				rlp.height = (int) Math.ceil(rlp.height * sScale);
			// Margin
			rlp.topMargin = (int) Math.ceil(rlp.topMargin * sScale);
			rlp.leftMargin = (int) Math.ceil(rlp.leftMargin * sScale);
			rlp.bottomMargin = (int) Math.ceil(rlp.bottomMargin * sScale);
			rlp.rightMargin = (int) Math.ceil(rlp.rightMargin * sScale);
			// System.out.println("rightMargin == "+rlp.rightMargin);
			// Padding
			paddingTop = (int) Math.ceil(view.getPaddingTop() * sScale);
			paddingLeft = (int) Math.ceil(view.getPaddingLeft() * sScale);
			paddingRight = (int) Math.ceil(view.getPaddingRight() * sScale);
			paddingBottom = (int) Math.ceil(view.getPaddingBottom() * sScale);
			view.setPadding(paddingLeft, paddingTop, paddingRight,
					paddingBottom);
			view.setLayoutParams(rlp);
			break;
		case LAYOUT_PARAMS_TYPE_FRAME_LAYOUT:
			android.widget.FrameLayout.LayoutParams flp = (android.widget.FrameLayout.LayoutParams) view
					.getLayoutParams();
			orgLayoutParams[LAYOUT_PARAMS_WIDTH] = flp.width;
			orgLayoutParams[LAYOUT_PARAMS_HEIGHT] = flp.height;
			orgLayoutParams[LAYOUT_PARAMS_TOP_MARGIN] = flp.topMargin;
			orgLayoutParams[LAYOUT_PARAMS_LEFT_MARGIN] = flp.leftMargin;
			orgLayoutParams[LAYOUT_PARAMS_BOTTOM_MARGIN] = flp.bottomMargin;
			orgLayoutParams[LAYOUT_PARAMS_RIGHT_MARGIN] = flp.rightMargin;
			// Size
			if (flp.width > 0)
				flp.width = (int) Math.ceil(flp.width * sScale);
			if (flp.height > 0)
				flp.height = (int) Math.ceil(flp.height * sScale);
			// Margin
			flp.topMargin = (int) Math.ceil(flp.topMargin * sScale);
			flp.leftMargin = (int) Math.ceil(flp.leftMargin * sScale);
			flp.bottomMargin = (int) Math.ceil(flp.bottomMargin * sScale);
			flp.rightMargin = (int) Math.ceil(flp.rightMargin * sScale);
			// Padding
			paddingTop = (int) Math.ceil(view.getPaddingTop() * sScale);
			paddingLeft = (int) Math.ceil(view.getPaddingLeft() * sScale);
			paddingRight = (int) Math.ceil(view.getPaddingRight() * sScale);
			paddingBottom = (int) Math.ceil(view.getPaddingBottom() * sScale);
			view.setPadding(paddingLeft, paddingTop, paddingRight,
					paddingBottom);
			view.setLayoutParams(flp);
			break;
		}

		return orgLayoutParams;
	}

	// ///////////////////////////////////////////////////////
	// scale textview
	public static void scaleProcessTextView(TextView view) {
		if (view == null || sScale == 1.0f)
			return;
		float size = view.getTextSize();
		size *= sScale;
		// Size's unit use pixel,so param unit use TypedValue.COMPLEX_UNIT_PX.
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
	}

	public static int[] scaleProcessTextView(TextView view, int paramType) {
		if (view == null || sScale == 1.0f)
			return null;
		int[] orgLayoutParams = scaleProcess(view, paramType);
		scaleProcessTextView(view);
		return orgLayoutParams;
	}

	// ///////////////////////////////////////////////////////

}
