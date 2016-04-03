package com.example.addicheung.myapplication;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断五个棋子是否连线工具类
 * Created by AddiCheung on 2016/4/3.
 */
public class CheckUtil {

    private static final int MAX_POINT_COUNT = 5;

    public CheckUtil() {

    }

    public boolean checkFiveLine(ArrayList<Point> mArray){
        for(Point p:mArray) {
            if (checkHorizental(p.x,p.y,mArray)) {
                return true;
            } else if (checkVertical(p.x,p.y,mArray)) {
                return true;
            } else if (checkLeftBevel(p.x,p.y,mArray)) {
                return true;
            } else if (checkRightBevel(p.x,p.y,mArray)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizental(int x,int y,ArrayList<Point> mArray){
        int count  = 1;
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x-i,y);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x+i,y);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        if(count==MAX_POINT_COUNT){
            return true;
        }
        return false;
    }

    private boolean checkVertical(int x,int y,ArrayList<Point> mArray){
        int count  = 1;
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x,y-i);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x,y+i);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        if(count==MAX_POINT_COUNT){
            return true;
        }
        return false;
    }

    private boolean checkLeftBevel(int x,int y,ArrayList<Point> mArray){
        int count  = 1;
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x-i,y+i);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x+i,y-i);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        if(count==MAX_POINT_COUNT){
            return true;
        }
        return false;
    }

    private boolean checkRightBevel(int x,int y,ArrayList<Point> mArray){
        int count  = 1;
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x-i,y-i);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        for(int i=1;i<MAX_POINT_COUNT;i++){
            Point p = new Point(x+i,y+i);
            if(mArray.contains(p)){
                count++;
            }else break;
        }
        if(count==MAX_POINT_COUNT){
            return true;
        }
        return false;
    }
}
