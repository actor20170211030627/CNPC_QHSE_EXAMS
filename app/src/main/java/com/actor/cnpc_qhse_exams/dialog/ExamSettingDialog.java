package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.databinding.DialogExamSettingBinding;
import com.actor.myandroidframework.dialog.ViewBindingDialog;
import com.blankj.utilcode.util.SizeUtils;

/**
 * description: 测试 设置
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 20
 * @version 1.0
 */
public class ExamSettingDialog extends ViewBindingDialog<DialogExamSettingBinding> {

    private final boolean isPlayVoice;
    private final OnConfirmClickListener listener;

    public ExamSettingDialog(@NonNull Context context, boolean isPlayVoice, @NonNull OnConfirmClickListener listener) {
        super(context);
        setWidthPercent(0.888888f, SizeUtils.dp2px(308f));
        this.isPlayVoice = isPlayVoice;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.tvPlayVoice.setSelected(isPlayVoice);

        viewBinding.tvPlayVoice.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });
        viewBinding.ivClose.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.stvCancel.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.stvYes.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirmClick(viewBinding.tvPlayVoice.isSelected());
            }
            dismiss();
        });
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(boolean isPlayVoice2);
    }
}
