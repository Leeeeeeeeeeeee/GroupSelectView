package com.qy.groupselectview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupSelectView extends AutoLineAndCenterLayout implements View.OnClickListener {
    private static final String TAG = "<GroupSelectView>";
    private static final int AUTO_COLUMN = -1;
    private int groupNum;
    private int athleteEachGroup;
    private int column;
    private int startId = -1;
    private LayoutInflater layoutInflater;

    public void setAttrs(Context context, int groupNum, int athleteEachGroup, int column) {
        this.groupNum = groupNum;
        this.athleteEachGroup = athleteEachGroup;
        this.column = column;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        onAttachedToWindow();
    }

    public GroupSelectView(Context context) {
        super(context);
        init(context, null);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        generateView(layoutInflater);
    }

    public GroupSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        generateView(layoutInflater);
    }

    public GroupSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GroupSelectView);
            groupNum = typedArray.getInt(R.styleable.GroupSelectView_groupNum, 1);
            athleteEachGroup = typedArray.getInt(R.styleable.GroupSelectView_athleteEachGroup, 0);
            column = typedArray.getInt(R.styleable.GroupSelectView_column, -1);
            Log.e(TAG, "groupNum:" + groupNum + " athleteEachGroup:" + athleteEachGroup + " column:" + column);
        }
    }

    private void generateView(LayoutInflater layoutInflater) {
        removeAllViews();
        startId = -1;
        for (int i = 0; i < groupNum; i++) {
            LinearLayout mLlGroupContainer = (LinearLayout) layoutInflater.inflate(R.layout.item_group, null);
            generateGroupView(layoutInflater, mLlGroupContainer);
            addView(mLlGroupContainer);
        }
    }

    /**
     * @param groupSeq   从0开始
     * @param memeberSeq 从0开始
     */
    public void setMemberName(int groupSeq, int memeberSeq, String memberName) {

        int memberViewId = startId + (groupSeq * athleteEachGroup) + memeberSeq;

        LinearLayout mLlAthleteContainer = findViewById(memberViewId);
        TextView mTvName = mLlAthleteContainer.findViewById(R.id.tv_name);
        mTvName.setText(memberName);

        Log.d(TAG, "setMemberName: memberViewId" + memberViewId);
    }

    private void generateGroupView(LayoutInflater layoutInflater, LinearLayout mLlGroupContainer) {
        AutoLineAndCenterLayout mLlGroup = mLlGroupContainer.findViewById(R.id.al_group_container);
        for (int i = 0; i < athleteEachGroup; i++) {
            LinearLayout mLlAthleteContainer = (LinearLayout) layoutInflater.inflate(R.layout.item_athlete, null);
            if (-1 == startId) {
                startId = generateViewId();
                mLlAthleteContainer.setId(startId);
                Log.d(TAG, "generateGroupView: startId:" + startId);
            } else {
                int id = generateViewId();
                Log.d(TAG, "generateGroupView: id:" + id);
                mLlAthleteContainer.setId(id);
            }
            mLlAthleteContainer.setOnClickListener(this);
            mLlGroup.addView(mLlAthleteContainer);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout: ");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
        generateView(layoutInflater);
    }

    @Override
    public void onClick(View v) {
        if (memberOnClickListener != null) {
            int id = v.getId() - startId;
            Log.e(TAG, "onClick: " + id);
            memberOnClickListener.memberOnClick(id / athleteEachGroup, id % athleteEachGroup);
        }
    }

    private MemberOnClickListener memberOnClickListener;

    public void setMemberOnClickListener(MemberOnClickListener memberOnClickListener) {
        this.memberOnClickListener = memberOnClickListener;
    }

    public interface MemberOnClickListener {
        void memberOnClick(int groupSeq, int athleteSeq);
    }
}
