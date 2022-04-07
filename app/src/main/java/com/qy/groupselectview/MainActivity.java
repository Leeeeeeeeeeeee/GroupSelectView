package com.qy.groupselectview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "<MainActivity>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AutoLineAndCenterLayout mAlContainer = findViewById(R.id.al_container);

        EditText mEtGroupNum = findViewById(R.id.et_group_num);
        EditText mEtAthleteOfEachGroup = findViewById(R.id.et_athlete_of_each_group);

        GroupSelectView groupSelectView = findViewById(R.id.group_select_view);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int groupNum = Integer.parseInt(mEtGroupNum.getText().toString());
                int athleteEachGroup = Integer.parseInt(mEtAthleteOfEachGroup.getText().toString());
                groupSelectView.setAttrs(getContext(), groupNum, athleteEachGroup, -1);
            }
        });
        TextView mTvMsg = findViewById(R.id.tv_msg);
        groupSelectView.setMemberOnClickListener(new GroupSelectView.MemberOnClickListener() {
            @Override
            public void memberOnClick(int groupSeq, int athleteSeq) {
                Log.e(TAG, "memberOnClick: " + groupSeq + " " + athleteSeq);
                mTvMsg.setText("第" + groupSeq + "组，第" + athleteSeq + "位运动员被点击了");
            }
        });
        Button mBtnChange = findViewById(R.id.btn_change);
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupSelectView.setMemberName(1, 1, "李清秋");
            }
        });
    }

    public Context getContext() {
        return this;
    }

}