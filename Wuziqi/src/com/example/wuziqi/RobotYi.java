package com.example.wuziqi;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.widget.Toast;

public class RobotYi {
	private ArrayList<Point>mBlackArrayList;
	private ArrayList<Point>mWhiteArrayList;
	private GoBangView mBangView;
	private boolean selfPiece;
	private int MAX_COUNT=3;
	
	public RobotYi(GoBangView goBangView,ArrayList<Point>blackArrayList,ArrayList<Point>whiteArrayList,boolean isWhite) {
		mBangView=goBangView;
		mBlackArrayList=blackArrayList;
		mWhiteArrayList=whiteArrayList;
		selfPiece=!isWhite;
	}
	/**
	 * 先判断对手是否有四颗连成一线且两端没有我方棋子的情况则任选一端下   其二是一端有自己棋子的情况选没有端下
	 * 再判断对手有无三颗棋子连成一线且两端没有自己棋子的情况，任选一端下  其二是一端有自己的棋子 则查找自己是否有
	 * @return
	 */
	public Point play_low(){
		mBangView.isManFirst=true;
		Point mPoint=new Point();
		Toast.makeText(mBangView.getContext(), "robot run", Toast.LENGTH_SHORT).show();
		if (selfPiece) {
			//己方为白棋
			ArrayList<Point>defenseArrayList=checkThreePointInLine(mBlackArrayList);
			for (int i = 0; i < mBlackArrayList.size(); i++) {
				
			}
			for (int i = 0; i < mWhiteArrayList.size(); i++) {
				
			}
		}else {
			//己方为黑棋
			for (int i = 0; i < mWhiteArrayList.size(); i++) {
				
			}
			for (int i = 0; i < mBlackArrayList.size(); i++) {
				
			}
		}
		
		return new Point();
	}
	
	public Point play_middle(){
		mBangView.isManFirst=true;
		return new Point();
	}
	
	public Point play_hard(){
		mBangView.isManFirst=true;
		return new Point();
	}
	
	
	private ArrayList<Point> checkThreePointInLine(List<Point> points) {
		// TODO Auto-generated method stub
		ArrayList<Point>pointss=new ArrayList<Point>();
		for (Point point : points) {
			int x = point.x;
			int y = point.y;
		/*	pointss = checkHorizontal(x, y, points);
			if (pointss.size()>0) {
				return pointss;
			}
			pointss = checkLeftDiagno(x, y, points);
			if (pointss.size()>0) {
				return pointss;
			}
			pointss = checkVertical(x, y, points);
			if (pointss.size()>0) {
				return pointss;
			}
			pointss = checkRightDiagno(x, y, points);
			if (pointss.size()>0) {
				return pointss;
			}*/
		}
		return pointss;
	}

	private boolean checkLeftDiagno(int x, int y, List<Point> points) {
		// TODO Auto-generated method stub
		ArrayList<Point>temp=new ArrayList<Point>();
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
		ArrayList<Point>temp=new ArrayList<Point>();
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
		ArrayList<Point>temp=new ArrayList<Point>();
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
		ArrayList<Point>temp=new ArrayList<Point>();
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

	
}
