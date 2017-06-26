package com.yeer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lvyb
 * 自适应的流失布局
 */
public class FlexBoxLayout extends ViewGroup{
    private int mScreenWidth;
    public int myHeight=0;
    private int horizontalSpace, verticalSpace;
    private float mDensity;//设备密度，用于将dp转为px
    public FlexBoxLayout(Context context) {
        this(context, null);
    }

    public FlexBoxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取屏幕宽高、设备密度
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mDensity = context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int myWidth = resolveSize(0, widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        // Measure each child and put the child to the right of previous child
        // if there's enough room for it, otherwise, wrap the line and put the child to next line.
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            } else {
                continue;
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft;
                childTop += verticalSpace + lineHeight;
                lineHeight = childHeight;
                i--;
                continue;
            } else {
                childLeft += childWidth + horizontalSpace;
            }
        }

        int wantedHeight = childTop + lineHeight + paddingBottom;

        setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    public int getMyHeight() {
        return myHeight;
    }

    public void setMyHeight(int myHeight) {
        this.myHeight = myHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int i1, int i2, int i3) {
        int myWidth = i2 - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft;
                childTop += verticalSpace + lineHeight;
                lineHeight = childHeight;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + horizontalSpace;
        }
        Log.i("infoOnLayout",getChildCount()+"\t");
    }
    /**
     * dp转为px
     *
     * @param dpValue
     * @return
     */
    private int dip2px(float dpValue) {
        return (int) (dpValue * mDensity + 0.5f);
    }
    /**
     * 设置子view间的水平间距 单位dp
     *
     * @param horizontalSpace
     */
    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }
    /**
     * 设置子view间的垂直间距 单位dp
     *
     * @param verticalSpace
     */
    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

}
