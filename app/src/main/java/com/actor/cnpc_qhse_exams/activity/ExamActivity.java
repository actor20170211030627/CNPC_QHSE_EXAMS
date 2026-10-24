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
import com.actor.cnpc_qhse_exams.global.Global;
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

    private boolean isPlayVoice = true;

    // TODO: 2026/4/11 弄个错题本
    private final       ExamAdapter mAdapter = new ExamAdapter((position, id, isAnswerRight) -> {
        new RightWrongDialog(mActivity, isAnswerRight).show();
        if (isPlayVoice) {
            MediaPlayerUtils.withRaw(isAnswerRight ? R.raw.right : R.raw.wrong).play(null);
        }
    });

    public static void start(BaseActivity<?> activity, View v, long version, int chapter, int type) {
        activity.startActivity(new Intent(activity, ExamActivity.class)
                .putExtra(Global.VERSION, version)
                .putExtra(Global.CHAPTER, chapter)
                .putExtra(Global.TYPE, type), v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long version = intent.getLongExtra(Global.VERSION, -1L);
        int chapter = intent.getIntExtra(Global.CHAPTER, 0);
        int type = intent.getIntExtra(Global.TYPE, 0);

        viewBinding.ivBack.setOnClickListener(v -> onBackPressed());
        viewBinding.ivSetting.setOnClickListener(v -> {
            new ExamSettingDialog(mActivity, isPlayVoice, isPlayVoice2 -> {
                this.isPlayVoice = isPlayVoice2;
            }).show();
        });
        viewBinding.stvPre.setOnClickListener(v -> {
            int currentItem = viewBinding.viewPager2.getCurrentItem();
            if (currentItem <= 0) {
                ToasterUtils.warning("没有上一题了");
            } else {
                viewBinding.viewPager2.setCurrentItem(-- currentItem);
            }
        });
        viewBinding.stvNext.setOnClickListener(v -> {
            int currentItem = viewBinding.viewPager2.getCurrentItem();
            if (currentItem >= mAdapter.getData().size() - 1) {
                ToasterUtils.warning("没有下一题了");
            } else {
                viewBinding.viewPager2.setCurrentItem(++ currentItem);
            }
        });

        viewBinding.viewPager2.setAdapter(mAdapter);
        //禁止用户左右滑动
//        viewBinding.viewPager2.setUserInputEnabled(false);
        List<SubjectDriver> subjectDrivers = SubjectSelectUtils.select(version, null, chapter, type);
        mAdapter.setList(subjectDrivers);
    }
}