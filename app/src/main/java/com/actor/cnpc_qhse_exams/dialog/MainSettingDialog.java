package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.databinding.DialogMainSettingBinding;
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
public class MainSettingDialog extends ViewBindingDialog<DialogMainSettingBinding> {

    private final boolean                isShowAnalysis;
    private final OnConfirmClickListener listener;

    public MainSettingDialog(@NonNull Context context, boolean isShowAnalysis, @NonNull OnConfirmClickListener listener) {
        super(context);
        setWidthPercent(0.888888f, SizeUtils.dp2px(308f));
        this.isShowAnalysis = isShowAnalysis;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.ivClose.setOnClickListener(v -> {
            dismiss();
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
                listener.onConfirmClick(viewBinding.tvShowAnalysis.isSelected(), viewBinding.tvShowScreen.isSelected());
            }
            dismiss();
        });

        viewBinding.tvShowAnalysis.setSelected(isShowAnalysis);
//        viewBinding.tvShowScreen.setSelected(EasyWindow.existWindowShowingByClass(EasyWindowSubjects.class));
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(boolean isShowAnalysis, boolean isShow2Screen);
    }
}
