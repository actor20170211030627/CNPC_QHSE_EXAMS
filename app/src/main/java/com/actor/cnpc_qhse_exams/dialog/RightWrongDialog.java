package com.actor.cnpc_qhse_exams.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.databinding.DialogRightWrongBinding;
import com.actor.myandroidframework.dialog.ViewBindingDialog;
import com.blankj.utilcode.util.SizeUtils;

/**
 * description: 显示对错
 * company    :
 *
 * @author : ldf
 * date       : 2026/4/11 on 22
 * @version 1.0
 */
public class RightWrongDialog extends ViewBindingDialog<DialogRightWrongBinding> {

    public RightWrongDialog(@NonNull Context context, boolean isRight) {
        super(context);
        //即使在xml中设置宽高=150dp, 显示出来还是很窄, 原因???
//        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(SizeUtils.dp2px(50));
        isStatusBarDimmed(false);
        AppCompatImageView iv = viewBinding.getRoot();
        iv.setImageResource(isRight ? R.drawable.icon_right_blue_circle : R.drawable.icon_wrong_red_circle2);
        iv.setOnClickListener(v -> dismiss());
    }
}
