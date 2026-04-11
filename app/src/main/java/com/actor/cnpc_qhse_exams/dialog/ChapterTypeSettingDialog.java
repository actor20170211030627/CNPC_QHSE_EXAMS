package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.databinding.DialogChapterTypeSettingBinding;
import com.actor.myandroidframework.dialog.ViewBindingDialog;
import com.blankj.utilcode.util.SizeUtils;

/**
 * description: 章节 & 类型 设置
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 20
 * @version 1.0
 */
public class ChapterTypeSettingDialog extends ViewBindingDialog<DialogChapterTypeSettingBinding> {

    private final OnConfirmClickListener listener;

    public ChapterTypeSettingDialog(@NonNull Context context, @NonNull OnConfirmClickListener listener) {
        super(context);
        setWidthPercent(0.888888f, SizeUtils.dp2px(308f));
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.ivClose.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.stvCancel.setOnClickListener(v -> {
            dismiss();
        });
        viewBinding.stvYes.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirmClick(viewBinding.bsChapters.getSelectedItemPosition(),
                        viewBinding.bsTypes.getSelectedItemPosition()
                );
            }
            dismiss();
        });
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(int chapter, int type);
    }
}
