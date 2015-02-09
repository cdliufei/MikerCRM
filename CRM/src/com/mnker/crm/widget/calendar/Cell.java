/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mnker.crm.widget.calendar;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mnker.crm.R;
import com.mnker.crm.widget.CalendarView;
import com.mnker.crm.widget.util.ViewScaleUtil;

public class Cell {
	private static final String TAG = "Cell";
	protected Rect mBound = null;
	public boolean isThisMonth;
	protected int mDayOfMonth = 1; // from 1 to 31
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
	int dx, dy;

	private Drawable circle;
	private Drawable rectangle;
	private Drawable triangle;

	private Context mContext;
	private float textSize;
	private int total;

	public Cell(int dayOfMon, Rect rect, float textSize, boolean bold, Context context) {
		mContext = context;
		mDayOfMonth = dayOfMon;
		mBound = rect;
		this.textSize = textSize;
		mPaint.setTextSize(textSize/* 26f */);
		mPaint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_num));
		// mPaint.setColor(Color.BLACK);
		mPaint.setSubpixelText(true);
		if (bold)
			mPaint.setFakeBoldText(true);
		// circle = context.getResources().getDrawable(R.drawable.month_11);
		// rectangle = context.getResources().getDrawable(R.drawable.month_12);
		// triangle = context.getResources().getDrawable(R.drawable.month_13);

		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
		// Log.e("dx--dy", "dx---"+dx+"  dy==="+dy+"  "+
		// mPaint.ascent()+"    "+mPaint.descent()+"  "+mBound.centerX()+"   "+mBound.centerY()+"  "+mBound.top+"  "+mBound.bottom);
	}

	public Cell(int dayOfMon, Rect rect, float textSize, Context context) {
		this(dayOfMon, rect, textSize, false, context);
	}

	public void draw(Canvas canvas, int total) {
		if (isThisMonth && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == mDayOfMonth
				&& Calendar.getInstance().get(Calendar.MONTH) == CalendarView.mHelper.getMonth()
				&& Calendar.getInstance().get(Calendar.YEAR) == CalendarView.mHelper.getYear()) {
			// Rect rect = mBound;
			// rect.top += 2;
			// rect.left += 1;
			// rect.right -= 1;
			// rect.bottom -= 2;
			Rect rect = new Rect();
			rect.top = mBound.top + 2;
			rect.left = mBound.left + 1;
			rect.right = mBound.left - 1;
			rect.bottom = mBound.bottom - 2;
			canvas.drawBitmap(CalendarView.bckg, null, rect, mPaint);

		}

		if (isThisMonth) {
			// drawDis(canvas);
			// canvas.drawText(String.valueOf(mDayOfMonth), mBound.left + 3,
			// mBound.top + 2 * dy, mPaint);

			Paint paint = new Paint();
			paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_num_bg));
			paint.setStyle(Style.FILL_AND_STROKE);

			int startX = mBound.left + (mBound.right - mBound.left) / 2 - dx;
			int startY = mBound.top + (mBound.bottom - mBound.top) / 2 + dy - 3;
			if (total != 0) {
				this.total = total;
				// 计算小数的宽度
				paint.setTextSize(23.0f * ViewScaleUtil.sScale);
				int totaldx = (int) paint.measureText(String.valueOf(total)) / 2;
				int totaldy = (int) (-paint.ascent() + paint.descent()) / 2;
				// end
				int radiu = totaldy;
				if (totaldx > totaldy) {
					radiu = totaldx;
				} else {
					radiu = totaldy;
				}
				//
				// // canvas.drawCircle(startX + dx, startY - dy + 3,
				// (mBound.bottom
				// -
				// // mBound.top - 35 * ViewScaleUtil.sScale) / 2, paint);
				paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_total_bg));

				int totalX = mBound.left + (mBound.right - mBound.left) * 3 / 4;
				int totalY = mBound.top + (mBound.bottom - mBound.top) * 1 / 4;

				canvas.drawCircle(totalX, totalY, radiu + 3 * ViewScaleUtil.sScale, paint);
				// canvas.drawCircle(totalX, totalY, 14 * ViewScaleUtil.sScale,
				// paint);
				paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_total_txt));
				paint.setTextSize(23.0f * ViewScaleUtil.sScale);
				// mPaint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_total_txt));

				canvas.drawText(String.valueOf(total), totalX - totaldx, totalY + totaldy - 3 * ViewScaleUtil.sScale, paint);
			} else {
				this.total = 0;
			}
			Log.d("zaokun", "mDayOfMonth======================================================" + mDayOfMonth);
			canvas.drawText(String.valueOf(mDayOfMonth), startX, startY, mPaint);
		}
	}

	public void drawCicle(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_num_bg));
		paint.setStyle(Style.FILL_AND_STROKE);

		int startX = mBound.left + (mBound.right - mBound.left) / 2 - dx;
		int startY = mBound.top + (mBound.bottom - mBound.top) / 2 + dy - 3;
		// canvas.drawCircle(startX + dx, startY - dy + 6 *
		// ViewScaleUtil.sScale, (mBound.bottom - mBound.top - 20 *
		// ViewScaleUtil.sScale) / 2, paint);
		int radiu = dx;
		if (dx > dy) {
			radiu = dx;
		} else {
			radiu = dy;
		}
		canvas.drawCircle(startX + dx, startY - dy + 6 * ViewScaleUtil.sScale, radiu + 9 * ViewScaleUtil.sScale, paint);
		if (total != 0) {
			// total = 98;
			// 计算小数的宽度
			paint.setTextSize(23.0f * ViewScaleUtil.sScale);
			int totaldx = (int) paint.measureText(String.valueOf(total)) / 2;
			int totaldy = (int) (-paint.ascent() + paint.descent()) / 2;
			// end

			radiu = totaldy;
			if (totaldx > totaldy) {
				radiu = totaldx;
			} else {
				radiu = totaldy;
			}
			paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_total_bg));

			int totalX = mBound.left + (mBound.right - mBound.left) * 3 / 4;
			int totalY = mBound.top + (mBound.bottom - mBound.top) * 1 / 4;
			canvas.drawCircle(totalX, totalY, radiu + 3 * ViewScaleUtil.sScale, paint);
			// if (total > 9) {
			// canvas.drawCircle(totalX, totalY, totaldx + 3 *
			// ViewScaleUtil.sScale, paint);
			// } else {
			// canvas.drawCircle(totalX, totalY, 2 * totaldx +
			// ViewScaleUtil.sScale * 3, paint);
			// }
			paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_total_txt));
			paint.setTextSize(23.0f * ViewScaleUtil.sScale);
			canvas.drawText(String.valueOf(total), totalX - totaldx, totalY + totaldy - 3 * ViewScaleUtil.sScale, paint);
		}
		paint.setTextSize(textSize);
		paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_total_txt));
		canvas.drawText(String.valueOf(mDayOfMonth), startX, startY, paint);
	}

	public void drawLine(Canvas canvas, int i) {
		canvas.save();
		Paint paint = new Paint();
		paint.setColor(mContext.getResources().getColor(R.color.schedule_calendar_line));
		// paint.setFakeBoldText(true);
		paint.setStrokeWidth(2 * ViewScaleUtil.sScale);
		paint.setStyle(Style.FILL_AND_STROKE);

		int startX = 5;
		int startY = mBound.bottom;
		float stopX = ViewScaleUtil.width - 5;
		float stopY = mBound.bottom;
		canvas.drawLine(startX, startY, stopX, stopY, paint);
		canvas.restore();
	}

	// private void drawDis(Canvas canvas) {
	// int year = CalendarView.mHelper.getYear();
	// int month = CalendarView.mHelper.getMonth();
	// int day = mDayOfMonth;
	//
	// String date = year + "/" + (month + 1) + "/" + day;
	// String tempDate = Util.formatToRod(date);
	//
	// // DataHelper helper = DataHelper.getInstance(mContext);
	// // HashMap<String, Boolean> map =
	// // helper.getCategoryDis(AppSet.CURRENT_USER.getUserId(), tempDate);
	// HashMap<String, Boolean> map = CalendarView.monthMap.get(tempDate);
	//
	// if (map != null) {
	//
	// int disCount = 0;
	//
	// if (map.get(Constant.DIARY)) {
	// disCount++;
	// }
	//
	// if (map.get(Constant.BOOK)) {
	// disCount++;
	// }
	//
	// if (map.get(Constant.OTHER)) {
	// disCount++;
	// }
	//
	// if (disCount == 3) {
	// if (map.get(Constant.DIARY)) {
	// circle.setBounds(mBound.centerX() - 25, mBound.centerY(),
	// mBound.centerX() - 10, mBound.centerY() + 15);
	// }
	//
	// if (map.get(Constant.BOOK)) {
	// rectangle.setBounds(mBound.centerX() - 9, mBound.centerY(),
	// mBound.centerX() + 8, mBound.centerY() + 15);
	// }
	//
	// if (map.get(Constant.OTHER)) {
	// triangle.setBounds(mBound.centerX() + 10, mBound.centerY(),
	// mBound.centerX() + 25, mBound.centerY() + 15);
	// }
	// } else if (disCount == 2) {
	// if (map.get(Constant.DIARY) == false) {
	//
	// rectangle.setBounds(mBound.centerX() - 17, mBound.centerY(),
	// mBound.centerX() - 1, mBound.centerY() + 15);
	//
	// triangle.setBounds(mBound.centerX() - 0, mBound.centerY(),
	// mBound.centerX() + 17, mBound.centerY() + 15);
	// } else if (map.get(Constant.BOOK) == false) {
	// circle.setBounds(mBound.centerX() - 17, mBound.centerY(),
	// mBound.centerX() - 1, mBound.centerY() + 15);
	// triangle.setBounds(mBound.centerX() - 0, mBound.centerY(),
	// mBound.centerX() + 17, mBound.centerY() + 15);
	// } else if (map.get(Constant.OTHER) == false) {
	// circle.setBounds(mBound.centerX() - 17, mBound.centerY(),
	// mBound.centerX() - 1, mBound.centerY() + 15);
	// rectangle.setBounds(mBound.centerX() - 0, mBound.centerY(),
	// mBound.centerX() + 17, mBound.centerY() + 15);
	// }
	//
	// } else if (disCount == 1) {
	// if (map.get(Constant.DIARY)) {
	// circle.setBounds(mBound.centerX() - 9, mBound.centerY(), mBound.centerX()
	// + 8, mBound.centerY() + 15);
	// }
	//
	// if (map.get(Constant.BOOK)) {
	// rectangle.setBounds(mBound.centerX() - 9, mBound.centerY(),
	// mBound.centerX() + 8, mBound.centerY() + 15);
	// }
	//
	// if (map.get(Constant.OTHER)) {
	// triangle.setBounds(mBound.centerX() - 9, mBound.centerY(),
	// mBound.centerX() + 8, mBound.centerY() + 15);
	// }
	// }
	// }
	//
	// circle.draw(canvas);
	// triangle.draw(canvas);
	// rectangle.draw(canvas);
	// }

	public int getDayOfMonth() {
		return mDayOfMonth;
	}

	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y);
	}

	public Rect getBound() {
		return mBound;
	}

	public String toString() {
		return String.valueOf(mDayOfMonth) + "(" + mBound.toString() + ")";
	}

}
