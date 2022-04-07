package com.qy.groupselectview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class AutoLineAndCenterLayout extends ViewGroup {

    private static final String TAG = "<AutoLayout>";

    List<Integer> childOfLine;
    List<Integer> marginHorizontalOfLine;
    private boolean autoCenter;

    public boolean isAutoCenter() {
        return autoCenter;
    }

    public void setAutoCenter(boolean autoCenter) {
        this.autoCenter = autoCenter;
    }

    private int lp_width;
    private int lp_height;

    public AutoLineAndCenterLayout(Context context) {
        super(context);
        init(context, null);
    }

    public AutoLineAndCenterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoLineAndCenterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLineAndCenterLayout);
            autoCenter = typedArray.getBoolean(R.styleable.AutoLineAndCenterLayout_autoCenter, true);
        }

    }
//
//    public AutoLineAndCenterLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        LayoutParams layoutParams = getLayoutParams();
        lp_width = layoutParams.width;
        lp_height = layoutParams.height;
        childOfLine = new ArrayList<>();  //记录每行的view数
        marginHorizontalOfLine = new ArrayList<>();  //记录每行的view数
        int childCount = getChildCount();
        int contentHeight = 0;  //内部元素的总高度
        int contentWidth = 0;  //内部元素的总宽度
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec); //ViewGroup的总长度
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec); //ViewGroup的总长度
        int curLineChildCount = 0; //当前行的view数
        int curLineWidth = 0; //当前行的宽度
        int maxHeight = 0; //当前行的最大高度
        int maxWidth = 0; //所有行的最大宽度
        for (int i = 0; i < childCount; i++) {
            View childItem = getChildAt(i);
            measureChild(childItem, widthMeasureSpec, heightMeasureSpec);
            int childHeight = childItem.getMeasuredHeight();
            int childWidth = childItem.getMeasuredWidth();
            if (curLineWidth + childWidth <= totalWidth) {
                // 当前行的宽度加上此view的宽度小于总长度
                curLineWidth += childWidth;
                maxHeight = Math.max(childHeight, maxHeight);
                curLineChildCount++;

            } else {
                // 当前行的宽度加上此view的宽度大于总长度，换行
                int curLineMargin = (totalWidth - curLineWidth) / 2;
                childOfLine.add(curLineChildCount);
                marginHorizontalOfLine.add(curLineMargin);
                curLineWidth = childWidth;
                curLineChildCount = 1;
                contentHeight += maxHeight;
                maxHeight = childHeight;
            }
            maxWidth = Math.max(curLineWidth, maxWidth);
        }

        int curLineMargin = (totalWidth - curLineWidth) / 2;
        childOfLine.add(curLineChildCount);
        marginHorizontalOfLine.add(curLineMargin);

        for (int i = 0; i < childOfLine.size(); i++) {
            if (childOfLine.get(i) == 0) {
                childOfLine.remove(i);
            }
        }

        contentHeight += maxHeight;
        contentWidth = maxWidth;


        if (lp_width == LayoutParams.MATCH_PARENT && lp_height == LayoutParams.MATCH_PARENT) {
            setMeasuredDimension(totalWidth, totalHeight);
        } else if (lp_width == LayoutParams.WRAP_CONTENT && lp_height == LayoutParams.MATCH_PARENT) {
            setMeasuredDimension(contentWidth, totalHeight);
        } else if (lp_width == LayoutParams.MATCH_PARENT && lp_height == LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(totalWidth, contentHeight);
        } else {
            setMeasuredDimension(contentWidth, contentHeight);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int index = 0;
        int curHeight = 0; //当前行的起始高度
        for (int i = 0; i < childOfLine.size(); i++) {
            int childCount = childOfLine.get(i);
            int maxHeight = 0;
            int lineWidth = 0;
            int target = index + childCount;
            for (; index < target; index++) {
                View item = getChildAt(index);
                maxHeight = Math.max(maxHeight, item.getMeasuredHeight());
                //第一个属性：相对于父控件的左位置
                //第二个属性：相对于父控件的上位置
                //第三个属性：相对于父控件的右位置
                //第四个属性：相对于父控件的下位置
                if (autoCenter)
                    item.layout(lineWidth + marginHorizontalOfLine.get(i), curHeight, lineWidth + item.getMeasuredWidth() + marginHorizontalOfLine.get(i), curHeight + item.getMeasuredHeight());
                else
                    item.layout(lineWidth, curHeight, lineWidth + item.getMeasuredWidth(), curHeight + item.getMeasuredHeight());
                lineWidth += item.getMeasuredWidth();
            }
            curHeight += maxHeight;
        }
    }
}
