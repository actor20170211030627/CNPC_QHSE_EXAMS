package com.actor.cnpc_qhse_exams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.adapter.ExamAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.ActivityExamBinding;
import com.actor.cnpc_qhse_exams.dialog.ExamSettingDialog;
import com.actor.cnpc_qhse_exams.dialog.RightWrongDialog;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.myandroidframework.utils.audio.MediaPlayerUtils;
import com.actor.myandroidframework.utils.toaster.ToasterUtils;

import java.util.List;

/**
 * description: 测试
 * company    :
 * @author    : ldf
 * date       : 2026/4/11 on 18:36
 */
public class ExamActivity extends BaseActivity<ActivityExamBinding> {

    public static final String CHAPTER = "CHAPTER";
    public static final String      TYPE     = "TYPE";

    private int position = 0, dataSize = 0;
    private boolean isPlayVoice = true;

    // TODO: 2026/4/11 弄个错题本
    private final       ExamAdapter mAdapter = new ExamAdapter((position, id, isAnswerRight) -> {
        new RightWrongDialog(mActivity, isAnswerRight).show();
        if (isPlayVoice) {
            MediaPlayerUtils.getInstance().playRaw(isAnswerRight ? R.raw.right : R.raw.wrong, null);
        }
    });

    public static void start(BaseActivity<?> activity, View v, int chapter, int type) {
        activity.startActivity(new Intent(activity, ExamActivity.class)
                .putExtra(CHAPTER, chapter)
                .putExtra(TYPE, type), v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int chapter = intent.getIntExtra(CHAPTER, 0);
        int type = intent.getIntExtra(TYPE, 0);

        viewBinding.ivBack.setOnClickListener(v -> onBackPressed());
        viewBinding.ivSetting.setOnClickListener(v -> {
            new ExamSettingDialog(mActivity, isPlayVoice, isPlayVoice2 -> {
                this.isPlayVoice = isPlayVoice2;
            }).show();
        });
        viewBinding.stvPre.setOnClickListener(v -> {
            if (position <= 0) {
                ToasterUtils.warning("没有上一题了");
            } else {
                position --;
                viewBinding.viewPager2.setCurrentItem(position);
            }
        });
        viewBinding.stvNext.setOnClickListener(v -> {
            if (position >= dataSize - 1) {
                ToasterUtils.warning("没有下一题了");
            } else {
                position ++;
                viewBinding.viewPager2.setCurrentItem(position);
            }
        });

        viewBinding.viewPager2.setAdapter(mAdapter);
        //禁止用户左右滑动
        viewBinding.viewPager2.setUserInputEnabled(false);
        List<SubjectDriver> subjectDrivers = SubjectSelectUtils.select(null, chapter, type);
        // TODO: 2026/4/12 查同样的题型, 为何isShowAnswer状态有缓存???
        for (SubjectDriver subjectDriver : subjectDrivers) {
            subjectDriver.isShowAnswer = false;
        }
        dataSize = subjectDrivers.size();
        mAdapter.setList(subjectDrivers);
    }
}