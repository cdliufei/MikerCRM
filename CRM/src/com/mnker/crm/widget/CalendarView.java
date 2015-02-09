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

package com.mnker.crm.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.mnker.crm.R;
import com.mnker.crm.tool.UndoWorkCollectInfo;
import com.mnker.crm.widget.calendar.Cell;
import com.mnker.crm.widget.util.Util;
import com.mnker.crm.widget.util.ViewScaleUtil;

public class CalendarView extends ImageView {
	private static int CELL_WIDTH = 58;
	private static int CELL_HEIGH = 53;
	private static int CELL_MARGIN_TOP = 0;
	// private static int CELL_MARGIN_TOP = 92;
	private static int CELL_MARGIN_LEFT = 39;
	private static float CELL_TEXT_SIZE;

	private static final String TAG = "CalendarView";
	private Calendar mRightNow = null;
	private Cell mToday = null;
	private Cell[][] mCells = new Cell[6][7];
	private OnCellTouchListener mOnCellTouchListener = null;
	public static MonthDisplayHelper mHelper;
	private GestureDetector mGestureDetector;
	public static Bitmap bckg;

	private Context mContext;

	public static HashMap<String, HashMap<String, Boolean>> monthMap;

	public interface OnCellTouchListener {
		public void onTouch(Cell cell);
	}

	public CalendarView(Context context) {
		this(context, null);
	}

