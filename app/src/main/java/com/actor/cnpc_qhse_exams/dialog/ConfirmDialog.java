package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.actor.cnpc_qhse_exams.databinding.DialogConfirmBinding;
import com.actor.myandroidframework.dialog.ViewBindingDialog;
import com.blankj.utilcode.util.SizeUtils;

/**
 * description: 确认框
 *
 * @author : ldf
 * date       : 2025/4/21 on 22
 * @version 1.0
 */
public class ConfirmDialog extends ViewBindingDialog<DialogConfirmBinding> {

    @Nullable
    private String title;
    @Nullable
    private String content;
    @Nullable
    private String cancelText;
    @Nullable
    private String sureText;
    private OnBtnClickListener onBtnClickListener;

    public ConfirmDialog(@NonNull Context context,
                         @Nullable String title,
                         @Nullable String content,
                         OnBtnClickListener onBtnClickListener) {
//        super(context);
        this(context, title, content, "取消", "确定", true, onBtnClickListener);
    }
    public ConfirmDialog(@NonNull Context context,
                         @Nullable String title,
                         @Nullable String content,
                         @Nullable String cancelText,   //默认 "取消"
                         @Nullable String sureText,     //默认 "确定"
                         boolean cancelAble,            //是否能取消, 默认 = true
                         OnBtnClickListener onBtnClickListener) {
        super(context);
        setCancelAble(cancelAble);
        setWidthPercent(0.8f, SizeUtils.dp2px(308f));
        this.title = title;
        this.content = content;
        this.cancelText = cancelText;
        this.sureText = sureText;
        this.onBtnClickListener = onBtnClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.tvTitle.setText(title);
        viewBinding.tvContent.setText(content);
        viewBinding.stvCancel.setText(cancelText);
        viewBinding.stvSure.setText(sureText);
        viewBinding.stvCancel.setOnClickListener(v -> {
            dismiss();
            if (onBtnClickListener != null) onBtnClickListener.onBtnClick(false);
        });
        viewBinding.stvSure.setOnClickListener(v -> {
            dismiss();
            if (onBtnClickListener != null) onBtnClickListener.onBtnClick(true);
        });
    }

    public interface OnBtnClickListener {
        void onBtnClick(boolean isConfirmClick);
    }
}
