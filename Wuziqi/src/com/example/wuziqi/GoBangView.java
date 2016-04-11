package com.example.wuziqi;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.app.usage.UsageEvents.Event;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GoBangView extends View {

	private float mlineheight = 0f;
	private int MAX_LINE = 13;
	private int mPanelWidth;
	private int MAX_COUNT = 5;

	private Paint mPaint, geziPaint, heartPaint;
	private Bitmap mBlackpiece;
	private Bitmap mWhitepiece;
	private float ratioOfPiece = 3.0f / 4f;

	private ArrayList<Point> mBlackList = new ArrayList<Point>();
	private ArrayList<Point> mWhiteList = new ArrayList<Point>();
	private boolean isWhite = true;// 白棋先手,或者轮到白棋

	private boolean mIsGameOver = false, isWhiteWinner;

	SoundPool mSoundPool;
	private int xiaqiMusic;
	
	//人先手
	public boolean isManFirst=true;
	private RobotYi mRobotYi;
	// 存储状态
	private static String INSTANCE = "instance";
	private static String INSTANCE_WHITEARRAY = "instance_whitearry";
	private static String INSTANCE_BLACKARRAY = "instance_blackarry";
	private static String INSTANCE_GAMEOVER = "instance_gameover";

	
	
	public ArrayList<Point> getmBlackList() {
		return mBlackList;
	}

	public void setmBlackList(ArrayList<Point> mBlackList) {
		this.mBlackList = mBlackList;
	}

	public ArrayList<Point> getmWhiteList() {
		return mWhiteList;
	}

	public void setmWhiteList(ArrayList<Point> mWhiteList) {
		this.mWhiteList = mWhiteList;
	}

	public boolean isManFirst() {
		return isManFirst;
	}

	public void setManFirst(boolean isManFirst) {
		this.isManFirst = isManFirst;
	}

	public GoBangView(Context context) {
		this(context, null);
	}

	public boolean isWhiteWinner() {
		return isWhiteWinner;
	}

	public GoBangView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		geziPaint = new Paint();
		heartPaint = new Paint();
		init();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
		bundle.putParcelableArrayList(INSTANCE_BLACKARRAY, mBlackList);
		bundle.putParcelableArrayList(INSTANCE_WHITEARRAY, mWhiteList);
		bundle.putBoolean(INSTANCE_GAMEOVER, mIsGameOver);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			mBlackList = ((Bundle) state).getParcelableArrayList(INSTANCE_BLACKARRAY);
			mWhiteList = ((Bundle) state).getParcelableArrayList(INSTANCE_WHITEARRAY);
			mIsGameOver = ((Bundle) state).getBoolean(INSTANCE_GAMEOVER);
			super.onRestoreInstanceState(((Bundle) state).getParcelable(INSTANCE));
			return;
		}
		super.onRestoreInstanceState(state);
	}

	private void init() {
		mRobotYi=new RobotYi(this,mBlackList,mWhiteList,isWhite);
		mPaint.setColor(Color.BLUE);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(3.5f);
		geziPaint.setAntiAlias(true);
		geziPaint.setDither(true);
		geziPaint.setStyle(Style.FILL);
		heartPaint.setAntiAlias(true);
		heartPaint.setDither(true);
		heartPaint.setStyle(Style.FILL);
		heartPaint.setColor(0xffFFAEB9);

		mBlackpiece = BitmapFactory.decodeResource(getResources(), R.drawable.cha);
		mWhitepiece = BitmapFactory.decodeResource(getResources(), R.drawable.quan);

		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
		xiaqiMusic = mSoundPool.load(getContext(), R.raw.nextstep, 1);
	}

	public void start() {
		mWhiteList.clear();
		mBlackList.clear();
		mIsGameOver = false;
		isWhiteWinner = false;
		isWhite = false;
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mPanelWidth = w;
		float depart = (mPanelWidth * 1.0f / MAX_LINE) / 4;
		mlineheight = (mPanelWidth - depart) / MAX_LINE;
		int pieceWidht = (int) (mlineheight * ratioOfPiece);
		mBlackpiece = Bitmap.createScaledBitmap(mBlackpiece, pieceWidht, pieceWidht, false);
		mWhitepiece = Bitmap.createScaledBitmap(mWhitepiece, pieceWidht, pieceWidht, false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int width = Math.min(widthSize, heightSize);

		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = heightSize;
		} else if (heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}
		setMeasuredDimension(width, width);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mIsGameOver) {
			return false;
		}
		if (!isManFirst) {
			return false;
		}
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			
			isManFirst=false;
			int x = (int) event.getX();
			int y = (int) event.getY();
			Point mPoint = getValidPoint(x, y);
			if (mWhiteList.contains(mPoint) || mBlackList.contains(mPoint)) {
				return false;
			}
			mSoundPool.setVolume(xiaqiMusic, 1, 1);
			mSoundPool.play(xiaqiMusic, 1, 1, 0, 0, 1);
			if (isWhite) {
				mWhiteList.add(mPoint);
			} else {
				mBlackList.add(mPoint);
			}
			invalidate();
			isWhite = !isWhite;
			
			if (!isManFirst) {
				mRobotYi.play_low();
			}
		}
		return true;
	}

	private Point getValidPoint(int x, int y) {
		// TODO Auto-generated method stub
		return new Point((int) (x / mlineheight), (int) (y / mlineheight));
	}

	@Override
	public void draw(Canvas canvas) {
		//drawBoard(canvas);
		drawHeartBoard(canvas);
		drawPiece(canvas);
		checkGameIsOver();
	}

	OnGameIsOverListener mOnGameIsOverListener = null;

	public interface OnGameIsOverListener {
		void alert();
	}

	public void setOnGameIsOverListener(OnGameIsOverListener onGameIsOverListener) {
		mOnGameIsOverListener = onGameIsOverListener;
	}

	private void checkGameIsOver() {
		// TODO Auto-generated method stub
		boolean whiteWin = checkFiveInLine(mWhiteList);
		boolean blackWin = checkFiveInLine(mBlackList);
		if (whiteWin || blackWin) {
			mIsGameOver = true;
			isWhiteWinner = whiteWin;
			mOnGameIsOverListener.alert();
			// Toast.makeText(getContext(), isWhiteWinner==true?"白棋胜利":"黑棋胜利",
			// Toast.LENGTH_SHORT).show();
		}
	}

	private boolean checkFiveInLine(List<Point> points) {
		// TODO Auto-generated method stub
		for (Point point : points) {
			int x = point.x;
			int y = point.y;
			boolean win = checkHorizontal(x, y, points);
			if (win) {
				return true;
			}
			win = checkLeftDiagno(x, y, points);
			if (win) {
				return true;
			}
			win = checkVertical(x, y, points);
			if (win) {
				return true;
			}
			win = checkRightDiagno(x, y, points);
			if (win) {
				return true;
			}
		}
		return false;
	}

	private boolean checkLeftDiagno(int x, int y, List<Point> points) {
		// TODO Auto-generated method stub
		int count = 1;
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x - i, y + i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT) {
			return true;
		}
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x + i, y - i))) {
				count++;
			} else {
				break;
			}
		}

		if (count == MAX_COUNT) {
			return true;
		}
		return false;
	}

	private boolean checkRightDiagno(int x, int y, List<Point> points) {
		// TODO Auto-generated method stub
		int count = 1;
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x - i, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT) {
			return true;
		}
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x + i, y + i))) {
				count++;
			} else {
				break;
			}
		}

		if (count == MAX_COUNT) {
			return true;
		}
		return false;
	}

	private boolean checkVertical(int x, int y, List<Point> points) {
		// TODO Auto-generated method stub
		int count = 1;
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT) {
			return true;
		}
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x, y + i))) {
				count++;
			} else {
				break;
			}
		}

		if (count == MAX_COUNT) {
			return true;
		}
		return false;
	}

	private boolean checkHorizontal(int x, int y, List<Point> points) {
		// TODO Auto-generated method stub
		int count = 1;
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x - i, y))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT) {
			return true;
		}
		for (int i = 1; i < MAX_COUNT; i++) {
			if (points.contains(new Point(x + i, y))) {
				count++;
			} else {
				break;
			}
		}

		if (count == MAX_COUNT) {
			return true;
		}
		return false;
	}

	private void drawPiece(Canvas canvas) {
		// TODO Auto-generated method stub
		/*
		 * for (int i = 0, n = mWhiteList.size(); i < n; i++) { Point mPoint =
		 * mWhiteList.get(i); //棋子落在线上 canvas.drawBitmap(mWhitepiece, (mPoint.x
		 * + (1 - ratioOfPiece) / 2) * mlineheight, (mPoint.y + (1 -
		 * ratioOfPiece) / 2) * mlineheight, null); } for (int i = 0,
		 * n=mBlackList.size(); i < n; i++) { Point mPoint=mBlackList.get(i);
		 * canvas.drawBitmap(mBlackpiece,
		 * (mPoint.x+(1-ratioOfPiece)/2)*mlineheight,
		 * (mPoint.y+(1-ratioOfPiece)/2)*mlineheight, null); }
		 */

		// 棋子落在格子当中
		for (int i = 0, n = mWhiteList.size(); i < n; i++) {
			Point mPoint = mWhiteList.get(i);
			// 棋子落在线上
			canvas.drawBitmap(mWhitepiece, (mPoint.x + 1f / 4f) * mlineheight, (mPoint.y + 1f / 4f) * mlineheight,
					null);
		}
		for (int i = 0, n = mBlackList.size(); i < n; i++) {
			Point mPoint = mBlackList.get(i);
			canvas.drawBitmap(mBlackpiece, (mPoint.x + 1f / 4f) * mlineheight, (mPoint.y + 1f / 4f) * mlineheight,
					null);
		}

	}

	private void drawBoard(Canvas canvas) {
		int w = mPanelWidth;
		float lineheight = mlineheight;

		int mColor = 0x1ffffff;
		for (int i = 0; i < MAX_LINE; i++) {
			for (int j = 0; j < MAX_LINE; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						mColor = 0xffEEE8AA;
					} else {
						mColor = Color.WHITE;
					}
				} else {
					if (j % 2 == 0) {
						mColor = Color.WHITE;
					} else {
						mColor = 0xffEEE8AA;
					}
				}

				geziPaint.setColor(mColor);
				float left = (float) ((j + 0.125) * lineheight);
				float top = (float) ((i + 0.125) * lineheight);
				float right = (float) ((j + 0.125) * lineheight + lineheight);
				float bottom = (float) ((i + 0.125) * lineheight + lineheight);
				canvas.drawRect(left, top, right, bottom, geziPaint);
			}
		}
		for (int i = 0; i <= MAX_LINE; i++) {
			float startX = (float) (lineheight / 8);
			float endX = (float) (w - lineheight / 8);
			float y = (float) ((0.125 + i) * lineheight);
			canvas.drawLine(startX, y, endX, y, mPaint);
			canvas.drawLine(y, startX, y, endX, mPaint);
		}

	}

	private void drawHeartBoard(Canvas canvas) {
		int w = mPanelWidth;
		float lineheight = mlineheight;

		int mColor = 0x1ffffff;
		int h_midValue = (MAX_LINE / 2) + 1;
		int v_midValue = MAX_LINE / 2;

		for (int i = 0; i < MAX_LINE; i++) {
			for (int j = 0; j < MAX_LINE; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						mColor = 0xffEEE8AA;
					} else {
						mColor = Color.WHITE;
					}
				} else {
					if (j % 2 == 0) {
						mColor = Color.WHITE;
					} else {
						mColor = 0xffEEE8AA;
					}
				}

				geziPaint.setColor(mColor);
				float left = (float) ((j + 0.125) * lineheight);
				float top = (float) ((i + 0.125) * lineheight);
				float right = (float) ((j + 0.125) * lineheight + lineheight);
				float bottom = (float) ((i + 0.125) * lineheight + lineheight);
				canvas.drawRect(left, top, right, bottom, geziPaint);
			}
		}

		for (int x=1,y=5; x<11&&y>=0; x++,y--) {
			for(int xx=x,yy=y;xx<=x+5&&yy<=y+5;xx++,yy++){
				if ((xx==6&&yy==0)||(xx==6&&yy==2)||(xx==5&&yy==1)||(xx==7&&yy==1)) {
					continue;
				}
				float left=(float) ((xx+0.125)*lineheight);
				float top=(float) ((yy+0.125)*lineheight);
				float right=(float) ((xx + 0.125) * lineheight + lineheight);
				float bottom=(float) ((yy + 0.125) * lineheight + lineheight);
				canvas.drawRect(left, top, right, bottom, heartPaint);
			}

		}
		
		for (int i = 0; i <= MAX_LINE; i++) {
			float startX = (float) (lineheight / 8);
			float endX = (float) (w - lineheight / 8);
			float y = (float) ((0.125 + i) * lineheight);
			canvas.drawLine(startX, y, endX, y, mPaint);
			canvas.drawLine(y, startX, y, endX, mPaint);
		}

	}

}