	public CalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFocusable(true);
		setFocusableInTouchMode(true);
		mContext = context;
		bckg = BitmapFactory.decodeResource(getResources(), R.drawable.month_14);
		initCalendarView();
	}

	public void initCalendarView() {

		mRightNow = Calendar.getInstance();
		// prepare static vars
		Resources res = getResources();
		// CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
		CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);

		CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
		// set background
		// setImageResource(R.drawable.calenda_background);
		// setScaleType(ScaleType.FIT_XY);

		mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR), mRightNow.get(Calendar.MONTH));
		sigleOntouch();
	}

	public Cell mSelectCell;
	private int selectDay = 1;
	private ArrayList<UndoWorkCollectInfo> unDoworkList;

	private void sigleOntouch() {
		mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				if (mOnCellTouchListener != null) {
					for (Cell[] week : mCells) {
						for (Cell day : week) {
							if (day.hitTest((int) e.getX(), (int) e.getY()) && day.isThisMonth) {
								mOnCellTouchListener.onTouch(day);
								mSelectCell = day;
								selectDay = day.getDayOfMonth();
								CalendarView.this.invalidate();
							}
						}
					}
				}
				return true;
			}

		});
	}

	private void initCells() {
		class _calendar {
			public int day;
			public boolean thisMonth;

			public _calendar(int d, boolean b) {
				day = d;
				thisMonth = b;
			}

			public _calendar(int d) {
				this(d, false);
			}
		}
		;
		_calendar tmp[][] = new _calendar[6][7];

		for (int i = 0; i < tmp.length; i++) {
			int n[] = mHelper.getDigitsForRow(i);
			for (int d = 0; d < n.length; d++) {
				if (mHelper.isWithinCurrentMonth(i, d))
					tmp[i][d] = new _calendar(n[d], true);
				else
					tmp[i][d] = new _calendar(n[d]);

			}
		}

		_calendar[][] temp = new _calendar[6][7];
		_calendar[][] temp2 = new _calendar[6][7];

		// for (int i = 0; i < temp.length; i++) {
		// for (int j = 0; j < temp[i].length; j++) {
		// if (j < 6) {
		// temp[i][j] = tmp[i][j + 1];
		// } else {
		// if (i == 5) {
		// temp[i][j] = new _calendar(tmp[i][j].day + 1);
		// } else {
		// temp[i][j] = tmp[i + 1][0];
		// }
		// }
		//
		// }
		// }

		// if (tmp[0][0].thisMonth) {//
		// mHelper.previousMonth();
		// int preMonthTotalDays = mHelper.getNumberOfDaysInMonth();
		// mHelper.nextMonth();
		// for (int i = temp2[0].length - 1; i > -1; i--) {
		// if (i == 6) {
		// temp2[0][6] = new _calendar(1, true);
		// } else {
		// temp2[0][i] = new _calendar(preMonthTotalDays--);
		// }
		// }
		//
		// for (int i = 1; i < 6; i++) {
		// temp2[i] = temp[i - 1];
		// }
		// tmp = temp2;
		// } else {
		// tmp = temp;
		// }

		// if (!tmp[5][0].thisMonth) {
		// setImageResource(R.drawable.calenda_background_2);
		// setScaleType(ScaleType.FIT_XY);
		// } else {
		// setImageResource(R.drawable.calenda_background);
		// setScaleType(ScaleType.FIT_XY);
		// }
		// if (!tmp[5][0].thisMonth ){
		//
		// } else if (!tmp[4][0].thisMonth) {
		//
		// }
		
		Calendar today = Calendar.getInstance();
		int thisDay = 0;
		mToday = null;
		if (mHelper.getYear() == today.get(Calendar.YEAR) && mHelper.getMonth() == today.get(Calendar.MONTH)) {
			thisDay = today.get(Calendar.DAY_OF_MONTH);
			selectDay = thisDay;
		}
		// build cells
		// Log.e("CELL_MARGIN_LEFT", CELL_MARGIN_LEFT + "");
		Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH + CELL_MARGIN_LEFT, CELL_HEIGH + CELL_MARGIN_TOP);
		for (int week = 0; week < mCells.length; week++) {
			for (int day = 0; day < mCells[week].length; day++) {
				if (tmp[week][day].thisMonth) {
					if (day == 0 || day == 6) {
						mCells[week][day] = new RedCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, mContext);
					} else {
						mCells[week][day] = new Cell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, mContext);
					}
					if (selectDay == tmp[week][day].day) {
						mSelectCell = mCells[week][day];
					}
					mCells[week][day].isThisMonth = true;
				} else {
					mCells[week][day] = new GrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, mContext);
					mCells[week][day].isThisMonth = false;
				}

				Bound.offset(CELL_WIDTH, 0); // move to next column

				// get today
				// if (tmp[week][day].day == thisDay &&
				// tmp[week][day].thisMonth) {
				// mToday = mCells[week][day];
				// mDecoration.setBounds(mToday.getBound());
				// circle.setBounds(mToday.getBound().centerX() - 25, mToday
				// .getBound().centerY(),
				// mToday.getBound().centerX() - 10, mToday.getBound()
				// .centerY() + 15);
				//
				// rectangle.setBounds(mToday.getBound().centerX() - 9, mToday
				// .getBound().centerY(),
				// mToday.getBound().centerX() + 8, mToday.getBound()
				// .centerY() + 15);
				// triangle.setBounds(mToday.getBound().centerX() + 10, mToday
				// .getBound().centerY(),
				// mToday.getBound().centerX() + 25, mToday.getBound()
				// .centerY() + 15);
				// }
				//

			}
			Bound.offset(0, CELL_HEIGH); // move to next row and first column
			Bound.left = CELL_MARGIN_LEFT;
			Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;
		}
	}

	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Rect re = getDrawable().getBounds();
		android.util.Log.d(TAG,
				"left=" + left + " right=" + right + " top=" + top + " bottom=" + bottom + " re.width=" + re.width() + " re.height" + re.height());
		CELL_HEIGH = (int) (((re.height() - CELL_MARGIN_TOP) / 6.0));
		// CELL_HEIGH = (int) (((re.height() - CELL_MARGIN_TOP) / 6.0) -
		// ViewScaleUtil.sScale * 3);
		CELL_WIDTH = (int) ((re.width() - 2) / 7.0);
		CELL_MARGIN_LEFT = (right - left - re.width()) / 2 + (re.width() - 7 * CELL_WIDTH) / 2;
		initCells();
		super.onLayout(changed, left, top, right, bottom);
	}

	public void setTimeInMillis(long milliseconds) {
		mRightNow.setTimeInMillis(milliseconds);
		initCells();
		this.invalidate();
	}

	public int getYear() {
		return mHelper.getYear();
	}

	public int getMonth() {
		return mHelper.getMonth();
	}

	public void setYearAndMonth(Calendar c) {
		mHelper = new MonthDisplayHelper(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
		initCells();
		invalidate();
	}

	public void nextMonth() {
		// mSelectCell = null;
		mHelper.nextMonth();
		initCells();
		invalidate();
	}

	public void previousMonth() {
		// mSelectCell = null;
		mHelper.previousMonth();
		initCells();
		invalidate();
	}

	public boolean firstDay(int day) {
		return day == 1;
	}

	public boolean lastDay(int day) {
		return mHelper.getNumberOfDaysInMonth() == day;
	}

	public void goToday() {
		Calendar cal = Calendar.getInstance();
		mHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		initCells();
		invalidate();
	}

	public Calendar getDate() {
		return mRightNow;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// if (mOnCellTouchListener != null) {
		// for (Cell[] week : mCells) {
		// for (Cell day : week) {
		// if (day.hitTest((int) event.getX(), (int) event.getY()) &&
		// day.isThisMonth) {
		// mOnCellTouchListener.onTouch(day);
		// }
		// }
		// }
		// }
		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			mGestureDetector.onTouchEvent(event);

		} else if (MotionEvent.ACTION_UP == event.getAction()) {
			mGestureDetector.onTouchEvent(event);
		}
		return true;
	}

	public void setOnCellTouchListener(OnCellTouchListener p) {
		mOnCellTouchListener = p;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw background
		super.onDraw(canvas);
		// draw cells

		int year = mHelper.getYear();
		int month = mHelper.getMonth() + 1;

		String tempYearAndMonth = year + "/" + month + "/01";
		String yearAndMonth = Util.formatToMomth(tempYearAndMonth);
		// DataHelper helper = DataHelper.getInstance(mContext);
		//
		// monthMap = helper.getMonthCategoryDis(AppSet.CURRENT_USER.userId,
		// yearAndMonth);
		int i = 0;
		for (Cell[] week : mCells) {
			for (Cell day : week) {
				if (unDoworkList != null) {
					Log.d("zaokun", "unDoworkList.get(day.getDayOfMonth====" + day.getDayOfMonth());
					if (unDoworkList.get(day.getDayOfMonth() - 1).total != 0) {
						Log.d("zaokun", "unDoworkList.get(day.getDayOfMonth() - 1======" + unDoworkList.get(day.getDayOfMonth() - 1).total);
						day.draw(canvas, unDoworkList.get(day.getDayOfMonth() - 1).total);
						continue;
					}
				}
				day.draw(canvas, 0);
			}
			// 划横线
			if (i != 5) {
				week[6].drawLine(canvas, i);
			}
			i++;
		}

		if (mSelectCell != null) {
			mSelectCell.drawCicle(canvas);
		}

		// if (mSelectCell != null) {
		// mSelectCell.drawCicle(canvas);
		// }

	}

	private class GrayCell extends Cell {
		public GrayCell(int dayOfMon, Rect rect, float s, Context context) {
			super(dayOfMon, rect, s, context);
			mPaint.setColor(Color.WHITE);
		}
	}

	private class RedCell extends Cell {
		public RedCell(int dayOfMon, Rect rect, float s, Context context) {
			super(dayOfMon, rect, s, context);
			mPaint.setColor(0xdddd0000);
		}

	}

	public String getMonthString() {

		String yearStr = mHelper.getYear() + "-";
		String monthStr = String.format("%02d", mHelper.getMonth() + 1);
		return yearStr + monthStr;
	}

	public String getDateString() {

		String yearStr = mHelper.getYear() + "-";
		String monthStr = String.format("%02d", mHelper.getMonth() + 1);
		return yearStr + monthStr;
	}

	public String getSelectDayTime() {
		String yearStr = mHelper.getYear() + "-";
		String monthStr = String.format("%02d", mHelper.getMonth() + 1) + "-";
		String dayStr = String.format("%02d", selectDay);
		return yearStr + monthStr + dayStr;
	}

	public int getSelectDay() {
		return selectDay;
	}

	public ArrayList<UndoWorkCollectInfo> getUnDoworkList() {
		return unDoworkList;
	}

	public void setUndoWorkTotal(ArrayList<UndoWorkCollectInfo> unDoworkList) {
		if (this.unDoworkList == null) {
			this.unDoworkList = new ArrayList<UndoWorkCollectInfo>();
		} else {
			this.unDoworkList.clear();
		}
		this.unDoworkList.addAll(unDoworkList);
		invalidate();
	}

}
