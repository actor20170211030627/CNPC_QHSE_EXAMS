package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.databinding.DialogStudySettingBinding;
import com.actor.myandroidframework.dialog.ViewBindingDialog;
import com.blankj.utilcode.util.SizeUtils;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 20
 * @version 1.0
 */
public class StudySettingDialog extends ViewBindingDialog<DialogStudySettingBinding> {

    private final boolean                isShowTestPoint, isShowAnswer, isShowAnalysis;
    private final OnConfirmClickListener listener;

    public StudySettingDialog(@NonNull Context context, boolean isShowTestPoint, boolean isShowAnswer, boolean isShowAnalysis, @NonNull OnConfirmClickListener listener) {
        super(context);
        setWidthPercent(0.888888f, SizeUtils.dp2px(308f));
        this.isShowTestPoint = isShowTestPoint;
        this.isShowAnswer = isShowAnswer;
        this.isShowAnalysis = isShowAnalysis;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.ivClose.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.tvShowTestPoint.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });
        viewBinding.tvShowAnswer.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });
        viewBinding.tvShowAnalysis.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });
        viewBinding.tvShowScreen.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });
        viewBinding.stvCancel.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.stvYes.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirmClick(viewBinding.tvShowTestPoint.isSelected(),
                        viewBinding.tvShowAnswer.isSelected(),
                        viewBinding.tvShowAnalysis.isSelected(),
                        viewBinding.tvShowScreen.isSelected()
                );
            }
            dismiss();
        });

        viewBinding.tvShowTestPoint.setSelected(isShowTestPoint);
        viewBinding.tvShowAnswer.setSelected(isShowAnswer);
        viewBinding.tvShowAnalysis.setSelected(isShowAnalysis);
//        viewBinding.tvShowScreen.setSelected(EasyWindow.existWindowShowingByClass(EasyWindowSubjects.class));
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(boolean isShowTestPoint, boolean isShowAnswer, boolean isShowAnalysis, boolean isShow2Screen);
    }
}
